package org.riverdell.robotics.gamepad

import com.qualcomm.robotcore.hardware.Gamepad
import java.lang.Thread.sleep
import kotlin.concurrent.thread

/**
 * @author GrowlyX
 * @since 9/4/2023
 */
class GamepadCommands(private val gamepad: Gamepad)
{
    enum class ButtonBehavior(val requiresLock: Boolean = false)
    {
        Continuous,
        Single(requiresLock = true)
    }

    data class ButtonMapping(
        val handler: () -> Unit,
        val behavior: ButtonBehavior,
        // TODO: B + Y will trigger a B listener (possibly?)
        //  associate by ButtonType and break if a larger expression passes
        // val totalExpressionAdditions: Int,
        val releaseTrigger: (() -> Unit)? = null,
        var delay: Long?,
        var lastTrigger: Long = 0L,
        var lock: Boolean = false,
    )

    private val listeners = mutableMapOf<() -> Boolean, ButtonMapping>()
    private var thread: Thread? = null

    fun where(base: ButtonType) = ButtonMappingBuilder { isActive(base) }

    fun startListening()
    {
        check(thread == null)

        thread = thread(isDaemon = true) {
            while (true)
            {
                for ((expr, mapping) in listeners)
                {
                    // if the expression is true, trigger the handler.
                    if (expr())
                    {
                        // if this requires a lock (single-use), don't continue until it's released
                        if (mapping.behavior.requiresLock)
                        {
                            if (mapping.lock)
                            {
                                continue
                            }
                        }

                        // we lock just in-case for release triggers to work properly
                        mapping.lock = true

                        if (mapping.delay != null)
                        {
                            // Ensure we don't exceed the delay
                            if (mapping.lastTrigger + mapping.delay!! > System.currentTimeMillis())
                            {
                                continue
                            }

                            mapping.lastTrigger = System.currentTimeMillis()
                        }

                        runCatching {
                            mapping.handler()
                        }.onFailure {
                            it.printStackTrace()
                        }
                        continue
                    }

                    // If previously locked, and a release trigger is set, release the lock.
                    if (mapping.releaseTrigger != null && mapping.lock)
                    {
                        mapping.releaseTrigger!!()
                    }

                    mapping.lock = false
                    mapping.lastTrigger = 0L
                }

                sleep(10L)
            }
        }
    }

    fun stopListening() = with(this) {
        checkNotNull(thread)
        thread!!.interrupt()
        thread = null
    }

    fun isActive(base: ButtonType) = base.gamepadMapping(gamepad)

    inner class ButtonMappingBuilder(
        private var expression: () -> Boolean = { true }
    )
    {
        fun and(b: ButtonType) = apply {
            val prevExp = expression
            expression = { prevExp() && isActive(b) }
        }

        fun andNot(b: ButtonType) = apply {
            val prevExp = expression
            expression = { prevExp() && !(isActive(b)) }
        }

        fun or(customizer: ButtonMappingBuilder.() -> ButtonMappingBuilder) = apply {
            val prevExp = expression
            val customizedExpr = customizer(ButtonMappingBuilder()).expression

            expression = { prevExp() || customizedExpr() }
        }

        fun onlyWhen(lambda: () -> Boolean) = apply {
            val prevExp = expression
            expression = { prevExp() && lambda() }
        }

        fun onlyWhenNot(lambda: () -> Boolean) = apply {
            val prevExp = expression
            expression = { prevExp() && !lambda() }
        }

        @JvmOverloads
        fun triggers(executor: () -> Unit, delay: Long? = null): InternalButtonMappingBuilderWithExecutor
        {
            check(delay == null || delay >= 50L) {
                "Delay cannot be less than 50ms as that is the tick speed"
            }

            return InternalButtonMappingBuilderWithExecutor(executor, delay)
        }

        inner class InternalButtonMappingBuilderWithExecutor(
            private val executor: () -> Unit,
            private val delay: Long?,
            private var built: Boolean = false
        )
        {
            fun whenPressedOnce()
            {
                check(delay == null) {
                    "Delay is not applicable to single-press buttons"
                }

                check(!built) {
                    "Button already mapped"
                }
                build(ButtonBehavior.Single)
            }

            fun repeatedlyWhilePressed()
            {
                check(!built) {
                    "Button already mapped"
                }
                build(ButtonBehavior.Continuous, delay = delay ?: 50L)
            }

            fun repeatedlyWhilePressedUntilReleasedWhere(lockRelease: () -> Unit)
            {
                check(!built) {
                    "Button already mapped"
                }
                build(ButtonBehavior.Continuous, lockRelease, delay ?: 50L)
            }

            fun andIsHeldUntilReleasedWhere(lockRelease: () -> Unit)
            {
                check(delay == null) {
                    "Delay is not applicable to held buttons"
                }
                check(!built) {
                    "Button already mapped"
                }

                // behavior is technically single
                build(ButtonBehavior.Single, lockRelease)
            }

            private fun build(
                behavior: ButtonBehavior,
                onRelease: (() -> Unit)? = null,
                delay: Long? = null
            ) = also {
                // return unit to prevent chaining of commands which is HIDEOUS
                listeners[expression] = ButtonMapping(
                    handler = executor,
                    behavior = behavior,
                    releaseTrigger = onRelease,
                    delay = delay
                )
                built = true
            }
        }
    }
}

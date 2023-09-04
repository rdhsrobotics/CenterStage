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
    enum class ButtonBehavior
    {
        Continuous, Single
    }

    data class ButtonMapping(
        val handler: () -> Unit,
        val behavior: ButtonBehavior,
        var lock: Boolean = false
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
                    if (expr())
                    {
                        if (mapping.behavior == ButtonBehavior.Single)
                        {
                            if (mapping.lock)
                            {
                                continue
                            }
                        }

                        runCatching {
                            mapping.handler()
                        }.onFailure {
                            it.printStackTrace()
                        }
                    } else
                    {
                        mapping.lock = false
                    }
                }

                sleep(50L)
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

        fun combinedWithNot(b: ButtonType) = apply {
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

        fun runs(executor: () -> Unit) = InternalButtonMappingBuilderWithExecutor(executor)

        inner class InternalButtonMappingBuilderWithExecutor(
            private val executor: () -> Unit
        )
        {
            fun whenPressedOnce() = build(ButtonBehavior.Single)
            fun whileItIsBeingPressed() = build(ButtonBehavior.Continuous)

            private fun build(behavior: ButtonBehavior) = also {
                // return unit to prevent chaining of commands which is HIDEOUS
                listeners[expression] = ButtonMapping(
                    handler = executor,
                    behavior = behavior
                )
            }
        }
    }
}

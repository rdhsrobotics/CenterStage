package org.riverdell.robotics.xdk.opmodes.subsystem.claw

import com.arcrobotics.ftclib.util.Timing.Timer
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.ElapsedTime
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.xdk.opmodes.autonomous.hardware
import org.riverdell.robotics.xdk.opmodes.subsystem.motionprofile.AsymmetricMotionProfile
import org.riverdell.robotics.xdk.opmodes.subsystem.motionprofile.ProfileConstraints
import kotlin.math.abs
import kotlin.properties.Delegates

class ExtendableClaw(private val opMode: LinearOpMode) : AbstractSubsystem()
{
    companion object
    {
        @JvmStatic
        var maxExtenderAddition = 0.0
        @JvmStatic
        var clawRangeExpansion = 0.0
    }

    private val backingExtender by lazy {
        opMode.hardware<Servo>("extender")
    }

    val backingClawOpenerRight by lazy {
        opMode.hardware<Servo>("clawRight")
    }

    val backingClawOpenerLeft by lazy {
        opMode.hardware<Servo>("clawLeft")
    }

    override fun composeStageContext() = object : StageContext
    {
        override fun isCompleted() = true
    }

    enum class ClawType(val servo: (ExtendableClaw) -> Servo)
    {
        Left({ it.backingClawOpenerLeft }),
        Right({ it.backingClawOpenerRight })
    }

    enum class ClawState(
        val calculatePosition: (ClawType) -> Double
    )
    {
        Open({
            if (it == ClawType.Left)
                ClawExpansionConstants.OPEN_LEFT_CLAW else
                ClawExpansionConstants.OPEN_RIGHT_CLAW
        }),
        Closed({
            if (it == ClawType.Left)
                ClawExpansionConstants.CLOSED_LEFT_CLAW else
                ClawExpansionConstants.CLOSED_RIGHT_CLAW
        })
    }

    enum class ExtenderState(val targetPosition: () -> Double)
    {
        PreLoad({
            ClawExpansionConstants.PRELOAD_EXTENDER_POSITION
        }),
        Intake({
            ClawExpansionConstants.MAX_EXTENDER_POSITION + maxExtenderAddition
        }),
        Intermediate({
            ClawExpansionConstants.INTERMEDIATE_EXTENDER_POSITION
        }),
        Deposit({
            ClawExpansionConstants.MIN_EXTENDER_POSITION
        })
    }

    private val extenderProfileConstraints = ProfileConstraints(0.01, 0.05, 0.05)
    private var motionProfile: AsymmetricMotionProfile? = null
    private var timer = ElapsedTime()

    var extenderState by Delegates.observable(ExtenderState.PreLoad) { _, _, new ->
        timer = ElapsedTime()
        motionProfile = AsymmetricMotionProfile(
            backingExtender.position,
            new.targetPosition(),
            extenderProfileConstraints
        )
    }

    private var rightClawState = ClawState.Closed
    private var leftClawState = ClawState.Closed

    override fun doInitialize()
    {
        maxExtenderAddition = 0.0
        clawRangeExpansion = 0.0
        backingExtender.position = ClawExpansionConstants
            .PRELOAD_EXTENDER_POSITION

        toggleExtender(ExtenderState.PreLoad)
        updateClawState(
            ClawStateUpdate.Both,
            ClawState.Closed
        )
    }

    fun extenderPeriodic()
    {
        if (motionProfile != null)
        {
            val diffs = abs(
                backingExtender.position - motionProfile!!.finalPosition
            )

            if (diffs < 0.01)
            {
                return
            }

            val position = motionProfile!!.calculate(timer.time())
            backingExtender.position = position.x
        }
    }

    fun incrementClawAddition()
    {
        clawRangeExpansion = (clawRangeExpansion + 0.025)
            .coerceIn(0.0, 0.2)
    }

    fun decrementClawAddition()
    {
        clawRangeExpansion = (clawRangeExpansion - 0.025)
            .coerceIn(0.0, 0.2)
    }

    fun incrementAddition()
    {
        maxExtenderAddition = (maxExtenderAddition - 0.01)
            .coerceIn(-0.1, 0.1)
        toggleExtender(extenderState)
    }

    fun decrementAddition()
    {
        maxExtenderAddition = (maxExtenderAddition + 0.01)
            .coerceIn(-0.1, 0.1)
        toggleExtender(extenderState)
    }

    @JvmOverloads
    fun toggleExtender(state: ExtenderState? = null)
    {
        if (state != null)
        {
            extenderState = state
            return
        }

        extenderState = when (extenderState)
        {
            ExtenderState.Deposit -> ExtenderState.Intake
            ExtenderState.Intermediate -> ExtenderState.Deposit
            else -> ExtenderState.Deposit
        }
    }

    enum class ClawStateUpdate(
        val clawType: () -> ClawType
    )
    {
        Left({ ClawType.Left }),
        Right({ ClawType.Right }),
        Both({
            throw IllegalStateException("tf u doing")
        })
    }

    fun updateClawState(effectiveOn: ClawStateUpdate, state: ClawState)
    {
        if (effectiveOn == ClawStateUpdate.Both)
        {
            updateClawState(ClawStateUpdate.Right, state)
            updateClawState(ClawStateUpdate.Left, state)
            return
        }

        val servo = effectiveOn.clawType().servo(this)
        val position = state.calculatePosition(effectiveOn.clawType())

        servo.position = if (effectiveOn == ClawStateUpdate.Left)
        {
            if (state == ClawState.Closed)
                position - clawRangeExpansion else
                position + clawRangeExpansion
        } else
        {
            if (state == ClawState.Closed)
                position + clawRangeExpansion else
                position - clawRangeExpansion
        }

        if (effectiveOn == ClawStateUpdate.Right)
        {
            this.rightClawState = state
        } else
        {
            this.leftClawState = state
        }
    }

    override fun isCompleted() = true
}
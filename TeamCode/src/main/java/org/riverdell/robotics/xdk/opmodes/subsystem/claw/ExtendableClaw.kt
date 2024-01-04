package org.riverdell.robotics.xdk.opmodes.subsystem.claw

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.xdk.opmodes.autonomous.hardware
import org.riverdell.robotics.xdk.opmodes.subsystem.motionprofile.wrappers.MotionProfiledServo
import org.riverdell.robotics.xdk.opmodes.subsystem.motionprofile.ProfileConstraints

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
        MotionProfiledServo(
            servo = opMode.hardware<Servo>("extender"),
            constraints = {
                ProfileConstraints(
                    ClawExpansionConstants.CLAW_MOTION_PROFILE_VELOCITY,
                    ClawExpansionConstants.CLAW_MOTION_PROFILE_ACCEL,
                    ClawExpansionConstants.CLAW_MOTION_PROFILE_DECEL
                )
            }
        )
    }

    private val clawFingerMPConstraints = {
        ProfileConstraints(
            ClawExpansionConstants.CLAW_FINGER_PROFILE_VELOCITY,
            ClawExpansionConstants.CLAW_FINGER_PROFILE_ACCEL,
            ClawExpansionConstants.CLAW_FINGER_PROFILE_DECEL
        )
    }

    val backingClawOpenerRight by lazy {
        MotionProfiledServo(
            servo = opMode.hardware<Servo>("clawRight"),
            constraints = clawFingerMPConstraints
        )
    }

    val backingClawOpenerLeft by lazy {
        MotionProfiledServo(
            servo = opMode.hardware<Servo>("clawLeft"),
            constraints = clawFingerMPConstraints
        )
    }

    override fun composeStageContext() = object : StageContext
    {
        override fun isCompleted() = true
    }

    enum class ClawType(val servo: (ExtendableClaw) -> MotionProfiledServo)
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

    var extenderState = ExtenderState.PreLoad

    private var rightClawState = ClawState.Closed
    private var leftClawState = ClawState.Closed

    override fun doInitialize()
    {
        maxExtenderAddition = 0.0
        clawRangeExpansion = 0.0

        toggleExtender(ExtenderState.PreLoad)
        updateClawState(
            ClawStateUpdate.Both,
            ClawState.Closed
        )
    }

    fun periodic()
    {
        backingExtender.runPeriodic()
        backingClawOpenerRight.runPeriodic()
        backingClawOpenerLeft.runPeriodic()
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
        extenderState = state
            ?: when (extenderState)
            {
                ExtenderState.Deposit -> ExtenderState.Intake
                ExtenderState.Intermediate -> ExtenderState.Deposit
                else -> ExtenderState.Deposit
            }

        backingExtender.setMotionProfileTarget(extenderState.targetPosition())
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

        if (effectiveOn == ClawStateUpdate.Right)
        {
            this.rightClawState = state
        } else
        {
            this.leftClawState = state
        }

        servo.setMotionProfileTarget(if (effectiveOn == ClawStateUpdate.Left)
        {
            if (state == ClawState.Closed)
                position - clawRangeExpansion else
                position + clawRangeExpansion
        } else
        {
            if (state == ClawState.Closed)
                position + clawRangeExpansion else
                position - clawRangeExpansion
        })
    }

    override fun isCompleted() = true
}
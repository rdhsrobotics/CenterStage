package org.robotics.robotics.xdk.teamcode.subsystem.claw

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.robotics.robotics.xdk.teamcode.autonomous.hardware
import org.robotics.robotics.xdk.teamcode.autonomous.scheduleAsyncExecution
import org.robotics.robotics.xdk.teamcode.subsystem.motionprofile.wrappers.MotionProfiledServo
import org.robotics.robotics.xdk.teamcode.subsystem.motionprofile.ProfileConstraints

class ExtendableClaw(private val opMode: LinearOpMode) : AbstractSubsystem()
{
    lateinit var backingExtender: MotionProfiledServo

    private val clawFingerMPConstraints = {
        ProfileConstraints(
            ClawExpansionConstants.CLAW_FINGER_PROFILE_VELOCITY,
            ClawExpansionConstants.CLAW_FINGER_PROFILE_ACCEL,
            ClawExpansionConstants.CLAW_FINGER_PROFILE_DECEL
        )
    }

    lateinit var backingClawOpenerRight: MotionProfiledServo
    lateinit var backingClawOpenerLeft: MotionProfiledServo

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
        MosaicFix({
            if (it == ClawType.Left)
                ClawExpansionConstants.OPEN_LEFT_CLAW + 0.2 else
                ClawExpansionConstants.OPEN_RIGHT_CLAW - 0.2
        }),
        Intake({
            if (it == ClawType.Left)
                ClawExpansionConstants.OPEN_LEFT_CLAW_INTAKE else
                ClawExpansionConstants.OPEN_RIGHT_CLAW_INTAKE
        }),
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
            ExtenderConstants.PRELOAD_EXTENDER_POSITION
        }),
        Intake({
            ExtenderConstants.MAX_EXTENDER_POSITION
        }),
        Intermediate({
            ExtenderConstants.INTERMEDIATE_EXTENDER_POSITION
        }),
        Deposit({
            ExtenderConstants.MIN_EXTENDER_POSITION
        })
    }

    var extenderState = ExtenderState.PreLoad

    private var rightClawState = ClawState.Closed
    private var leftClawState = ClawState.Closed

    override fun doInitialize()
    {
        backingExtender = MotionProfiledServo(
            servo = opMode.hardware<Servo>("extender"),
            constraints = {
                ProfileConstraints(
                    ClawExpansionConstants.CLAW_MOTION_PROFILE_VELOCITY,
                    ClawExpansionConstants.CLAW_MOTION_PROFILE_ACCEL,
                    ClawExpansionConstants.CLAW_MOTION_PROFILE_DECEL
                )
            }
        )

        backingClawOpenerRight = MotionProfiledServo(
            servo = opMode.hardware<Servo>("clawRight"),
            constraints = clawFingerMPConstraints
        )

        backingClawOpenerLeft = MotionProfiledServo(
            servo = opMode.hardware<Servo>("clawLeft"),
            constraints = clawFingerMPConstraints
        )

        scheduleAsyncExecution(1000L) {
            toggleExtender(
                ExtenderState.PreLoad,
                force = true
            )
        }

        updateClawState(
            ClawStateUpdate.Both,
            ClawState.Closed,
            force = true
        )
    }

    fun periodic()
    {
        backingExtender.runPeriodic()
        backingClawOpenerRight.runPeriodic()
        backingClawOpenerLeft.runPeriodic()
    }

    /*fun incrementAddition()
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
    }*/

    fun toggleExtender(state: ExtenderState? = null, force: Boolean = false)
    {
        val previousState = extenderState
        extenderState = state
            ?: when (previousState)
            {
                ExtenderState.Deposit -> ExtenderState.Intake
                ExtenderState.Intermediate -> ExtenderState.Deposit
                else -> ExtenderState.Deposit
            }

        if (force)
        {
            println("Setting extender target FORCE to ${extenderState.targetPosition()} -> ${extenderState.name}")
            backingExtender.setTarget(extenderState.targetPosition())
            return
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

    fun updateClawState(effectiveOn: ClawStateUpdate, state: ClawState, force: Boolean = false)
    {
        if (effectiveOn == ClawStateUpdate.Both)
        {
            updateClawState(ClawStateUpdate.Right, state, force)
            updateClawState(ClawStateUpdate.Left, state, force)
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

        servo.setTarget(position)

        /*if (force)
        {
            servo.setTarget(position)
            return
        }

        servo.setMotionProfileTarget(position)*/
    }

    override fun isCompleted() = true
}
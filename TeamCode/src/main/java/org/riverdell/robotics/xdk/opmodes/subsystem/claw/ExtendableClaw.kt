package org.riverdell.robotics.xdk.opmodes.subsystem.claw

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.xdk.opmodes.pipeline.hardware

class ExtendableClaw(private val opMode: LinearOpMode) : AbstractSubsystem()
{
    private val backingExtender by lazy {
        opMode.hardware<Servo>("extender")
    }

    val backingClawOpenerRight by lazy {
        opMode.hardware<Servo>("clawRight")
    }

    val backingClawOpenerLeft by lazy {
        opMode.hardware<Servo>("clawLeft")
    }

    private var maxExtenderAddition = 0.0
    private var clawRangeExpansion = 0.0

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

    enum class ExtenderState
    {
        PreLoad, Intake, Intermediate, Deposit
    }

    var extenderState = ExtenderState.PreLoad

    private var rightClawState = ClawState.Closed
    private var leftClawState = ClawState.Closed

    override fun doInitialize()
    {
        toggleExtender(ExtenderState.PreLoad)
        updateClawState(
            ClawStateUpdate.Both,
            ClawState.Closed
        )
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
            backingExtender.position = when (extenderState)
            {
                ExtenderState.PreLoad -> ClawExpansionConstants.PRELOAD_EXTENDER_POSITION
                ExtenderState.Deposit -> ClawExpansionConstants.MIN_EXTENDER_POSITION
                ExtenderState.Intermediate -> ClawExpansionConstants.INTERMEDIATE_EXTENDER_POSITION
                ExtenderState.Intake -> ClawExpansionConstants.MAX_EXTENDER_POSITION + maxExtenderAddition
            }

            return
        }

        extenderState = when (extenderState)
        {
            ExtenderState.Deposit ->
            {
                backingExtender.position = ClawExpansionConstants.MAX_EXTENDER_POSITION + maxExtenderAddition
                ExtenderState.Intake
            }
            else ->
            {
                backingExtender.position = ClawExpansionConstants.MIN_EXTENDER_POSITION
                ExtenderState.Deposit
            }
        }

        extenderState = if (extenderState == ExtenderState.Deposit)
        {
            backingExtender.position = ClawExpansionConstants.MAX_EXTENDER_POSITION + maxExtenderAddition
            ExtenderState.Intake
        } else if (extenderState == ExtenderState.Intermediate)
        {
            backingExtender.position = ClawExpansionConstants.INTERMEDIATE_EXTENDER_POSITION
            ExtenderState.Deposit
        } else
        {
            backingExtender.position = ClawExpansionConstants.MIN_EXTENDER_POSITION
            ExtenderState.Deposit
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
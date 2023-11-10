package org.riverdell.robotics.xdk.opmodes.subsystem.claw

import com.arcrobotics.ftclib.hardware.SimpleServo
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.Subsystem
import org.riverdell.robotics.xdk.opmodes.pipeline.hardware

class ExtendableClaw(private val opMode: LinearOpMode) : Subsystem
{
    private val backingExtender by lazy {
        opMode.hardware<Servo>("extender")
    }

    val backingClawOpener by lazy {
        opMode.hardware<Servo>("claw")
    }

    private var maxExtenderAddition = 0.0
    private var clawIncrement = 0.0

    override fun composeStageContext() = object : StageContext
    {
        override fun isCompleted() = true
    }

    enum class ClawState
    {
        Intake, Deposit, Start
    }

    private var clawState = ClawState.Start

    override fun initialize()
    {
        backingExtender.position = 0.0
        backingClawOpener.position = 0.5
    }

    fun incrementClawAddition()
    {
        clawIncrement = (clawIncrement + 0.025)
            .coerceIn(-0.2, 0.2)
    }

    fun decrementClawAddition()
    {
        clawIncrement = (clawIncrement - 0.025)
            .coerceIn(-0.2, 0.2)
    }

    fun incrementAddition()
    {
        maxExtenderAddition = (maxExtenderAddition - 0.01)
            .coerceIn(-0.1, 0.1)
        toggleExtender(clawState)
    }

    fun decrementAddition()
    {
        maxExtenderAddition = (maxExtenderAddition + 0.01)
            .coerceIn(-0.1, 0.1)
        toggleExtender(clawState)
    }

    @JvmOverloads
    fun toggleExtender(state: ClawState? = null)
    {
        if (state != null)
        {
            clawState = state
            backingExtender.position = when (clawState)
            {
                ClawState.Deposit -> ClawExpansionConstants.MIN_EXTENDER_POSITION
                ClawState.Start -> 0.0
                ClawState.Intake -> ClawExpansionConstants.MAX_EXTENDER_POSITION + maxExtenderAddition
            }

            return
        }

        clawState = if (clawState == ClawState.Deposit)
        {
            backingExtender.position = ClawExpansionConstants.MAX_EXTENDER_POSITION + maxExtenderAddition
            ClawState.Intake
        } else
        {
            backingExtender.position = ClawExpansionConstants.MIN_EXTENDER_POSITION
            ClawState.Deposit
        }
    }

    /**
     * Expand claw by the trigger amount. Accepts an expansion
     * range of [0.0, 1.0].
     */
    fun expandClaw(expandsTo: Double)
    {
        backingClawOpener.position = (0.5 + clawIncrement) -
            expandsTo * (0.5 - clawIncrement)
    }

    override fun isCompleted() = true
}
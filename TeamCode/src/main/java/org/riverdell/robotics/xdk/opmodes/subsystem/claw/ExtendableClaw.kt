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

    private val backingClawOpener by lazy {
        opMode.hardware<Servo>("claw")
    }

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
                ClawState.Intake -> ClawExpansionConstants.MAX_EXTENDER_POSITION
            }

            return
        }

        clawState = if (clawState == ClawState.Deposit)
        {
            backingExtender.position = ClawExpansionConstants.MAX_EXTENDER_POSITION
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
        backingClawOpener.position = 0.5 - expandsTo * 0.5
    }

    override fun isCompleted() = true
}
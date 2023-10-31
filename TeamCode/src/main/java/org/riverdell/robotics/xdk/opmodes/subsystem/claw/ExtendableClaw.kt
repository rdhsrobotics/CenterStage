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
        SimpleServo(
            opMode.hardwareMap,
            "extender",
            0.0, 90.0
        )
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
        Intake, Deposit
    }

    private var clawState = ClawState.Deposit

    override fun initialize()
    {
        backingExtender.turnToAngle(0.0)
        backingClawOpener.position = 0.0
    }

    @JvmOverloads
    fun toggleExtender(state: ClawState? = null)
    {
        if (state != null)
        {
            if (clawState == state)
            {
                return
            }

            clawState = state
            backingExtender.turnToAngle(
                if (clawState == ClawState.Intake) 0.0 else 21.75
            )
            return
        }

        clawState = when (clawState)
        {
            ClawState.Deposit ->
            {
                backingExtender.turnToAngle(21.75)
                ClawState.Intake
            }

            ClawState.Intake ->
            {
                backingExtender.turnToAngle(0.0)
                ClawState.Deposit
            }
        }
    }

    /**
     * Expand claw by the trigger amount. Accepts an expansion
     * range of [0.0, 1.0].
     */
    fun expandClaw(expandsTo: Double)
    {
        backingClawOpener.position = expandsTo * ClawExpansionConstants.MAX_CLAW_POSITION
    }

    override fun isCompleted() = true
}
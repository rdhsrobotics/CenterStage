package org.robotics.robotics.xdk.teamcode.subsystem.claw

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import io.liftgate.robotics.mono.subsystem.Subsystem
import io.liftgate.robotics.mono.subsystem.System

@TeleOp(name = "Test | Claw Test")
class ClawTestOpMode : LinearOpMode(), System
{
    override val subsystems = mutableSetOf<Subsystem>()

    override fun runOpMode()
    {
        val extendableClaw = ExtendableClaw(this)
        register(extendableClaw)
        initializeAll()

        waitForStart()

        var alternate = false
        while (!isStopRequested)
        {
            alternate = if (alternate)
            {
                extendableClaw.updateClawState(ExtendableClaw.ClawStateUpdate.Both, ExtendableClaw.ClawState.Intake)
                false
            } else
            {
                extendableClaw.updateClawState(ExtendableClaw.ClawStateUpdate.Both, ExtendableClaw.ClawState.Closed)
                true
            }
            Thread.sleep(500L)
        }
    }
}
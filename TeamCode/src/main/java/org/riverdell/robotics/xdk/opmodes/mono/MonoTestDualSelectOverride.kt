package org.riverdell.robotics.xdk.opmodes.mono

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.gamepad.ButtonType

@Disabled
@TeleOp(name = "Mono | Test Select Override")
class MonoTestDualSelectOverride : LinearOpMode()
{
    override fun runOpMode()
    {
        val gp1Commands = Mono.commands(gamepad1)

        gp1Commands
            .where(ButtonType.ButtonA)
            .triggers {
                telemetry.addLine("Hey!")
                telemetry.update()
            }
            .whenPressedOnce()

        telemetry.addLine("Waiting for start")

        waitForStart()
        gp1Commands.initialize()
        telemetry.addLine("Listening for commands")

        while (opModeIsActive())
        {
            Thread.sleep(100L)
        }

        gp1Commands.dispose()
    }
}
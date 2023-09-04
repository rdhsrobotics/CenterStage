package org.riverdell.robotics.opmode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import io.liftgate.ftc.scripting.KotlinScript.ImpliedVariable
import io.liftgate.ftc.scripting.opmode.DevLinearOpMode
import org.riverdell.robotics.gamepad.GamepadCommands

@TeleOp(
    name = "kts - Dev Test",
    group = "scripted"
)
class DevTeleOp : DevLinearOpMode()
{
    private val gp1Commands by lazy { GamepadCommands(gamepad1) }
    private val gp2Commands by lazy { GamepadCommands(gamepad2) }

    override fun runOpMode()
    {
        // TODO: add hooks to ProdOpMode for prestart conf stuff, etc
        listOf(gp1Commands, gp2Commands)
            .forEach(GamepadCommands::startListening)

        super.runOpMode()
    }

    override fun getImpliedVariables() = mutableListOf(
        ImpliedVariable.of("gp1Commands", gp1Commands),
        ImpliedVariable.of("gp2Commands", gp2Commands)
    )
    override fun getScriptName() = "Dev.kts"
}

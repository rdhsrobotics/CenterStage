package org.riverdell.robotics.opmode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import io.liftgate.ftc.scripting.KotlinScript.ImpliedVariable
import io.liftgate.ftc.scripting.opmode.DevLinearOpMode
import org.riverdell.robotics.gamepad.ButtonType
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

        fun toggleLiftMotorSlowMode() = Unit

        gp1Commands
            .where(ButtonType.ButtonA)
            .triggers(::toggleLiftMotorSlowMode) // toggle each time the user presses A once
            .whenPressedOnce()

        fun toggleHoldingOfLiftMotor() = Unit

        gp1Commands
            .where(ButtonType.ButtonX)
            .triggers(::toggleHoldingOfLiftMotor) // toggle lift motor hold when user starts holding X
            // maintain lift motor hold
            .andIsMaintainedUntilReleasedWhere(::toggleHoldingOfLiftMotor) // when user stops holding X, disable lift hold

        fun incrementSomething() = Unit

        gp1Commands
            .where(ButtonType.ButtonY)
            .and(ButtonType.ButtonB)
            .triggers(::incrementSomething) // continue to call this ever 50ms while the user is holding both B and Y
            .whileItIsBeingPressed()

        super.runOpMode()
    }

    override fun getImpliedVariables() = mutableListOf(
        ImpliedVariable.of("gp1Commands", gp1Commands),
        ImpliedVariable.of("gp2Commands", gp2Commands)
    )
    override fun getScriptName() = "Dev.kts"
}

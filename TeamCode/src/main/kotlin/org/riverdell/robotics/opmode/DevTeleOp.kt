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

    fun configureCommands()
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
            .andIsHeldUntilReleasedWhere(::toggleHoldingOfLiftMotor) // when user stops holding X, disable lift hold

        fun bringUpLiftMotor() = Unit

        gp1Commands
            .where(ButtonType.ButtonY)
            .and(ButtonType.ButtonB)
            .triggers(::bringUpLiftMotor, 250L) // continue to increment lift motor position while the user is holding both B and Y every 250ms
            .repeatedlyWhilePressed()

        fun resetLiftMotorPosition() = Unit
        fun inEndGame(): Boolean = false

        // Similar example to the one right above this, however, this time we are able to
        // reset the lift motor back to position 0 when the user stops pressing
        gp1Commands
            .where(ButtonType.ButtonY)
            .and(ButtonType.ButtonB) // can either use B + Y
            .or {
                and(ButtonType.DPadUp)
                    .and(ButtonType.DPadLeft)
            } // or they can use DPUp + DPLeft
            .onlyWhen(::inEndGame) // only allow this command when in end game
            .triggers(::bringUpLiftMotor, 500L) // continue to call this ever 50ms while the user is holding both B and Y every 500ms
            .repeatedlyWhilePressedUntilReleasedWhere(::resetLiftMotorPosition)
    }

    override fun runOpMode()
    {
        configureCommands()
        super.runOpMode()
    }

    override fun getImpliedVariables() = mutableListOf(
        ImpliedVariable.of("gp1Commands", gp1Commands),
        ImpliedVariable.of("gp2Commands", gp2Commands)
    )
    override fun getScriptName() = "Dev.kts"
}

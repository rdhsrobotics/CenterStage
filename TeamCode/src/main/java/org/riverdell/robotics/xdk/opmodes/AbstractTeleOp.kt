package org.riverdell.robotics.xdk.opmodes

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.Mono.commands
import io.liftgate.robotics.mono.gamepad.ButtonType
import io.liftgate.robotics.mono.subsystem.Subsystem
import io.liftgate.robotics.mono.subsystem.System
import org.riverdell.robotics.xdk.opmodes.subsystem.AirplaneLauncher
import org.riverdell.robotics.xdk.opmodes.subsystem.Drivebase
import org.riverdell.robotics.xdk.opmodes.subsystem.Elevator
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ExtendableClaw

/**
 * Configures Mono gamepad commands and FTCLib
 * drive systems for TeleOp.
 *
 * @author Subham
 * @since 9/5/2023
 */
abstract class AbstractTeleOp : LinearOpMode(), System
{
    override val subsystems: MutableSet<Subsystem> = mutableSetOf()

    private val gp1Commands by lazy { commands(gamepad1) }
    private val gp2Commands by lazy { commands(gamepad2) }

    private val drivebase by lazy { Drivebase(this) }
    private val paperPlaneLauncher by lazy { AirplaneLauncher(this) }
    private val elevator by lazy { Elevator(this) }
    private val extendableClaw by lazy { ExtendableClaw(this) }

    abstract fun driveRobot(
        drivebase: Drivebase,
        driverOp: GamepadEx,
        multiplier: Double
    )

    override fun runOpMode()
    {
        register(
            drivebase, paperPlaneLauncher, elevator,
            extendableClaw, gp1Commands, gp2Commands
        )

        val driverOp = GamepadEx(gamepad1)
        buildCommands()

        telemetry.addLine("Configured commands. Waiting for start...")
        telemetry.update()

        initializeAll()
        waitForStart()

        extendableClaw.toggleExtender(
            ExtendableClaw.ClawState.Deposit
        )

        while (opModeIsActive())
        {
            val multiplier = 0.6 + gamepad1.right_trigger * 0.4
            driveRobot(drivebase, driverOp, multiplier)

            extendableClaw.expandClaw(gamepad2.left_trigger.toDouble())
            elevator.configureElevator(gamepad2.right_stick_y.toDouble())
        }

        disposeOfAll()
    }

    private fun buildCommands()
    {
        gp1Commands
            .where(ButtonType.ButtonY)
            .triggers {
                paperPlaneLauncher.launch()
            }
            .andIsHeldUntilReleasedWhere {
                paperPlaneLauncher.reset()
            }
        gp2Commands
            .where(ButtonType.ButtonY)
            .triggers {
                extendableClaw.toggleExtender()
            }
            .whenPressedOnce()

        // extender expansion ranges
        gp2Commands
            .where(ButtonType.DPadDown)
            .triggers {
                extendableClaw.decrementAddition()
            }
            .whenPressedOnce()
        gp2Commands
            .where(ButtonType.DPadUp)
            .triggers {
                extendableClaw.incrementAddition()
            }
            .whenPressedOnce()

        // claw expansion
        gp2Commands
            .where(ButtonType.DPadLeft)
            .triggers {
                extendableClaw.decrementClawAddition()
            }
            .whenPressedOnce()

        gp2Commands
            .where(ButtonType.DPadRight)
            .triggers {
                extendableClaw.incrementClawAddition()
            }
            .whenPressedOnce()
    }
}
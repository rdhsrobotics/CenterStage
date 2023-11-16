package org.riverdell.robotics.xdk.opmodes

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Gamepad.LedEffect
import io.liftgate.robotics.mono.Mono.commands
import io.liftgate.robotics.mono.gamepad.ButtonType
import io.liftgate.robotics.mono.gamepad.GamepadCommands
import io.liftgate.robotics.mono.subsystem.Subsystem
import io.liftgate.robotics.mono.subsystem.System
import org.riverdell.robotics.xdk.opmodes.pipeline.scheduleAsyncExecution
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
            ExtendableClaw.ExtenderState.Deposit
        )

        while (opModeIsActive())
        {
            val multiplier = 0.6 + gamepad1.right_trigger * 0.4
            driveRobot(drivebase, driverOp, multiplier)
            elevator.configureElevator(gamepad2.right_stick_y.toDouble())

            if (extendableClaw.extenderState == ExtendableClaw.ExtenderState.Intake)
            {
                // physical feedback towards the driver when driving with the extender down
                if (gamepad1.nextRumbleApproxFinishTime > 0)
                {
                    continue
                }

                gamepad1.rumble(1000)
            }
        }

        disposeOfAll()
    }

    private fun buildCommands()
    {
        gp1Commands
            .where(ButtonType.PlayStationTouchpad)
            .onlyWhen { 
                gamepad1.touchpad_finger_1_y <= 0.0 &&
                gamepad1.touchpad_finger_2_y >= 0.0 
            }
            .triggers {
                paperPlaneLauncher.launch()
            }
            .andIsHeldUntilReleasedWhere {
                paperPlaneLauncher.reset()
            }

        // extender expansion ranges
        gp1Commands
            .where(ButtonType.PlayStationTouchpad)
            .onlyWhen { gamepad1.touchpad_finger_1_y <= 0.0 }
            .triggers {
                extendableClaw.decrementAddition()
            }
            .whenPressedOnce()

        gp1Commands
            .where(ButtonType.PlayStationTouchpad)
            .onlyWhen { gamepad1.touchpad_finger_1_y >= 0.0 }
            .triggers {
                extendableClaw.incrementAddition()
            }
            .whenPressedOnce()

        var bundleExecutionInProgress = false

        // claw expansion ranges
        /*gp1Commands
            .where(ButtonType.PlayStationTouchpad)
            .onlyWhenNot { bundleExecutionInProgress }
            .onlyWhen { gamepad1.touchpad_finger_1_x <= 0.0 }
            .triggers {
                extendableClaw.decrementClawAddition()
                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Both,
                    ExtendableClaw.ClawState.Closed
                )
            }
            .whenPressedOnce()

        gp1Commands
            .where(ButtonType.PlayStationTouchpad)
            .onlyWhenNot { bundleExecutionInProgress }
            .onlyWhen { gamepad1.touchpad_finger_1_x >= 0.0 }
            .triggers {
                extendableClaw.incrementClawAddition()
                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Both,
                    ExtendableClaw.ClawState.Closed
                )
            }
            .whenPressedOnce()*/

        // bumper commands for opening closing claw fingers individually
        gp2Commands
            .where(ButtonType.BumperLeft)
            .onlyWhen { !bundleExecutionInProgress }
            .triggers {
                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Left,
                    ExtendableClaw.ClawState.Open
                )
            }
            .andIsHeldUntilReleasedWhere {
                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Left,
                    ExtendableClaw.ClawState.Closed
                )
            }

        gp2Commands
            .where(ButtonType.BumperRight)
            .onlyWhen { !bundleExecutionInProgress }
            .triggers {
                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Right,
                    ExtendableClaw.ClawState.Open
                )
            }
            .andIsHeldUntilReleasedWhere {
                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Right,
                    ExtendableClaw.ClawState.Closed
                )
            }

        // elevator preset return to home
        gp2Commands
            .where(ButtonType.ButtonB)
            .triggers {
                elevator.configureElevatorManually(0.0)
            }
            .whenPressedOnce()

        // extender to intake
        gp2Commands
            .where(ButtonType.ButtonY)
            .onlyWhen { !bundleExecutionInProgress }
            .triggers {
                extendableClaw.toggleExtender(
                    ExtendableClaw.ExtenderState.Intake
                )
            }
            .repeatedlyWhilePressedUntilReleasedWhere {
                extendableClaw.toggleExtender(
                    ExtendableClaw.ExtenderState.Deposit
                )
            }

        fun GamepadCommands.ButtonMappingBuilder.depositPresetReleaseOnElevatorHeight(position: Double)
        {
            triggers {
                bundleExecutionInProgress = true
                elevator.configureElevatorManually(position)
            }.andIsHeldUntilReleasedWhere {
                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Both,
                    ExtendableClaw.ClawState.Open
                )

                scheduleAsyncExecution(250L) {
                    elevator.configureElevatorManually(0.0)
                    bundleExecutionInProgress = false
                }
            }
        }

        gp2Commands
            .where(ButtonType.DPadLeft)
            .onlyWhenNot { bundleExecutionInProgress }
            .depositPresetReleaseOnElevatorHeight(0.5)

        gp2Commands
            .where(ButtonType.DPadUp)
            .onlyWhenNot { bundleExecutionInProgress }
            .depositPresetReleaseOnElevatorHeight(0.75)

        gp2Commands
            .where(ButtonType.DPadRight)
            .onlyWhenNot { bundleExecutionInProgress }
            .depositPresetReleaseOnElevatorHeight(1.0)

        // human station pickup preset
        gp2Commands
            .where(ButtonType.ButtonA)
            .onlyWhenNot { bundleExecutionInProgress }
            .triggers {
                bundleExecutionInProgress = true
                extendableClaw.toggleExtender(
                    ExtendableClaw.ExtenderState.Intake
                )
                
                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Both,
                    ExtendableClaw.ClawState.Open
                )
            }
            .andIsHeldUntilReleasedWhere {
                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Both,
                    ExtendableClaw.ClawState.Closed
                )

                scheduleAsyncExecution(450L) {
                    extendableClaw.toggleExtender(
                        ExtendableClaw.ExtenderState.Deposit
                    )

                    bundleExecutionInProgress = false
                }
            }
    }
}

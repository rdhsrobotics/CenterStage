package org.riverdell.robotics.xdk.opmodes

import com.acmerobotics.dashboard.FtcDashboard
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.IMU
import io.liftgate.robotics.mono.Mono.commands
import io.liftgate.robotics.mono.gamepad.ButtonType
import io.liftgate.robotics.mono.gamepad.GamepadCommands
import io.liftgate.robotics.mono.subsystem.Subsystem
import io.liftgate.robotics.mono.subsystem.System
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.riverdell.robotics.xdk.opmodes.autonomous.hardware
import org.riverdell.robotics.xdk.opmodes.autonomous.scheduleAsyncExecution
import org.riverdell.robotics.xdk.opmodes.subsystem.AirplaneLauncher
import org.riverdell.robotics.xdk.opmodes.subsystem.Drivebase
import org.riverdell.robotics.xdk.opmodes.subsystem.Elevator
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ExtendableClaw
import kotlin.math.abs

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

        telemetry.addLine("Configured all subsystems. Waiting for start...")

        if (
            gamepad1.type != Gamepad.Type.SONY_PS4 &&
            gamepad1.type != Gamepad.Type.SONY_PS4_SUPPORTED_BY_KERNEL
        )
        {
            telemetry.addLine("WARNING! We require a Sony PS4 controller to be used as GAMEPAD 1. Please fix this to ensure everything works as intended!")
        }

        telemetry.update()
        gp1Commands.doButtonUpdatesManually()
        gp2Commands.doButtonUpdatesManually()

        initializeAll()
        waitForStart()

        telemetry.addLine("Initialized all subsystems. We're ready to go!")
        telemetry.update()

        extendableClaw.toggleExtender(
            ExtendableClaw.ExtenderState.Deposit
        )

        val imu = hardware<IMU>("imu")
        imu.initialize(
            IMU.Parameters(
                RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD,
                    RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
                )
            )
        )
        imu.resetYaw()

        while (opModeIsActive())
        {
            val multiplier = 0.6 + gamepad1.right_trigger * 0.4
            driveRobot(drivebase, driverOp, multiplier)

            gp1Commands.run()
            gp2Commands.run()

            if (!bundleExecutionInProgress)
            {
                elevator.configureElevator(gamepad2.right_stick_y.toDouble())
            }

            telemetry.addData("Current", elevator.backingMotor.currentPosition)
            telemetry.addData("Target", elevator.backingMotor.targetPosition)
            telemetry.addData("Diff", elevator.backingMotor.targetPosition - elevator.backingMotor.currentPosition)
            telemetry.update()
            extendableClaw.periodic()

            if (extendableClaw.extenderState == ExtendableClaw.ExtenderState.Intake)
            {
                // physical feedback towards the driver when driving with the extender down
                if (gamepad1.nextRumbleApproxFinishTime > java.lang.System.currentTimeMillis())
                {
                    continue
                }

                gamepad1.rumble(250)
            }
        }

        disposeOfAll()
    }

    private var bundleExecutionInProgress = false

    private fun buildCommands()
    {
        gp1Commands
            .where(ButtonType.PlayStationSquare)
            .triggers {
                paperPlaneLauncher.launch()
            }
            .andIsHeldUntilReleasedWhere {
                paperPlaneLauncher.reset()
            }

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

        /* // extender expansion ranges


         // claw expansion ranges
         gp1Commands
             .where(ButtonType.PlayStationTouchpad)
             .onlyWhenNot { bundleExecutionInProgress }
             .onlyWhen { !gamepad1.touchpad_finger_2 && gamepad1.touchpad_finger_1_x <= 0.0 }
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
             .onlyWhen { !gamepad1.touchpad_finger_2 && gamepad1.touchpad_finger_1_x >= 0.0 }
             .triggers {
                 extendableClaw.incrementClawAddition()
                 extendableClaw.updateClawState(
                     ExtendableClaw.ClawStateUpdate.Both,
                     ExtendableClaw.ClawState.Closed
                 )
             }
             .whenPressedOnce()*/

        // going underneath stage door. drive should be able to take over:
        gp1Commands
            .where(ButtonType.BumperRight)
            .triggers {
                extendableClaw.toggleExtender(
                    ExtendableClaw.ExtenderState.Intermediate
                )
            }
            .andIsHeldUntilReleasedWhere {
                extendableClaw.toggleExtender(
                    ExtendableClaw.ExtenderState.Deposit
                )
            }

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

        // lift motor toggles
        gp1Commands
            .where(ButtonType.PlayStationShare)
            .onlyWhen { gamepad1.left_bumper }
            .triggers {
                elevator.toggleHangLift(-1.0)
            }
            .andIsHeldUntilReleasedWhere {
                elevator.toggleHangLift(0.0)
            }

        gp1Commands
            .where(ButtonType.PlayStationOptions)
            .onlyWhen { gamepad1.left_bumper }
            .triggers {
                elevator.toggleHangLift(1.0)
            }
            .andIsHeldUntilReleasedWhere {
                elevator.toggleHangLift(0.0)
            }

        // elevator preset (low backboard)
        gp2Commands
            .where(ButtonType.ButtonB)
            .triggers {
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
            }

        // extender to intake
        gp2Commands
            .where(ButtonType.ButtonY)
            .onlyWhen { !bundleExecutionInProgress }
            .triggers {
                extendableClaw.toggleExtender(
                    ExtendableClaw.ExtenderState.Intake
                )
            }
            .andIsHeldUntilReleasedWhere {
                extendableClaw.toggleExtender(
                    ExtendableClaw.ExtenderState.Deposit
                )
            }

        gp2Commands
            .where(ButtonType.ButtonA)
            .triggers {
                if (bundleExecutionInProgress)
                {
                    return@triggers
                }

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
                if (!bundleExecutionInProgress)
                {
                    return@andIsHeldUntilReleasedWhere
                }

                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Both,
                    ExtendableClaw.ClawState.Closed
                )

                scheduleAsyncExecution(250L) {
                    extendableClaw.toggleExtender(
                        ExtendableClaw.ExtenderState.Deposit
                    )
                    bundleExecutionInProgress = false
                }
            }

        fun GamepadCommands.ButtonMappingBuilder.depositPresetReleaseOnElevatorHeight(position: Int)
        {

                triggers {
                    if (bundleExecutionInProgress)
                    {
                        return@triggers
                    }

                    bundleExecutionInProgress = true
                    elevator.configureElevatorManuallyRaw(position)
                }
                .andIsHeldUntilReleasedWhere {
                    if (!bundleExecutionInProgress)
                    {
                        return@andIsHeldUntilReleasedWhere
                    }

                    // premature release
                    if (abs(elevator.backingMotor.currentPosition - position) > 35)
                    {
                        scheduleAsyncExecution(50L) {
                            elevator.configureElevatorManuallyRaw(0)

                            Thread.sleep(1000L)
                            bundleExecutionInProgress = false
                        }
                        return@andIsHeldUntilReleasedWhere
                    }

                    val applyUpdatesTo = when (true)
                    {
                        gamepad2.left_bumper -> ExtendableClaw.ClawStateUpdate.Left
                        gamepad2.right_bumper -> ExtendableClaw.ClawStateUpdate.Right
                        (gamepad2.right_bumper && gamepad2.left_bumper) -> ExtendableClaw.ClawStateUpdate.Both
                        else -> ExtendableClaw.ClawStateUpdate.Both
                    }

                    if (gamepad2.right_trigger <= 0.5)
                    {
                        extendableClaw.updateClawState(
                            applyUpdatesTo,
                            ExtendableClaw.ClawState.Open
                        )
                    }

                    scheduleAsyncExecution(350L) {
                        elevator.configureElevatorManuallyRaw(0)

                        extendableClaw.updateClawState(
                            applyUpdatesTo,
                            ExtendableClaw.ClawState.Closed
                        )

                        Thread.sleep(1000L)
                        bundleExecutionInProgress = false
                    }
                }
        }

        gp2Commands
            .where(ButtonType.DPadLeft)
            .depositPresetReleaseOnElevatorHeight(-630)

        gp2Commands
            .where(ButtonType.DPadUp)
            .depositPresetReleaseOnElevatorHeight(-850)

        gp2Commands
            .where(ButtonType.DPadRight)
            .depositPresetReleaseOnElevatorHeight(-1130)

        gp2Commands
            .where(ButtonType.DPadDown)
            .onlyWhen { !bundleExecutionInProgress }
            .triggers {
                elevator.configureElevatorManually(0.0)
            }
            .whenPressedOnce()
    }
}
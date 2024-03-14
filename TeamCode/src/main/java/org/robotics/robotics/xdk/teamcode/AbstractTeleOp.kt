package org.robotics.robotics.xdk.teamcode

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.Mono.commands
import io.liftgate.robotics.mono.gamepad.ButtonType
import io.liftgate.robotics.mono.gamepad.GamepadCommands
import io.liftgate.robotics.mono.subsystem.Subsystem
import io.liftgate.robotics.mono.subsystem.System
import org.robotics.robotics.xdk.teamcode.autonomous.scheduleAsyncExecution
import org.robotics.robotics.xdk.teamcode.subsystem.drone.DroneLauncher
import org.robotics.robotics.xdk.teamcode.subsystem.Drivebase
import org.robotics.robotics.xdk.teamcode.subsystem.Elevator
import org.robotics.robotics.xdk.teamcode.subsystem.MathUtils
import org.robotics.robotics.xdk.teamcode.subsystem.claw.ExtendableClaw
import org.robotics.robotics.xdk.teamcode.subsystem.hang.Hang
import kotlin.math.abs

/**
 * A base implementation of a TeleOp. Contains lifecycles for
 * all subsystems and Mono gamepad command implementations.
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
    private val paperPlaneLauncher by lazy { DroneLauncher(this) }
    private val elevator by lazy { Elevator(this) }
    private val extendableClaw by lazy { ExtendableClaw(this) }
    private val hang by lazy { Hang(this) }

    abstract fun driveRobot(
        drivebase: Drivebase,
        driverOp: GamepadEx,
        multiplier: Double
    )

    override fun runOpMode()
    {
        register(
            drivebase, paperPlaneLauncher, elevator,
            extendableClaw, hang,

            gp1Commands, gp2Commands
        )

        val driverOp = GamepadEx(gamepad1)
        buildCommands()

        telemetry.addLine("Configured all subsystems. Waiting for start...")
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

        while (opModeIsActive())
        {
            /*
            val multiplier = MathUtils.INSTANCE.joystickScalar(
                gamepad1.right_trigger.toDouble(), 0.01
            )
             */
            val multiplier = 0.5 + gamepad1.right_trigger * 0.5
                /*MathUtils.INSTANCE.joystickScalar(
                    gamepad1.right_trigger.toDouble(), 0.01
                )*/

            driveRobot(drivebase, driverOp, multiplier)

            gp1Commands.run()
            gp2Commands.run()

            if (!bundleExecutionInProgress)
            {
                elevator.configureElevator(gamepad2.right_stick_y.toDouble())
            }

            extendableClaw.periodic()

            if (extendableClaw.extenderState == ExtendableClaw.ExtenderState.Intake)
            {
                // physical feedback towards the driver when driving with the extender down
                if (gamepad1.nextRumbleApproxFinishTime > java.lang.System.currentTimeMillis())
                {
                    continue
                }

                gamepad1.rumble(50)
            }
        }

        disposeOfAll()
    }

    private var bundleExecutionInProgress = false

    /**
     * Maps actions to gamepad buttons.
     */
    private fun buildCommands()
    {
        gp1Commands
            .where(ButtonType.BumperLeft)
            .triggers {
                if (paperPlaneLauncher.state == DroneLauncher.DroneLauncherState.Launched)
                {
                    paperPlaneLauncher.reset()
                    return@triggers
                }

                paperPlaneLauncher.launch()
            }
            .whenPressedOnce()

        gp2Commands
            .where(ButtonType.DPadDown)
            .triggers {
                extendableClaw.toggleExtender(
                    ExtendableClaw.ExtenderState.Intermediate
                )

                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Both,
                    ExtendableClaw.ClawState.MosaicFix
                )
            }
            .andIsHeldUntilReleasedWhere {
                extendableClaw.toggleExtender(
                    ExtendableClaw.ExtenderState.Deposit
                )

                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Both,
                    ExtendableClaw.ClawState.Closed
                )
            }

        /* // extender expansion ranges
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

        // bumper commands for opening closing claw fingers individually
        gp2Commands
            .where(ButtonType.BumperLeft)
            .onlyWhen { !bundleExecutionInProgress }
            .triggers {
                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Left,
                    if (gamepad2.left_trigger > 0.5) ExtendableClaw.ClawState.Intake else ExtendableClaw.ClawState.Open
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
                    if (gamepad2.right_trigger > 0.5) ExtendableClaw.ClawState.Intake else ExtendableClaw.ClawState.Open
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
            .where(ButtonType.PlayStationTriangle)
            .triggers {
                if (hang.hangState == Hang.PassiveHangState.Deployed)
                {
                    hang.brake()
                    return@triggers
                }

                hang.deploy()
            }
            .whenPressedOnce()

        gp1Commands
            .where(ButtonType.PlayStationCross)
            .triggers {
                if (hang.hangState == Hang.PassiveHangState.Armed)
                {
                    hang.brake()
                    return@triggers
                }

                hang.arm()
                extendableClaw.toggleExtender(ExtendableClaw.ExtenderState.PreLoad)
            }
            .whenPressedOnce()

        gp1Commands
            .where(ButtonType.PlayStationLogo)
            .triggers {
                hang.brake()
            }
            .whenPressedOnce()

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
                    ExtendableClaw.ClawState.Intake
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
            }.andIsHeldUntilReleasedWhere {
                if (!bundleExecutionInProgress)
                {
                    return@andIsHeldUntilReleasedWhere
                }

                // premature release
                if (abs(elevator.getCurrentElevatorPosition() - position) > 35)
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
    }
}
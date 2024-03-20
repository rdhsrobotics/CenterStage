package org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.v2

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.pipeline.consecutive
import io.liftgate.robotics.mono.pipeline.simultaneous
import io.liftgate.robotics.mono.pipeline.single
import io.liftgate.robotics.mono.pipeline.waitMillis
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Point
import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Pose
import org.robotics.robotics.xdk.teamcode.autonomous.profiles.AutonomousProfile
import org.robotics.robotics.xdk.teamcode.autonomous.position.PositionCommand
import org.robotics.robotics.xdk.teamcode.autonomous.position.degrees
import org.robotics.robotics.xdk.teamcode.autonomous.position.navigateTo
import org.robotics.robotics.xdk.teamcode.autonomous.position.purePursuitNavigateTo
import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.Waypoint
import org.robotics.robotics.xdk.teamcode.autonomous.shared.GlobalConstants
import org.robotics.robotics.xdk.teamcode.subsystem.claw.ExtendableClaw
import kotlin.math.PI

@Autonomous(name = "Test | 2+0 V2", group = "Test")
class V2ControlTestAutoPositionCommand : AbstractAutoPipeline(
    AutonomousProfile.RedPlayer1TwoPlusZero,
    blockExecutionGroup = { opMode, _ ->
        simultaneous("pixel drop") {
            consecutive("pos then deposit") {
                single("Pixel Deposit") {
                    navigateTo(
                        Pose(0.0, 30.0, 0.degrees)
                    )
                }

                single("deposit") {
                    opMode.clawSubsystem.updateClawState(
                        ExtendableClaw.ClawStateUpdate.Right,
                        ExtendableClaw.ClawState.Open
                    )
                }
            }

            single("extender down") {
                opMode.clawSubsystem.toggleExtender(
                    ExtendableClaw.ExtenderState.Intake
                )
            }
        }

        simultaneous("extender back") {
            waitMillis(450L)
            single("extender back") {
                opMode.clawSubsystem.toggleExtender(ExtendableClaw.ExtenderState.Deposit)
            }
        }

        simultaneous("move into deposit yellow pixel") {
            single("Backboard deposit") {
                navigateTo(
                    Pose(-40.0, -35.0, (-90).degrees)
                )
            }

            single("elevator up") {
                opMode.elevatorSubsystem.configureElevatorManually(
                    GlobalConstants.ScalarExpectedElevatorDropHeight
                )
            }
        }

        simultaneous("wait for yellow pixel to drop") {
            waitMillis(500L)
            single("deposit yellow pixel") {
                // open the claw and wait for the pixel to drop
                opMode.clawSubsystem.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Left,
                    ExtendableClaw.ClawState.Open,
                    force = true
                )

                opMode.elevatorSubsystem.configureElevatorManually(
                    GlobalConstants.ScalarExpectedElevatorDropHeight + 0.1
                )
            }
        }

        simultaneous("retract and paark") {
            consecutive("wait before doing thing") {
                waitMillis(750L)
                single("retract") {
                    // open the claw and wait for the pixel to drop
                    opMode.clawSubsystem.toggleExtender(
                        ExtendableClaw.ExtenderState.PreLoad,
                        force = true
                    )
                    opMode.clawSubsystem.updateClawState(
                        ExtendableClaw.ClawStateUpdate.Left,
                        ExtendableClaw.ClawState.Closed,
                        force = true
                    )

                    opMode.elevatorSubsystem.configureElevatorManually(0.0)
                }
            }

            single("bla bla bla") {
                navigateTo(
                    Pose(-37.0, -50.0, (-90).degrees)
                )
            }
        }
    }
)
package org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.v3

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.pipeline.consecutive
import io.liftgate.robotics.mono.pipeline.simultaneous
import io.liftgate.robotics.mono.pipeline.single
import io.liftgate.robotics.mono.pipeline.waitMillis
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TapeSide
import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Pose
import org.robotics.robotics.xdk.teamcode.autonomous.profiles.AutonomousProfile
import org.robotics.robotics.xdk.teamcode.autonomous.position.degrees
import org.robotics.robotics.xdk.teamcode.autonomous.position.navigateTo
import org.robotics.robotics.xdk.teamcode.autonomous.position.purePursuitNavigateTo
import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.Waypoint
import org.robotics.robotics.xdk.teamcode.autonomous.shared.GlobalConstants
import org.robotics.robotics.xdk.teamcode.subsystem.claw.ExtendableClaw

@Autonomous(name = "Test | 2+0 V2", group = "Test")
class V3TwoPlusZeroTest : AbstractAutoPipeline(
    AutonomousProfile.RedPlayer1TwoPlusZero,
    blockExecutionGroup = { opMode, kms ->
        single("Pixel Deposit") {
            opMode.clawSubsystem.toggleExtender(
                ExtendableClaw.ExtenderState.Intake,
                force = true
            )


            if (kms != TapeSide.Middle)
            {
                if (kms == TapeSide.Left)
                {
                    purePursuitNavigateTo(
                        Waypoint(Pose(-15.0, -12.0, 20.degrees), 10.0),
                        Waypoint(Pose(0.0, -20.0, 45.degrees), 10.0),
                    )
                }

                if (kms == TapeSide.Right)
                {
                    navigateTo(Pose(-12.0, -20.0, 0.degrees))
                }
            }

            /*navigateTo(
                when (kms)
                {
                    TapeSide.Right -> Pose(0.0, -20.0, (-15).degrees)
                    TapeSide.Left -> Pose(0.0, -20.0, 25.degrees)
                    TapeSide.Middle -> Pose(0.0, -26.0, 0.degrees)
                }
            )*/
        }

        single("things") {
            Thread.sleep(250L)

            opMode.clawSubsystem.updateClawState(
                ExtendableClaw.ClawStateUpdate.Right,
                ExtendableClaw.ClawState.Open,
            )
        }

        simultaneous("move into deposit yellow pixel") {
            single("Backboard deposit") {
                opMode.clawSubsystem.toggleExtender(ExtendableClaw.ExtenderState.Deposit, force = true)
                Thread.sleep(1000L)

                if (kms == TapeSide.Middle)
                {
                    navigateTo(
                        Pose(0.0, -23.0, 0.degrees)
                    )
                }

                navigateTo(
                    when (kms)
                    {
                        TapeSide.Middle -> Pose(-40.0, -33.0, (-90).degrees)
                        TapeSide.Left -> Pose(-40.0, -39.0, (-90).degrees)
                        TapeSide.Right -> Pose(-40.0, -27.0, (-90).degrees)

                    }
                )
            }

            single("elevator up") {
                opMode.elevatorSubsystem.configureElevatorManually(
                    GlobalConstants.ScalarExpectedElevatorDropHeight
                )
            }
        }

        simultaneous("retract and paark") {
            single("bla bla bla") {
                /*opMode.clawSubsystem.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Right,
                    ExtendableClaw.ClawState.MosaicFix
                )

                purePursuitNavigateTo(
                    Waypoint(Pose(0.0, -55.0, 90.degrees), 20.0),
                    Waypoint(Pose(20.0, -58.0, 90.degrees), 20.0),
                    Waypoint(Pose(62.0, -50.0, 90.degrees), 20.0)
                ) {
                    setDeathMillis(5000.0)
                }

                opMode.clawSubsystem.toggleExtender(
                    ExtendableClaw.ExtenderState.Intake
                )


                Thread.sleep(350L)
                opMode.elevatorSubsystem.configureElevatorManually(0.17)
                Thread.sleep(350L)

                opMode.clawSubsystem.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Right,
                    ExtendableClaw.ClawState.Closed
                )

                Thread.sleep(300L)
                opMode.clawSubsystem.toggleExtender(ExtendableClaw.ExtenderState.Deposit)
                opMode.elevatorSubsystem.configureElevatorManually(0.0)

                purePursuitNavigateTo(
                    Waypoint(Pose(50.0, -55.0, (90.0).degrees), 20.0),
                    Waypoint(Pose(0.0, -55.0, (90.0).degrees), 20.0),
                    Waypoint(Pose(-42.0, -35.0, (-90).degrees), 20.0)
                ) {
                    setDeathMillis(5000.0)
                }

                opMode.elevatorSubsystem.configureElevatorManually(
                    GlobalConstants.ScalarExpectedElevatorDropHeight + 0.2
                )

                // open the claw and wait for the pixel to drop
                opMode.clawSubsystem.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Right,
                    ExtendableClaw.ClawState.Open
                )

                Thread.sleep(1000L)*/

                // open the claw and wait for the pixel to drop
                opMode.clawSubsystem.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Left,
                    ExtendableClaw.ClawState.Open,
                    force = true
                )

                Thread.sleep(1000L)
                opMode.elevatorSubsystem.configureElevatorManually(
                    GlobalConstants.ScalarExpectedElevatorDropHeight + 0.1
                )
                Thread.sleep(350L)

                opMode.clawSubsystem.toggleExtender(
                    ExtendableClaw.ExtenderState.PreLoad
                )


                navigateTo(
                    Pose(-28.0, -33.0, (-90).degrees)
                )

                opMode.clawSubsystem.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Left,
                    ExtendableClaw.ClawState.Closed,
                    force = true
                )

                opMode.elevatorSubsystem.configureElevatorManually(0.0)

                navigateTo(
                    Pose(-37.0, -57.0, (-90).degrees)
                )
            }
        }
    }
)
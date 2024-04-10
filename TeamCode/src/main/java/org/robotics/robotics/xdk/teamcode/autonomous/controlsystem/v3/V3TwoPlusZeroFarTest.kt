package org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.v3

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.pipeline.single
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TapeSide
import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Pose
import org.robotics.robotics.xdk.teamcode.autonomous.profiles.AutonomousProfile
import org.robotics.robotics.xdk.teamcode.autonomous.position.degrees
import org.robotics.robotics.xdk.teamcode.autonomous.position.navigateTo
import org.robotics.robotics.xdk.teamcode.autonomous.position.purePursuitNavigateTo
import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.ActionWaypoint
import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.FieldWaypoint
import org.robotics.robotics.xdk.teamcode.subsystem.Elevator
import org.robotics.robotics.xdk.teamcode.subsystem.claw.ExtendableClaw

@Autonomous(name = "Test | 2+1 FARFARFARFAR V2", group = "Test")
class V3TwoPlusZeroFarTest : AbstractAutoPipeline(

    AutonomousProfile.RedPlayer1TwoPlusZero,
    blockExecutionGroup = { opMode, tapeSide ->
        spikeMark(opMode, tapeSide)

        single("prep for stak") {
            opMode.clawSubsystem.updateClawState(
                ExtendableClaw.ClawStateUpdate.Right,
                ExtendableClaw.ClawState.Intake,
            )

            opMode.clawSubsystem.toggleExtender(ExtendableClaw.ExtenderState.Deposit, force = true)
            opMode.elevatorSubsystem.configureElevatorManually(0.22)

            Thread.sleep(500L)
        }

        single("d") {

            when (tapeSide)
            {
                TapeSide.Right -> purePursuitNavigateTo(
                    FieldWaypoint(
                        Pose(0.0, 0.0, 0.degrees),
                        10.0
                    ),
                    FieldWaypoint(
                        Pose(4.0, -23.0, (-25).degrees),
                        10.0
                    ),
                    FieldWaypoint(
                        Pose(0.0, -39.0, (0).degrees),
                        10.0
                    ),
                    ActionWaypoint {
                        opMode.clawSubsystem.toggleExtender(
                            ExtendableClaw.ExtenderState.Intake,
                            force = true
                        )
                    },
                    FieldWaypoint(
                        Pose(-7.0, -49.0, 90.degrees),
                        4.0
                    ),
                    FieldWaypoint(
                        farStackPickup,
                        10.0
                    )
                )

                TapeSide.Middle -> purePursuitNavigateTo(
                    FieldWaypoint(
                        Pose(0.0, -25.0, 0.degrees),
                        10.0
                    ),
                    FieldWaypoint(
                        Pose(5.0, -20.0, 0.degrees),
                        10.0
                    ),
                    ActionWaypoint {
                        opMode.clawSubsystem.toggleExtender(
                            ExtendableClaw.ExtenderState.Intake,
                            force = true
                        )
                    },
                    FieldWaypoint(
                        farStackPickup,
                        10.0
                    )
                )

                TapeSide.Left -> purePursuitNavigateTo(
                    FieldWaypoint(
                        Pose(0.0, -20.0, 35.degrees),
                        10.0
                    ),
                    FieldWaypoint(
                        Pose(-2.0, -45.0, 0.degrees),
                        10.0
                    ),
                    FieldWaypoint(
                        Pose(-7.0, -49.0, 90.degrees),
                        3.0
                    ),
                    ActionWaypoint {
                        opMode.clawSubsystem.toggleExtender(
                            ExtendableClaw.ExtenderState.Intake,
                            force = true
                        )
                    },
                    FieldWaypoint(
                        farStackPickup,
                        10.0
                    )
                ) {
                    setDeathMillis(5000.0)
                }
            }


        }


        single("Intake from the stack") {



            Thread.sleep(500)
            opMode.clawSubsystem.updateClawState(
                ExtendableClaw.ClawStateUpdate.Right,
                ExtendableClaw.ClawState.Closed
            )
            Thread.sleep(500)

            opMode.clawSubsystem.toggleExtender(
                ExtendableClaw.ExtenderState.Intermediate,
                force = true
            )
            opMode.elevatorSubsystem.configureElevatorManually(0.0)
            Thread.sleep(500)

            //navigateTo(Pose(-50.0, -50.0, (90).degrees))
            purePursuitNavigateTo(
                FieldWaypoint(
                    farStackPickup,
                    10.0
                ),
                FieldWaypoint(
                    Pose(-63.0, -50.0, (90).degrees),
                    10.0
                ),
                ActionWaypoint {
                    opMode.clawSubsystem.toggleExtender(
                        ExtendableClaw.ExtenderState.Deposit,
                        force = true
                    )

                    opMode.elevatorSubsystem.configureElevatorManually(0.15)
                },
                FieldWaypoint(
                    Pose(-87.0, -30.0, (-90.0).degrees),
                    10.0
                )
            ) {
                setDeathMillis(5000.0)
            }
            opMode.clawSubsystem.updateClawState(ExtendableClaw.ClawStateUpdate.Both, ExtendableClaw.ClawState.Closed)
        }

        /*

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
                *//*opMode.clawSubsystem.updateClawState(
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

                Thread.sleep(1000L)*//*

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
        }*/
    }
)
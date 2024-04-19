package org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.v3

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.pipeline.ParallelExecutionGroup
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
import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.ActionWaypoint
import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.FieldWaypoint
import org.robotics.robotics.xdk.teamcode.subsystem.Elevator
import org.robotics.robotics.xdk.teamcode.subsystem.claw.ExtendableClaw

@Autonomous(name = "Red 2+1 Slow", group = "Test")
class V3TwoPlusZeroFarTest : AbstractAutoPipeline(

    AutonomousProfile.RedPlayer2TwoPlusZero,
    blockExecutionGroup = { opMode, tapeSide ->
        waitMillis(7000)
        spikeMark(opMode, tapeSide)

        single("prep for stak") {
            opMode.clawSubsystem.updateClawState(
                ExtendableClaw.ClawStateUpdate.Right,
                ExtendableClaw.ClawState.MosaicFix,
            )
            opMode.clawSubsystem.toggleExtender(ExtendableClaw.ExtenderState.Deposit, force = true)
            opMode.elevatorSubsystem.configureElevatorManually(0.235)

            Thread.sleep(500L)
        }

        single("d") {

            when (tapeSide)
            {
                TapeSide.Right ->
                {
                    purePursuitNavigateTo(
                        FieldWaypoint(
                            Pose(-1.5, -23.0, (-35).degrees),
                            10.0
                        ),
                        FieldWaypoint(
                            Pose(0.0, -48.0, (0).degrees),
                            10.0
                        )
                    )
                    opMode.clawSubsystem.toggleExtender(
                        ExtendableClaw.ExtenderState.Intake,
                        force = true
                    )
                    purePursuitNavigateTo(
                        FieldWaypoint(
                            Pose(0.0, -48.0, (0).degrees),
                            10.0
                        ),
                        FieldWaypoint(
                            Pose(7.5, -48.0, 90.degrees),
                            5.0
                        )
                    ) {
                        setMAX_TRANSLATIONAL_SPEED(0.4)
                        setMAX_ROTATIONAL_SPEED(0.4)
                    }
                    Thread.sleep(750)
                    navigateTo(farStackPickup) {
                        setMAX_TRANSLATIONAL_SPEED(0.3)
                        setMAX_ROTATIONAL_SPEED(0.3)
                    }
                }


                TapeSide.Middle ->
                {
                    purePursuitNavigateTo(
                        FieldWaypoint(
                            Pose(0.0, -25.0, 0.degrees),
                            20.0
                        ),
                        FieldWaypoint(
                            Pose(9.0, -25.0, 45.degrees),
                            7.0
                        ),
                        FieldWaypoint(
                            Pose(7.5, -48.0, 90.degrees),
                            10.0
                        )
                    )

                    opMode.clawSubsystem.toggleExtender(
                        ExtendableClaw.ExtenderState.Intake,
                        force = true
                    )
                    Thread.sleep(750)
                    navigateTo(
                        farStackPickup
                    ) {
                        setMAX_TRANSLATIONAL_SPEED(0.4)
                    }
                }

                TapeSide.Left ->
                {
                    purePursuitNavigateTo(
                        FieldWaypoint(
                            Pose(0.0, -20.0, 35.degrees),
                            20.0
                        ),
                        FieldWaypoint(
                            Pose(0.0, -30.0, 0.degrees),
                            10.0
                        ),
                        FieldWaypoint(
                            Pose(0.0, -49.0, 0.degrees),
                            10.0
                        ),
                        FieldWaypoint(
                            Pose(-7.0, -49.0, 90.degrees),
                            20.0
                        )
                    ) {
                        setDeathMillis(5000.0)
                    }
                    opMode.clawSubsystem.toggleExtender(
                        ExtendableClaw.ExtenderState.Intake,
                        force = true
                    )
                    Thread.sleep(750)
                    navigateTo(
                        farStackPickup
                    ) {
                        setMAX_TRANSLATIONAL_SPEED(0.35)
                        setMAX_ROTATIONAL_SPEED(0.35)
                    }
                }
            }
        }


        single("Intake from the stack") {

            opMode.clawSubsystem.updateClawState(
                ExtendableClaw.ClawStateUpdate.Both,
                ExtendableClaw.ClawState.Closed,
                force = true
            )
            Thread.sleep(750)
            opMode.clawSubsystem.toggleExtender(ExtendableClaw.ExtenderState.Intermediate)


            purePursuitNavigateTo(
                FieldWaypoint(
                    farStackPickup,
                    20.0
                ),
                FieldWaypoint(
                    Pose(-5.0, -56.0, (90).degrees),
                    25.0
                ),
                FieldWaypoint(
                    Pose(-63.0, -56.0, (90).degrees),
                    25.0
                ),
                ActionWaypoint {
                    opMode.elevatorSubsystem.configureElevatorManually(0.0)
                },
                FieldWaypoint(
                    Pose(-72.0, -42.0, (180).degrees),
                    25.0
                ),
                ActionWaypoint {
                    opMode.clawSubsystem.toggleExtender(
                        ExtendableClaw.ExtenderState.Deposit,
                        force = true
                    )
                },
                *when (tapeSide)
                { //backboard
                    TapeSide.Left -> arrayOf(
                        FieldWaypoint(
                            Pose(-86.85, -34.5, (-90.0).degrees),
                            5.0
                        )
                    )

                    TapeSide.Middle -> arrayOf(
                        FieldWaypoint(
                            Pose(-85.85, -28.0, (-90.0).degrees),
                            10.0
                        )
                    )

                    TapeSide.Right -> arrayOf(
                        FieldWaypoint(
                            Pose(-85.85, -22.5, (-90.0).degrees),
                            20.0
                        )
                    )
                }
            ) {
                setDeathMillis(5000.0)
            }
        }
        single("drop on backboard") {
            dropPixels(opMode)
        }

        single("park") {
            opMode.elevatorSubsystem.configureElevatorManually(0.0)
            purePursuitNavigateTo(
                when (tapeSide)
                {
                    TapeSide.Left ->
                        FieldWaypoint(
                            Pose(redBoardX, -41.0, (-90.0).degrees), 4.0
                        )

                    TapeSide.Middle ->
                        FieldWaypoint(
                            Pose(redBoardX, -28.0, (-90.0).degrees), 4.0
                        )

                    TapeSide.Right ->
                        FieldWaypoint(
                            Pose(redBoardX, -22.5, (-90.0).degrees), 4.0
                        )
                },
                FieldWaypoint(parkMiddle, 10.0)
            ) {
                setMAX_TRANSLATIONAL_SPEED(0.5)
            }
        }
    }
)
package org.robotics.robotics.xdk.teamcode.autonomous.shared

import io.liftgate.robotics.mono.pipeline.ExecutionGroup
import io.liftgate.robotics.mono.pipeline.consecutive
import io.liftgate.robotics.mono.pipeline.simultaneous
import io.liftgate.robotics.mono.pipeline.single
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.detection.Direction
import org.robotics.robotics.xdk.teamcode.autonomous.detection.StartPosition
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TapeSide
import org.robotics.robotics.xdk.teamcode.subsystem.claw.ExtendableClaw

/**
 * Shared execution group stages to move forward towards the spike mark
 * and deposit the purple pixel on the corresponding side.
 */
fun ExecutionGroup.depositPurplePixelOnSpikeMarkAndTurnTowardsBackboard(
    pipe: AbstractAutoPipeline,
    gameObjectTapeSide: TapeSide,
    relativeBackboardDirectionAtRobotStart: Direction
)
{
    simultaneous("move forward and intake") {
        single("intake") {
            pipe.clawSubsystem.toggleExtender(
                ExtendableClaw.ExtenderState.Intake,
                force = true
            )
        }

        single("move forward") {
            // move forward a little bit less if we're going to place the pixel where the bar is.
            pipe.move(-GlobalConstants.MoveForwardToSpikeMark +
                if (relativeBackboardDirectionAtRobotStart.oppositeOf().matches(gameObjectTapeSide)) 100 else 0)
        }
    }

    single("turn towards tape if required") {
        val rightOrientationDegrees =
            if (relativeBackboardDirectionAtRobotStart.oppositeOf().matches(gameObjectTapeSide)) 55.0 else 65.0
        val leftOrientationDegrees =
            if (relativeBackboardDirectionAtRobotStart.oppositeOf().matches(gameObjectTapeSide)) 65.0 else 55.0

        if (gameObjectTapeSide == TapeSide.Middle)
        {
            return@single
        }

        pipe.turn(
            if (gameObjectTapeSide == TapeSide.Right)
                rightOrientationDegrees
            else
                -leftOrientationDegrees
        )
    }

    single("deposit pixel and return") {
        pipe.clawSubsystem.updateClawState(
            ExtendableClaw.ClawStateUpdate.Right,
            ExtendableClaw.ClawState.Open,
            force = true
        )
    }

    simultaneous("move back from spike mark and retract") {
        single("move back from spike mark") {
            // wait for the robot to start moving
            Thread.sleep(250L)

            pipe.clawSubsystem.toggleExtender(
                ExtendableClaw.ExtenderState.Deposit,
                force = true
            )
        }

        single("move back from spike mark") {
            pipe.move(-GlobalConstants.MoveBackFromSpikeMark)
        }
    }

    single("turn towards backboard") {
        pipe.turn(relativeBackboardDirectionAtRobotStart.heading)
    }
}

fun ExecutionGroup.moveTowardsBackboard(
    pipe: AbstractAutoPipeline,
    startPosition: StartPosition
)
{
    single("move towards backboard") {
        pipe.move(
            -(if (startPosition == StartPosition.Far)
                GlobalConstants.FarMoveTowardsBackboard else
                    GlobalConstants.CloseMoveTowardsBackboard)
        )
    }
}


/**
 * Completes the second portion of our 2+0 autonomous: deposit yellow pixel and park.
 */
fun ExecutionGroup.strafeIntoBackboardPositionThenDepositYellowPixelAndPark(
    pipe: AbstractAutoPipeline,
    relativeBackboardDirectionAtParkingZone: Direction
)
{
    val maintainDirection = relativeBackboardDirectionAtParkingZone.oppositeOf()
    simultaneous("strafe into drop position") {
        consecutive("strafe") {
            single("strafe into position") {
                pipe.strafe(
                    GlobalConstants.ScalarStrafeIntoPosition *
                        if (relativeBackboardDirectionAtParkingZone == Direction.Left) -1 else 1
                )
            }

            single("sync into heading") {
                pipe.turn(maintainDirection.heading)
            }
        }

        single("raise elevator") {
            pipe.elevatorSubsystem.configureElevatorManually(
                GlobalConstants.ScalarExpectedElevatorDropHeight
            )
        }
    }

    single("move slightly into backboard") {
        pipe.move(-GlobalConstants.ScalarMoveSlightlyIntoBackboard)
    }

    single("deposit yellow pixel") {
        pipe.clawSubsystem.updateClawState(
            ExtendableClaw.ClawStateUpdate.Left,
            ExtendableClaw.ClawState.Open,
            force = true
        )

        Thread.sleep(350L)
    }

    single("move back from into backboard") {
        pipe.move(GlobalConstants.ScalarMoveSlightlyIntoBackboard)
    }

    simultaneous("retract claw/elevator and strafe into parking zone") {
        single("elevator retraction") {
            pipe.elevatorSubsystem.configureElevatorManually(0.0)
        }

        single("right claw reset") {
            pipe.clawSubsystem.updateClawState(
                ExtendableClaw.ClawStateUpdate.Left,
                ExtendableClaw.ClawState.Closed,
                force = true
            )
        }

        consecutive("strafe into position and realign") {
            single("strafe back to parking zone") {
                pipe.strafe(
                    -GlobalConstants.ScalarStrafeIntoParkingPosition *
                        if (relativeBackboardDirectionAtParkingZone == Direction.Left) -1 else 1
                )
            }

            single("realign with heading") {
                pipe.turn(maintainDirection.heading)
            }
        }
    }

    single("park into position") {
        pipe.move(-GlobalConstants.ScalarMoveIntoParkingZone)
    }
}
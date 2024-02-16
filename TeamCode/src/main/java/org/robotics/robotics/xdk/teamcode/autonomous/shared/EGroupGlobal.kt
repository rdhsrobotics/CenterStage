package org.robotics.robotics.xdk.teamcode.autonomous.shared

import io.liftgate.robotics.mono.pipeline.ExecutionGroup
import io.liftgate.robotics.mono.pipeline.consecutive
import io.liftgate.robotics.mono.pipeline.simultaneous
import io.liftgate.robotics.mono.pipeline.single
import io.liftgate.robotics.mono.pipeline.waitMillis
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
    startPosition: StartPosition,
    relativeBackboardDirectionAtRobotStart: Direction,
    isSpikeMarkOnly: Boolean
)
{
    val amountToMoveToSpikeMark = -GlobalConstants.MoveForwardToSpikeMark +
            // if it's left or right, move a little bit LESS
            if (relativeBackboardDirectionAtRobotStart.oppositeOf().matches(gameObjectTapeSide)) 100 else 0

    simultaneous("move forward and intake") {
        single("intake") {
            pipe.clawSubsystem.toggleExtender(
                ExtendableClaw.ExtenderState.Intake,
                force = true
            )
        }

        single("move forward") {
            pipe.move(amountToMoveToSpikeMark, 0.0)
        }
    }

    val headingFixedTowardsSpikeMark = GlobalConstants.TurnToSpikeMark *
            when (gameObjectTapeSide) {
                TapeSide.Right -> -1.0
                TapeSide.Left -> 1.0
                else -> 0.0
            }

    single("turn towards tape if required") {
        if (gameObjectTapeSide == TapeSide.Middle)
        {
            return@single
        }

        pipe.turn(headingFixedTowardsSpikeMark)
    }

    single("deposit pixel") {
        pipe.clawSubsystem.updateClawState(
            ExtendableClaw.ClawStateUpdate.Right,
            ExtendableClaw.ClawState.Open,
            force = true
        )

        if (gameObjectTapeSide == TapeSide.Middle)
        {
            return@single
        }
    }

    simultaneous("move back from spike mark and retract") {
        single("retract extender and close claw") {
            // wait for the robot to start moving
            Thread.sleep(250L)

            pipe.clawSubsystem.toggleExtender(
                ExtendableClaw.ExtenderState.Deposit,
                force = true
            )

            pipe.clawSubsystem.updateClawState(
                ExtendableClaw.ClawStateUpdate.Right,
                ExtendableClaw.ClawState.Closed,
                force = true
            )
        }

        single("move back from spike mark") {
            pipe.turn(0.0)

            if (isSpikeMarkOnly)
            {
                return@single
            }

            pipe.move(-amountToMoveToSpikeMark, 0.0)
        }
    }

    if (isSpikeMarkOnly)
    {
        return
    }

    single("turn towards backboard") {
        pipe.turn(relativeBackboardDirectionAtRobotStart.heading)
    }
}

fun ExecutionGroup.moveTowardsBackboard(
    pipe: AbstractAutoPipeline,
    startPosition: StartPosition,
    direction: Direction
)
{
    single("move towards backboard") {
        // move to the backboard
        pipe.move(
            -(if (startPosition == StartPosition.Far)
                GlobalConstants.FarMoveTowardsBackboard else
                    GlobalConstants.CloseMoveTowardsBackboard),
            direction.heading
        )
    }
}

/**s
 * Completes the second portion of our 2+0 autonomous: deposit yellow pixel and park.
 */
fun ExecutionGroup.strafeIntoBackboardPositionThenDepositYellowPixelAndPark(
    pipe: AbstractAutoPipeline,
    tapeSide: TapeSide,
    startPosition: StartPosition,
    relativeBackboardDirectionAtParkingZone: Direction
)
{
    // opposite of where the backboard is relative to the backboard
    // is the direction the robot should be facing
    val maintainDirection = relativeBackboardDirectionAtParkingZone.oppositeOf()
    val strafePositions = mapOf(
        // Red values
        Direction.Left to mapOf(
            TapeSide.Left to 600,
            TapeSide.Middle to 570,
            TapeSide.Right to 480
        ),
        // Blue values
        Direction.Right to mapOf(
            TapeSide.Left to 480,
            TapeSide.Middle to 570,
            TapeSide.Right to 600
        )
    )

    val strafeFromParkingToBackboard = strafePositions[relativeBackboardDirectionAtParkingZone]!![tapeSide]!!
    single("strafe into position") {
        // strafe either left or right based on where the backboard is relative to the robot
        val strafeDirectionFactor = if (relativeBackboardDirectionAtParkingZone == Direction.Left) -1.0 else 1.0
        pipe.strafe(-strafeFromParkingToBackboard * strafeDirectionFactor)
    }

    single("sync into heading") {
        // again, align robot with the direction it's supposed to be in to compensate for strafe issues
        pipe.turn(maintainDirection.heading)
    }

    simultaneous("move slightly into backboard and raise elevator") {
        single("raise elevator") {
            pipe.elevatorSubsystem.configureElevatorManually(
                GlobalConstants.ScalarExpectedElevatorDropHeight
            )
        }

        single("move slightly into backboard") {
            pipe.move(-GlobalConstants.ScalarMoveSlightlyIntoBackboard, maintainDirection.heading)
        }
    }

    waitMillis(250L)
    single("deposit yellow pixel") {
        // open the claw and wait for the pixel to drop
        pipe.clawSubsystem.updateClawState(
            ExtendableClaw.ClawStateUpdate.Left,
            ExtendableClaw.ClawState.Open,
            force = true
        )
    }

    waitMillis(250L)
    single("move back from into backboard") {
        pipe.move(GlobalConstants.ScalarMoveSlightlyIntoBackboard, maintainDirection.heading)
    }

    simultaneous("retract claw/elevator and strafe into parking zone") {
        single("elevator retraction") {
            pipe.elevatorSubsystem.configureElevatorManually(0.0)
        }

        single("right claw reset") {
            // wait to make sure the pixel has dropped
            Thread.sleep(350L)

            pipe.clawSubsystem.updateClawState(
                ExtendableClaw.ClawStateUpdate.Left,
                ExtendableClaw.ClawState.Closed,
                force = true
            )
        }

        consecutive("strafe into position and realign") {
            single("strafe back to parking zone") {
                // strafe into the parking zone with the direction based on where the backboard
                // was relative to the robot when it was previously in parking
                val strafeDirectionFactor = if (relativeBackboardDirectionAtParkingZone == Direction.Left) 1.0 else -1.0
                pipe.strafe(-strafeFromParkingToBackboard * strafeDirectionFactor)
            }

            single("realign with heading") {
                // align back into the heading we want to maintain
                pipe.turn(maintainDirection.heading)
            }
        }
    }

    single("park into position") {
        // move into parking zone
        pipe.move(-GlobalConstants.ScalarMoveIntoParkingZone, maintainDirection.heading)
    }
}
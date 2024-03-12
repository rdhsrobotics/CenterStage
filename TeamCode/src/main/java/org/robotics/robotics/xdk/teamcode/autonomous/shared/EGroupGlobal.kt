package org.robotics.robotics.xdk.teamcode.autonomous.shared

import io.liftgate.robotics.mono.pipeline.ExecutionGroup
import io.liftgate.robotics.mono.pipeline.consecutive
import io.liftgate.robotics.mono.pipeline.simultaneous
import io.liftgate.robotics.mono.pipeline.single
import io.liftgate.robotics.mono.pipeline.waitMillis
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.detection.Direction
import org.robotics.robotics.xdk.teamcode.autonomous.detection.StartPosition
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TapeSide
import org.robotics.robotics.xdk.teamcode.autonomous.detection.targetAprilTagIDs
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
    val amountToMoveToSpikeMark = -GlobalConstants.MoveForwardToSpikeMark

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
            when (gameObjectTapeSide)
            {
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

    waitMillis(250L)
    simultaneous("move back from spike mark and retract") {
        single("retract extender and close claw") {
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
            Thread.sleep(300L)

            if (isSpikeMarkOnly)
            {
                return@single
            }

            if (gameObjectTapeSide != TapeSide.Middle)
            {
                pipe.turn(0.0)
            }

            // don't smash into the wall lol
            pipe.move(-amountToMoveToSpikeMark, 0.0)
        }
    }

    if (isSpikeMarkOnly)
    {
        return
    }

    val maintainDirection = relativeBackboardDirectionAtRobotStart.heading/* +
            if (startPosition == StartPosition.Far && relativeBackboardDirectionAtRobotStart == Direction.Left)
                -5.0 else 0.0*/

    single("turn towards backboard") {
        pipe.turn(maintainDirection)
    }
}

fun ExecutionGroup.moveTowardsBackboard(
    pipe: AbstractAutoPipeline,
    startPosition: StartPosition,
    direction: Direction
)
{
    val maintainDirection = direction.heading/* +
            if (startPosition == StartPosition.Far && direction == Direction.Left)
                -5.0 else 0.0*/

    single("move towards backboard") {
        // move to the backboard
        pipe.move(
            -(if (startPosition == StartPosition.Far)
                GlobalConstants.FarMoveTowardsBackboard else
                GlobalConstants.CloseMoveTowardsBackboard),
            maintainDirection
        )
    }
}

/**
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
    val maintainDirection = relativeBackboardDirectionAtParkingZone.oppositeOf().heading/* +
            if (startPosition == StartPosition.Far && relativeBackboardDirectionAtParkingZone.oppositeOf() == Direction.Left)
                -5.0 else 0.0*/

    val strafePositions = mapOf(
        // Red values
        Direction.Left to mapOf(
            TapeSide.Left to 780,
            TapeSide.Middle to 650,
            TapeSide.Right to 390
        ),
        // Blue values                                                                                                                                 nbbbbbbbbbbbbbbbbbc
        Direction.Right to mapOf(
            TapeSide.Left to 600,
            TapeSide.Middle to 790,
            TapeSide.Right to 990
        )
    )

    val strafeFromParkingToBackboard =
        strafePositions[relativeBackboardDirectionAtParkingZone]!![tapeSide]!!
    single("strafe into position") {
        // strafe either left or right based on where the backboard is relative to the robot
        val strafeDirectionFactor =
            if (relativeBackboardDirectionAtParkingZone == Direction.Left) -1.0 else 1.0
        pipe.strafe(-strafeFromParkingToBackboard * strafeDirectionFactor)
    }

    single("sync into heading") {
        // again, align robot with the direction it's supposed to be in to compensate for strafe issues
        pipe.turn(maintainDirection)
    }

    /*single("relocalize") {
        pipe.visionPipeline.aprilTagLocalizer()
            .relocalize(
                targetId = targetAprilTagIDs[relativeBackboardDirectionAtParkingZone]!![tapeSide]!!
            ) { detection ->
                pipe.movementHandler.relocalize(detection.ftcPose)
            }
            .join()
    }*/

    simultaneous("move slightly into backboard and raise elevator") {
        single("raise elevator") {
            pipe.elevatorSubsystem.configureElevatorManually(
                GlobalConstants.ScalarExpectedElevatorDropHeight
            )
        }

        single("move slightly into backboard") {
            pipe.move(-GlobalConstants.ScalarMoveSlightlyIntoBackboard, maintainDirection)
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
        pipe.move(GlobalConstants.ScalarMoveSlightlyIntoBackboard, maintainDirection)
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

        single("strafe back to parking zone") {
            // TODO: Park Left/Right options
            // strafe into the parking zone with the direction based on where the backboard
            // was relative to the robot when it was previously in parking
            val strafeDirectionFactor =
                if (relativeBackboardDirectionAtParkingZone == Direction.Left) 1.0 else -1.0
            pipe.strafe(-strafeFromParkingToBackboard * strafeDirectionFactor)
        }
    }

    single("park into position") {
        // move into parking zone
        pipe.move(-GlobalConstants.ScalarMoveIntoParkingZone, maintainDirection)
    }
}
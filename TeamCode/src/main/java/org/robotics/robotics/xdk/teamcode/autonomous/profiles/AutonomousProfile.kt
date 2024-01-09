package org.robotics.robotics.xdk.teamcode.autonomous.profiles

import io.liftgate.robotics.mono.pipeline.ExecutionGroup
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.detection.Direction
import org.robotics.robotics.xdk.teamcode.autonomous.detection.StartPosition
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TapeSide
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TeamColor
import org.robotics.robotics.xdk.teamcode.autonomous.shared.depositPurplePixelOnSpikeMarkAndTurnTowardsBackboard
import org.robotics.robotics.xdk.teamcode.autonomous.shared.moveTowardsBackboard
import org.robotics.robotics.xdk.teamcode.autonomous.shared.strafeIntoBackboardPositionThenDepositYellowPixelAndPark

sealed class AutonomousProfile(
    val teamColor: TeamColor,
    val relativeBackboardDirectionAtRobotStart: Direction,
    val startPosition: StartPosition
)
{
    open fun buildExecutionGroup(): ExecutionGroup.(AbstractAutoPipeline, TapeSide) -> Unit =
        { opMode, tapeSide ->
            depositPurplePixelOnSpikeMarkAndTurnTowardsBackboard(
                pipe = opMode,
                gameObjectTapeSide = tapeSide,
                relativeBackboardDirectionAtRobotStart = relativeBackboardDirectionAtRobotStart
            )

            moveTowardsBackboard(
                pipe = opMode,
                startPosition = startPosition,
                direction = relativeBackboardDirectionAtRobotStart
            )

            strafeIntoBackboardPositionThenDepositYellowPixelAndPark(
                pipe = opMode,
                relativeBackboardDirectionAtParkingZone = relativeBackboardDirectionAtRobotStart.oppositeOf()
            )
        }

    data object RedPlayer1TwoPlusZero : AutonomousProfile(
        teamColor = TeamColor.Red,
        relativeBackboardDirectionAtRobotStart = Direction.Right,
        startPosition = StartPosition.Close
    )

    data object RedPlayer2TwoPlusZero : AutonomousProfile(
        teamColor = TeamColor.Red,
        relativeBackboardDirectionAtRobotStart = Direction.Right,
        startPosition = StartPosition.Far
    )

    data object BluePlayer1TwoPlusZero : AutonomousProfile(
        teamColor = TeamColor.Blue,
        relativeBackboardDirectionAtRobotStart = Direction.Left,
        startPosition = StartPosition.Close
    )

    data object BluePlayer2TwoPlusZero : AutonomousProfile(
        teamColor = TeamColor.Blue,
        relativeBackboardDirectionAtRobotStart = Direction.Left,
        startPosition = StartPosition.Far
    )
}
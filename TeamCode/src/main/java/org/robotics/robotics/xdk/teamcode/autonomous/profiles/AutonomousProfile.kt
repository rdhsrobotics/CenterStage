package org.robotics.robotics.xdk.teamcode.autonomous.profiles

import io.liftgate.robotics.mono.pipeline.ExecutionGroup
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.detection.StartPosition
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TapeSide
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TeamColor

sealed class AutonomousProfile(
    val teamColor: TeamColor,
    val startPosition: StartPosition,
    val isSpikeMarkOnly: Boolean = false
)
{
    open fun buildExecutionGroup(): ExecutionGroup.(AbstractAutoPipeline, TapeSide) -> Unit =
        context@{ opMode, tapeSide ->
//            val tapeSide = TapeSide.Right
           /* depositPurplePixelOnSpikeMarkAndTurnTowardsBackboard(
                pipe = opMode,
                gameObjectTapeSide = tapeSide,
                startPosition = startPosition,
                relativeBackboardDirectionAtRobotStart = relativeBackboardDirectionAtRobotStart,
                isSpikeMarkOnly = isSpikeMarkOnly
            )

            if (isSpikeMarkOnly)
            {
                return@context
            }

            moveTowardsBackboard(
                pipe = opMode,
                startPosition = startPosition,
                direction = relativeBackboardDirectionAtRobotStart
            )

            strafeIntoBackboardPositionThenDepositYellowPixelAndPark(
                pipe = opMode,
                tapeSide = tapeSide,
                startPosition = startPosition,
                relativeBackboardDirectionAtParkingZone = relativeBackboardDirectionAtRobotStart.oppositeOf()
            )*/
        }

    data object RedPlayer1TwoPlusZero : AutonomousProfile(
        teamColor = TeamColor.Red,
        startPosition = StartPosition.Close
    )

    data object RedPlayer2TwoPlusZero : AutonomousProfile(
        teamColor = TeamColor.Red,
        startPosition = StartPosition.Far
    )

    data object BluePlayer1TwoPlusZero : AutonomousProfile(
        teamColor = TeamColor.Blue,
        startPosition = StartPosition.Close
    )

    data object BluePlayer2TwoPlusZero : AutonomousProfile(
        teamColor = TeamColor.Blue,
        startPosition = StartPosition.Far
    )

    // Spike mark autos
    data object RedPlayer1SpikeMarkOnly : AutonomousProfile(
        teamColor = TeamColor.Red,
        startPosition = StartPosition.Close,
        isSpikeMarkOnly = true
    )

    data object RedPlayer2SpikeMarkOnly : AutonomousProfile(
        teamColor = TeamColor.Red,
        startPosition = StartPosition.Far,
        isSpikeMarkOnly = true
    )

    data object BluePlayer1SpikeMarkOnly : AutonomousProfile(
        teamColor = TeamColor.Blue,
        startPosition = StartPosition.Close,
        isSpikeMarkOnly = true
    )

    data object BluePlayer2SpikeMarkOnly : AutonomousProfile(
        teamColor = TeamColor.Blue,
        startPosition = StartPosition.Far,
        isSpikeMarkOnly = true
    )
}
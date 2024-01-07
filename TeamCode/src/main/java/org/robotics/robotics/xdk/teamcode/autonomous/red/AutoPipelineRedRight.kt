package org.robotics.robotics.xdk.teamcode.autonomous.red

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.Mono
import org.robotics.robotics.xdk.teamcode.Global
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.detection.Direction
import org.robotics.robotics.xdk.teamcode.autonomous.detection.StartPosition
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TapeSide
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TeamColor
import org.robotics.robotics.xdk.teamcode.autonomous.shared.depositPurplePixelOnSpikeMarkAndTurnTowardsBackboard
import org.robotics.robotics.xdk.teamcode.autonomous.shared.moveTowardsBackboard
import org.robotics.robotics.xdk.teamcode.autonomous.shared.strafeIntoBackboardPositionThenDepositYellowPixelAndPark

/**
 * @author Subham
 * @since 10/23/2023
 */
@Autonomous(
    name = "Red | Player 1",
    group = "Red",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineRedRight : AbstractAutoPipeline()
{
    override fun getTeamColor() = TeamColor.Red
    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono
        .buildExecutionGroup {
            depositPurplePixelOnSpikeMarkAndTurnTowardsBackboard(
                pipe = this@AutoPipelineRedRight,
                gameObjectTapeSide = tapeSide,
                relativeBackboardDirectionAtRobotStart = Direction.Right
            )

            moveTowardsBackboard(
                pipe = this@AutoPipelineRedRight,
                startPosition = StartPosition.Close
            )

            strafeIntoBackboardPositionThenDepositYellowPixelAndPark(
                pipe = this@AutoPipelineRedRight,
                relativeBackboardDirectionAtParkingZone = Direction.Left
            )
        }
}
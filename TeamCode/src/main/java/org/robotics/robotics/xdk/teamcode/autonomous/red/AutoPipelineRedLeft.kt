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
    name = "Red | Player 2",
    group = "Red",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineRedLeft : AbstractAutoPipeline()
{
    override fun getTeamColor() = TeamColor.Red
    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono
        .buildExecutionGroup {
            depositPurplePixelOnSpikeMarkAndTurnTowardsBackboard(
                pipe = this@AutoPipelineRedLeft,
                gameObjectTapeSide = tapeSide,
                relativeBackboardDirectionAtRobotStart = Direction.Right
            )

            moveTowardsBackboard(
                pipe = this@AutoPipelineRedLeft,
                startPosition = StartPosition.Far
            )

            strafeIntoBackboardPositionThenDepositYellowPixelAndPark(
                pipe = this@AutoPipelineRedLeft,
                relativeBackboardDirectionAtParkingZone = Direction.Left
            )
        }
}
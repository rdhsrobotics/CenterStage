package org.robotics.robotics.xdk.teamcode.autonomous.blue

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.robotics.robotics.xdk.teamcode.Global
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.detection.Direction
import org.robotics.robotics.xdk.teamcode.autonomous.detection.StartPosition
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TeamColor
import org.robotics.robotics.xdk.teamcode.autonomous.shared.depositPurplePixelOnSpikeMarkAndTurnTowardsBackboard
import org.robotics.robotics.xdk.teamcode.autonomous.shared.moveTowardsBackboard
import org.robotics.robotics.xdk.teamcode.autonomous.shared.strafeIntoBackboardPositionThenDepositYellowPixelAndPark

/**
 * @author Subham
 * @since 10/23/2023
 */
@Autonomous(
    name = "Blue | Player 2",
    group = "Blue",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineBlueRight : AbstractAutoPipeline(TeamColor.Blue, { opMode, tapeSide ->
    depositPurplePixelOnSpikeMarkAndTurnTowardsBackboard(
        pipe = opMode,
        gameObjectTapeSide = tapeSide,
        relativeBackboardDirectionAtRobotStart = Direction.Left
    )

    moveTowardsBackboard(
        pipe = opMode,
        startPosition = StartPosition.Far
    )

    strafeIntoBackboardPositionThenDepositYellowPixelAndPark(
        pipe = opMode,
        relativeBackboardDirectionAtParkingZone = Direction.Right
    )
})
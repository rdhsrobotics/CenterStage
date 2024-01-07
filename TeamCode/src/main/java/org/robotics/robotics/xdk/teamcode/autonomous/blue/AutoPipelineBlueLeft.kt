package org.robotics.robotics.xdk.teamcode.autonomous.blue

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.robotics.robotics.xdk.teamcode.Global
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.profiles.AutonomousProfile

/**
 * @author Subham
 * @since 10/23/2023
 */
@Autonomous(
    name = "Blue | Player 1",
    group = "Blue",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineBlueLeft : AbstractAutoPipeline(AutonomousProfile.BluePlayer1TwoPlusZero)
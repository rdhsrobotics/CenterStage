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
    name = "Blue | Player 2",
    group = "Blue",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineBlueRight : AbstractAutoPipeline(AutonomousProfile.BluePlayer2TwoPlusZero)
package org.robotics.robotics.xdk.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.robotics.robotics.xdk.teamcode.Global
import org.robotics.robotics.xdk.teamcode.autonomous.profiles.AutonomousProfile

@Autonomous(
    name = "Red | Player 2 | 2+0",
    group = "Red",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineRedPlayer2 : AbstractAutoPipeline(AutonomousProfile.RedPlayer2TwoPlusZero)

@Autonomous(
    name = "Red | Player 1 | 2+0",
    group = "Red",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineRedPlayer1 : AbstractAutoPipeline(AutonomousProfile.RedPlayer1TwoPlusZero)

@Autonomous(
    name = "Blue | Player 2 | 2+0",
    group = "Blue",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineBluePlayer2 : AbstractAutoPipeline(AutonomousProfile.BluePlayer2TwoPlusZero)

@Autonomous(
    name = "Blue | Player 1 | 2+0",
    group = "Blue",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineBluePlayer1 : AbstractAutoPipeline(AutonomousProfile.BluePlayer1TwoPlusZero)
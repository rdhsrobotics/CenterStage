package org.robotics.robotics.xdk.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.pipeline.waitMillis
import org.robotics.robotics.xdk.teamcode.Global
import org.robotics.robotics.xdk.teamcode.autonomous.profiles.AutonomousProfile

/*
@Autonomous(
    name = "🔴 | Far | 2+0F",
    group = "Red",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineRedPlayer2 : AbstractAutoPipeline(AutonomousProfile.RedPlayer2TwoPlusZero)

@Autonomous(
    name = "🔴 | Close | 2+0F",
    group = "Red",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineRedPlayer1 : AbstractAutoPipeline(AutonomousProfile.RedPlayer1TwoPlusZero)

@Autonomous(
    name = "🔵 | Far | 2+0F",
    group = "Blue",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineBluePlayer2 : AbstractAutoPipeline(AutonomousProfile.BluePlayer2TwoPlusZero)

@Autonomous(
    name = "🔵 | Close | 2+0F",
    group = "Blue",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineBluePlayer1 : AbstractAutoPipeline(AutonomousProfile.BluePlayer1TwoPlusZero)

// Slow autos
@Autonomous(
    name = "🔵 | Far | 2+0S",
    group = "Blue",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineBluePlayer2SlowMode : AbstractAutoPipeline(
    AutonomousProfile.BluePlayer2TwoPlusZero,
    blockExecutionGroup = { k, v ->
        waitMillis(3000L)
        AutonomousProfile.BluePlayer2TwoPlusZero
            .buildExecutionGroup()(k, v)
    }
)

@Autonomous(
    name = "🔴 | Far | 2+0S",
    group = "Red",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineRedPlayer2SlowMode : AbstractAutoPipeline(
    AutonomousProfile.RedPlayer2TwoPlusZero,
    blockExecutionGroup = { k, v ->
        waitMillis(3000L)
        AutonomousProfile.RedPlayer2TwoPlusZero
            .buildExecutionGroup()(k, v)
    }
)

// Spike mark only autos
@Autonomous(
    name = "🔴 | Far | SPM",
    group = "Red",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineRedPlayer2SpikeMarkOnly : AbstractAutoPipeline(AutonomousProfile.RedPlayer2SpikeMarkOnly)

@Autonomous(
    name = "🔴 | Close | SPM",
    group = "Red",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineRedPlayer1SpikeMarkOnly : AbstractAutoPipeline(AutonomousProfile.RedPlayer1SpikeMarkOnly)

@Autonomous(
    name = "🔵 | Far | SPM",
    group = "Blue",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineBluePlayer2SpikeMarkOnly : AbstractAutoPipeline(AutonomousProfile.BluePlayer2SpikeMarkOnly)

@Autonomous(
    name = "🔵 | Close | SPM",
    group = "Blue",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineBluePlayer1SpikeMarkOnly : AbstractAutoPipeline(AutonomousProfile.BluePlayer1SpikeMarkOnly)
*/

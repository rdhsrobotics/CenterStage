package org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.v2

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.pipeline.single
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.detection.Direction
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TapeSide
import org.robotics.robotics.xdk.teamcode.autonomous.detection.targetAprilTagIDs
import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Pose
import org.robotics.robotics.xdk.teamcode.autonomous.profiles.AutonomousProfile
import org.robotics.robotics.xdk.teamcode.autonomous.thing.PositionCommand
import java.time.Duration
import kotlin.math.PI

@Autonomous(name = "Test | horsaf", group = "Test")
class V2ControlTestAutoATLocalize : AbstractAutoPipeline(
    AutonomousProfile.RedPlayer1TwoPlusZero,
    blockExecutionGroup = { opMode, _ ->
        single("relocalize") {
            /*opMode.stopAndResetMotors()
            opMode.runMotors()

            val result = opMode.visionPipeline.aprilTagLocalizer()
                .relocalize(
                    targetId = targetAprilTagIDs[Direction.Left]!![TapeSide.Middle]!!
                ) { detection ->
                    opMode.movementHandler.relocalize(detection.ftcPose)
                }
                .join()

            opMode.stopAndResetMotors()

            opMode.telemetry.addLine("Localization result: $result")
            opMode.telemetry.update()*/


            PositionCommand(
                Pose(-0.0, -30.0, 0.0), opMode
            ).runLol()

            PositionCommand(
                Pose(-40.0, -35.0, -PI / 2), opMode
            ).runLol()

            PositionCommand(
                Pose(-10.0, -55.0, PI / 2), opMode
            ).runLol()

            PositionCommand(
                Pose(50.0, -55.0, PI / 2), opMode
            ).runLol()
            PositionCommand(
                Pose(60.0, -40.0, PI / 2), opMode
            ).runLol()
            PositionCommand(
                Pose(-10.0, -55.0, PI / 2), opMode
            ).runLol()
            PositionCommand(
                Pose(-40.0, -35.0, -PI / 2), opMode
            ).runLol()
        }
    }
)
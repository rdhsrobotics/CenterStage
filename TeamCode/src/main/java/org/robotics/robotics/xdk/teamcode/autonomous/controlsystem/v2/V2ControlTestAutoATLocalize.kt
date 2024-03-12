package org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.v2

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.pipeline.single
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.detection.Direction
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TapeSide
import org.robotics.robotics.xdk.teamcode.autonomous.detection.targetAprilTagIDs
import org.robotics.robotics.xdk.teamcode.autonomous.profiles.AutonomousProfile
import java.time.Duration

@Autonomous(name = "Test | Relocalize (RedRight) (Middle)", group = "Test")
class V2ControlTestAutoATLocalize : AbstractAutoPipeline(
    AutonomousProfile.RedPlayer1TwoPlusZero,
    blockExecutionGroup = { opMode, _ ->
        single("relocalize") {
            opMode.stopAndResetMotors()
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
            opMode.telemetry.update()
        }
    }
)
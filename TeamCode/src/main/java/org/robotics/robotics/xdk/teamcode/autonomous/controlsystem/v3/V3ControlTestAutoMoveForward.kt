package org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.v3

import com.arcrobotics.ftclib.purepursuit.Waypoint
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.pipeline.single
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Pose
import org.robotics.robotics.xdk.teamcode.autonomous.position.degrees
import org.robotics.robotics.xdk.teamcode.autonomous.position.navigateTo
import org.robotics.robotics.xdk.teamcode.autonomous.position.purePursuitNavigateTo
import org.robotics.robotics.xdk.teamcode.autonomous.profiles.AutonomousProfile
import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.FieldWaypoint

@Autonomous(name = "Test | Move Forward", group = "Test")
class V3ControlTestAutoMoveForward : AbstractAutoPipeline(
    AutonomousProfile.RedPlayer1TwoPlusZero,
    blockExecutionGroup = { opMode, _ ->
        single("move forward and backward") {
            purePursuitNavigateTo(
                FieldWaypoint(Pose(0.0, 0.0, 0.degrees), 15.0),
                FieldWaypoint(Pose(0.0, -40.0, 0.degrees), 15.0),
            )
        }
    }
)
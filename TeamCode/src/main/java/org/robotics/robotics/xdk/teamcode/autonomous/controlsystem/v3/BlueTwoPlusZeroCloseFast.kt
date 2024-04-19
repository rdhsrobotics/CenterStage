package org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.v3

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.pipeline.ParallelExecutionGroup
import io.liftgate.robotics.mono.pipeline.simultaneous
import io.liftgate.robotics.mono.pipeline.single
import io.liftgate.robotics.mono.pipeline.waitMillis
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TapeSide
import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Pose
import org.robotics.robotics.xdk.teamcode.autonomous.profiles.AutonomousProfile
import org.robotics.robotics.xdk.teamcode.autonomous.position.degrees
import org.robotics.robotics.xdk.teamcode.autonomous.position.navigateTo
import org.robotics.robotics.xdk.teamcode.autonomous.position.purePursuitNavigateTo
import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.ActionWaypoint
import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.FieldWaypoint
import org.robotics.robotics.xdk.teamcode.subsystem.Elevator
import org.robotics.robotics.xdk.teamcode.subsystem.claw.ExtendableClaw

val blueCloseBoardX = 35.0

@Autonomous(name = "Blue Close 2+0 Fast Park Middle", group = "Test")
class BlueTwoPlusZeroCloseFast : AbstractAutoPipeline(


    AutonomousProfile.RedPlayer2TwoPlusZero,
    blockExecutionGroup = { opMode, tapeSide ->
        spikeMark(opMode, tapeSide)
        /*TapeSide.Middle -> Pose(-40.0, -33.0, (-90).degrees)
                        TapeSide.Left -> Pose(-40.0, -39.0, (-90).degrees)
                        TapeSide.Right -> Pose(-40.0, -27.0, (-90).degrees)
                        */

        single("go to backboad") {
            when (tapeSide) {
                TapeSide.Left -> purePursuitNavigateTo(
                    FieldWaypoint(Pose(-1.5, -24.75, 35.degrees), 10.0),
                    FieldWaypoint(Pose(0.0, -15.0, 35.degrees), 10.0),
                    FieldWaypoint(Pose(blueCloseBoardX, -25.0, 90.degrees), 10.0)
                )
                TapeSide.Middle -> purePursuitNavigateTo(
                    FieldWaypoint(Pose(5.0, -20.0, 0.degrees), 10.0),
                    FieldWaypoint(Pose(blueCloseBoardX, -30.0, 90.degrees), 10.0))
                TapeSide.Right -> navigateTo(Pose(blueCloseBoardX, -36.0, 90.degrees))
            }
            dropPixels(opMode)
        }

        single("park") {
            opMode.elevatorSubsystem.configureElevatorManually(0.0)
            purePursuitNavigateTo(
                when (tapeSide) {
                    TapeSide.Left -> FieldWaypoint(Pose(blueCloseBoardX, -39.0, 90.degrees), 15.0)
                    TapeSide.Middle -> FieldWaypoint(Pose(blueCloseBoardX, -33.0, 90.degrees), 15.0)
                    TapeSide.Right -> FieldWaypoint(Pose(blueCloseBoardX, -25.0, 90.degrees), 15.0)
                },
                when (tapeSide) {
                    TapeSide.Left -> FieldWaypoint(Pose(blueCloseBoardX - 15, -40.0, 90.degrees), 10.0)
                    TapeSide.Middle -> FieldWaypoint(Pose(blueCloseBoardX - 15, -40.0, 90.degrees), 10.0)
                    TapeSide.Right -> FieldWaypoint(Pose(blueCloseBoardX - 15, -40.0, 90.degrees), 10.0)
                },
                FieldWaypoint(Pose(blueCloseBoardX - 10, -50.0, 90.degrees), 15.0),
                FieldWaypoint(Pose(blueCloseBoardX + 6, -56.5, 90.degrees), 15.0)
            )
            Thread.sleep(1000)
        }
    }
)
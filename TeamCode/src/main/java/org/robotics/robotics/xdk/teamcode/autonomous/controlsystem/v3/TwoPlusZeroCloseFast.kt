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

val boardX = -25.0

@Autonomous(name = "Red 2+0 Fast", group = "Test")
class TwoPlusZeroCloseFast : AbstractAutoPipeline(


    AutonomousProfile.BluePlayer2TwoPlusZero,
    blockExecutionGroup = { opMode, tapeSide ->
        spikeMark(opMode, tapeSide)

        single("go to backboad") {
            when (tapeSide) {
                TapeSide.Left -> navigateTo(Pose(boardX, -32.0, -90.degrees))
                TapeSide.Middle -> navigateTo(Pose(boardX, -27.0, -90.degrees))
                TapeSide.Right -> purePursuitNavigateTo(
                    FieldWaypoint(
                        Pose(1.5, -24.75, -35.degrees),
                        10.0
                    ),
                    FieldWaypoint(
                        Pose(0.0, -15.0, -35.degrees),
                        10.0
                    ),
                    FieldWaypoint(
                        Pose(boardX, -15.0, -90.degrees),
                        10.0
                    )
                )
            }
            dropPixels(opMode)
        }

        single("park") {
            purePursuitNavigateTo(
                when (tapeSide) {
                    TapeSide.Left -> FieldWaypoint(Pose(boardX, -32.0, -90.degrees), 15.0)
                    TapeSide.Middle -> FieldWaypoint(Pose(boardX, -27.0, -90.degrees), 15.0)
                    TapeSide.Right -> FieldWaypoint(Pose(boardX, -15.0, -90.degrees), 15.0)
                },
                when (tapeSide) {
                    TapeSide.Left -> FieldWaypoint(Pose(boardX - 10, -32.0, -90.degrees), 15.0)
                    TapeSide.Middle -> FieldWaypoint(Pose(boardX - 10, -27.0, -90.degrees), 15.0)
                    TapeSide.Right -> FieldWaypoint(Pose(boardX - 10, -15.0, -90.degrees), 15.0)
                },
                FieldWaypoint(Pose(boardX, -50.0, -90.degrees), 15.0)
            )
        }
    }
)
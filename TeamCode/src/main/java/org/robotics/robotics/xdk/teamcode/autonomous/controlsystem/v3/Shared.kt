package org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.v3

import io.liftgate.robotics.mono.pipeline.RootExecutionGroup
import io.liftgate.robotics.mono.pipeline.single
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TapeSide
import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Pose
import org.robotics.robotics.xdk.teamcode.autonomous.position.degrees
import org.robotics.robotics.xdk.teamcode.autonomous.position.navigateTo
import org.robotics.robotics.xdk.teamcode.autonomous.position.purePursuitNavigateTo
import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.FieldWaypoint
import org.robotics.robotics.xdk.teamcode.subsystem.claw.ExtendableClaw

val farStackPickup = Pose(12.5, -48.0, 90.degrees)

fun RootExecutionGroup.spikeMark(opMode: AbstractAutoPipeline, kms: TapeSide)
{
    single("Pixel Deposit") {
        opMode.clawSubsystem.toggleExtender(
            ExtendableClaw.ExtenderState.Intake,
            force = true
        )

        if (kms != TapeSide.Middle)
        {
            if (kms == TapeSide.Left)
            {
                purePursuitNavigateTo(
                    FieldWaypoint(
                        Pose(0.0, -15.0, 0.degrees),
                        10.0
                    ),
                    FieldWaypoint(
                        Pose(0.0, -20.0, 35.degrees),
                        10.0
                    ),
                )
            }

            if (kms == TapeSide.Right)
            {
                purePursuitNavigateTo(
                    FieldWaypoint(
                        Pose(0.0, -18.0, 0.degrees),
                        10.0
                    ),
                    FieldWaypoint(
                        Pose(0.0, -23.0, -25.degrees),
                        10.0
                    ),
                )
            }
            return@single
        }


        navigateTo(Pose(0.0, -25.0, 0.degrees))
    }

    single("purple pixel") {
        Thread.sleep(250L)

        opMode.clawSubsystem.updateClawState(
            ExtendableClaw.ClawStateUpdate.Right,
            ExtendableClaw.ClawState.Open,
        )
    }
}
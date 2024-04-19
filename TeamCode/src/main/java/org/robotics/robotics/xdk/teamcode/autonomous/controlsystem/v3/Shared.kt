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

val redStackY = -48.0
val redBoardX = -74.75

val blueStackY = -61.0

val blueCenterY = -62.0
val blueBoardX = 80.75

val blueStackPickup = Pose(-19.5, blueStackY, -90.degrees)
val blueParkMiddle = Pose(blueBoardX + 2, -54.0, (90).degrees)

val farStackPickup = Pose(12.8, redStackY, 90.degrees)
val parkMiddle = Pose(-85.0, -54.0, (-90).degrees)

fun RootExecutionGroup.dropPixels(opMode: AbstractAutoPipeline) {
    opMode.clawSubsystem.updateClawState(
        ExtendableClaw.ClawStateUpdate.Both,
        ExtendableClaw.ClawState.Open
    )
    opMode.elevatorSubsystem.configureElevatorManually(0.15)

    Thread.sleep(750)
}

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
                        Pose(0.0, 0.0, 0.degrees),
                        5.0
                    ),
                    FieldWaypoint(
                        Pose(0.0, -25.0, 0.degrees),
                        10.0
                    ),
                    FieldWaypoint(
                        Pose(0.0, -20.0, 49.degrees),
                        10.0
                    ),
                )
            }

            if (kms == TapeSide.Right)
            {
                purePursuitNavigateTo(
                    FieldWaypoint(
                        Pose(0.0, 0.0, 0.degrees),
                        5.0
                    ),
                    FieldWaypoint(
                        Pose(0.0, -25.0, 0.degrees),
                        10.0
                    ),
                    FieldWaypoint(
                        Pose(1.5, -24.75, -38.degrees),
                        10.0
                    ),
                )
            }
            return@single
        }


        navigateTo(Pose(0.0, -25.0, 0.degrees))
    }

    single("purple pixel") {
        opMode.clawSubsystem.updateClawState(
            ExtendableClaw.ClawStateUpdate.Right,
            ExtendableClaw.ClawState.Open,
        )
        Thread.sleep(500L)
        opMode.clawSubsystem.toggleExtender(ExtendableClaw.ExtenderState.Deposit, force = true)
    }
}
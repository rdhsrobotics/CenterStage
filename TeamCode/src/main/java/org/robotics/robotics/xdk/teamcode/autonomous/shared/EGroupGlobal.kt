package org.robotics.robotics.xdk.teamcode.autonomous.shared

import io.liftgate.robotics.mono.pipeline.ExecutionGroup
import io.liftgate.robotics.mono.pipeline.simultaneous
import io.liftgate.robotics.mono.pipeline.single
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TapeSide
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TeamColor
import org.robotics.robotics.xdk.teamcode.subsystem.claw.ExtendableClaw

/**
 * Shared execution group stages to move forward towards the spike mark
 * and deposit the purple pixel on the corresponding side.
 */
fun ExecutionGroup.spikeMarkDeposit(
    pipe: AbstractAutoPipeline,
    teamColor: TeamColor,
    gameObjectTapeSide: TapeSide,
    barOrientation: TapeSide
)
{
    simultaneous("move forward and intake") {
        single("intake") {
            pipe.clawSubsystem.toggleExtender(
                ExtendableClaw.ExtenderState.Intake,
                force = true
            )
        }

        single("move forward") {
            // move forward a little bit less if we're going to place the pixel where the bar is.
            pipe.move(-GlobalConstants.MoveForwardToSpikeMark +
                if (gameObjectTapeSide == barOrientation) 100 else 0)
        }
    }

    single("turn towards tape if required") {
        val rightMovement =
    }
}
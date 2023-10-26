package org.riverdell.robotics.xdk.opmodes.pipeline

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import io.liftgate.robotics.mono.Mono
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TapeSide

/**
 * @author Subham
 * @since 10/23/2023
 */
@Disabled
@Autonomous(name = "Blue | Left", preselectTeleOp = "prod")
class AutoPipelineBlueLeft : AbstractAutoPipeline()
{
    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono
        .buildExecutionGroup {
            // TODO:
        }
}
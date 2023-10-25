package org.riverdell.robotics.xdk.opmodes.opmodes.pipeline

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.RootExecutionGroup
import org.riverdell.robotics.xdk.opmodes.opmodes.pipeline.detection.TapeSide

/**
 * @author Subham
 * @since 10/23/2023
 */
@Disabled
@Autonomous(name = "Red | Right", preselectTeleOp = "prod")
class AutoPipelineRedRight : AbstractAutoPipeline()
{
    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono
        .buildExecutionGroup {
            // TODO:
        }
}
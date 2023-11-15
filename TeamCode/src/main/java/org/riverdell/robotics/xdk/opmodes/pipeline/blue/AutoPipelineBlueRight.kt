package org.riverdell.robotics.xdk.opmodes.pipeline.blue

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import io.liftgate.robotics.mono.Mono
import org.riverdell.robotics.xdk.opmodes.pipeline.AbstractAutoPipeline
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TapeSide
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TeamColor

/**
 * @author Subham
 * @since 10/23/2023
 */
@Disabled
@Autonomous(name = "Blue | Right", preselectTeleOp = "prod")
class AutoPipelineBlueRight : AbstractAutoPipeline()
{
    override fun getTeamColor() = TeamColor.Blue
    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono
        .buildExecutionGroup {
        }
}
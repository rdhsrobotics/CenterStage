package org.riverdell.robotics.xdk.opmodes.pipeline.red

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import io.liftgate.robotics.mono.Mono
import org.riverdell.robotics.xdk.opmodes.Global
import org.riverdell.robotics.xdk.opmodes.pipeline.AbstractAutoPipeline
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TapeSide
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TeamColor

/**
 * @author Subham
 * @since 10/23/2023
 */
@Disabled
@Autonomous(name = "Red | Left", group = "Red", preselectTeleOp = Global.RobotCentricTeleOpName)
class AutoPipelineRedLeft : AbstractAutoPipeline()
{
    override fun getTeamColor() = TeamColor.Red
    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono.buildExecutionGroup {

    }
}
package org.riverdell.robotics.xdk.opmodes.pipeline.testing

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.single
import org.riverdell.robotics.xdk.opmodes.pipeline.AbstractAutoPipeline
import org.riverdell.robotics.xdk.opmodes.pipeline.contexts.DrivebaseContext
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TapeSide
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TeamColor

@Autonomous(name = "Test | Auto Movement", preselectTeleOp = "prod")
class IsolatedAutoTesting : AbstractAutoPipeline()
{
    init
    {
        monoShouldDoLogging = false
    }

    override fun getTeamColor() = TeamColor.Red

    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono
        .buildExecutionGroup {

        }
}
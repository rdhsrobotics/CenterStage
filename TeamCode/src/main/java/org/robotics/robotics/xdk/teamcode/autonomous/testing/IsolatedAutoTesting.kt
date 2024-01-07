package org.robotics.robotics.xdk.teamcode.autonomous.testing

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.Mono
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TapeSide
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TeamColor

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
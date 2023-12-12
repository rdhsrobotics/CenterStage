package org.riverdell.robotics.xdk.opmodes.autonomous.controlsystem.v2

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.single
import org.riverdell.robotics.xdk.opmodes.autonomous.AbstractAutoPipeline
import org.riverdell.robotics.xdk.opmodes.autonomous.detection.TapeSide
import org.riverdell.robotics.xdk.opmodes.autonomous.detection.TeamColor

@Autonomous(name = "V2 | Move Forward")
class V2ControlTestAutoMoveForward : AbstractAutoPipeline()
{
    override fun getTeamColor() = TeamColor.Red
    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono.buildExecutionGroup {
        single("move forward") {
            v2().move(-500.0)
            Thread.sleep(10000L)
        }

        /*single<DrivebaseContext>("move backward") {
            v2().move(-500.0)
        }*/
    }
}
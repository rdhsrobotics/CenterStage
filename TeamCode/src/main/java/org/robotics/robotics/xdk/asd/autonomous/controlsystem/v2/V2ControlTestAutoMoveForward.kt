package org.robotics.robotics.xdk.asd.autonomous.controlsystem.v2

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.single
import org.robotics.robotics.xdk.asd.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.asd.autonomous.detection.TapeSide
import org.robotics.robotics.xdk.asd.autonomous.detection.TeamColor

@Autonomous(name = "V2 | Move Forward")
class V2ControlTestAutoMoveForward : AbstractAutoPipeline()
{
    override fun getTeamColor() = TeamColor.Red
    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono.buildExecutionGroup {
        single("move forward") {
            move(4 * -500.0)
            Thread.sleep(10000L)
        }

        /*single<DrivebaseContext>("move backward") {
            move(-500.0)
        }*/
    }
}
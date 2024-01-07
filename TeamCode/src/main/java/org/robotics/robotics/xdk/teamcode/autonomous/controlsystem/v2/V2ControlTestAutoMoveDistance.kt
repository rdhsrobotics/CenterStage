package org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.v2

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.single
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.contexts.ClawSubsystemContext
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TapeSide
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TeamColor

@Autonomous(name = "V2 | Move Distance")
class V2ControlTestAutoMoveDistance : AbstractAutoPipeline()
{
    override fun getTeamColor() = TeamColor.Red
    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono.buildExecutionGroup {
        single<ClawSubsystemContext>("strafe") {
//            moveUntilDistanceReached(12.0)
        }
    }
}
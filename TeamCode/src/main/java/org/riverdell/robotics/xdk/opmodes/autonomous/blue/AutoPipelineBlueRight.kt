package org.riverdell.robotics.xdk.opmodes.autonomous.blue

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.single
import org.riverdell.robotics.xdk.opmodes.Global
import org.riverdell.robotics.xdk.opmodes.autonomous.AbstractAutoPipeline
import org.riverdell.robotics.xdk.opmodes.autonomous.detection.TapeSide
import org.riverdell.robotics.xdk.opmodes.autonomous.detection.TeamColor
import org.riverdell.robotics.xdk.opmodes.autonomous.red.RedRight
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ExtendableClaw

/**
 * @author Subham
 * @since 10/23/2023
 */

@Autonomous(name = "Blue | Right", group = "Blue", preselectTeleOp = Global.RobotCentricTeleOpName)
class AutoPipelineBlueRight : AbstractAutoPipeline()
{
    override fun getTeamColor() = TeamColor.Blue
    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono
        .buildExecutionGroup {
            single("move forward") {
                clawSubsystem.toggleExtender(
                    ExtendableClaw.ExtenderState.Intake
                )
                if (tapeSide == TapeSide.Middle)
                {
                    v2().move(-RedRight.MoveForwardToTape + 60)
                } else
                {
                    v2().move(-RedRight.MoveForwardToTape + 100)
                }
            }
            var deg: Double = 0.0

            if (tapeSide == TapeSide.Left)
            {
                single("turn 20 deg") {
                    v2().turn(65.0)
                    deg = 65.0
                }
            } else if (tapeSide == TapeSide.Right)
            {
                single("turn 20 deg") {
                    v2().turn(-60.0)
                    deg = -60.0
                    v2().move(-50.0)
                }
            }

            single("open left claw") {
                clawSubsystem.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Right,
                    ExtendableClaw.ClawState.Open
                )
                Thread.sleep(350L)
                clawSubsystem.toggleExtender(ExtendableClaw.ExtenderState.Deposit)
            }

            single("move back") {
                if (deg != 0.0)
                {
                    v2().move(175.0)
                    v2().turn(-deg)
                }
                v2().move(-RedRight.MoveBackFromTape)
                v2().turn(90.0)
                v2().move(-1700.0)
            }
        }
}
package org.riverdell.robotics.xdk.opmodes.autonomous.blue

import com.acmerobotics.dashboard.config.Config
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
@Config
object BlueLeft
{
    @JvmField var MovePixelToSpike = 790.0
    @JvmField var MoveBackFromSpike = -300.0
    @JvmField var TurnDegreesTowardBackboard = 90.0
    @JvmField var GoToBackboard = 700.0
    @JvmField var StrafeIntoBackboardPosition = 650.0
    @JvmField var ElevateElevatorAtBackboard = 0.4
    @JvmField var BackUpFromBackboard = -150.0
    // elevator
    @JvmField var StrafeIntoParkingZone = -975.0
}

@Autonomous(name = "Blue | Left", group = "Blue  ", preselectTeleOp = Global.RobotCentricTeleOpName)
class  t4AutoPipelineBlueLeft : AbstractAutoPipeline()
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
                    v2().move(-RedRight.MoveForwardToTape + 30)
                } else
                {
                    v2().move(-RedRight.MoveForwardToTape + 100)
                }
            }
            var deg: Double = 0.0

            if (tapeSide == TapeSide.Left)
            {
                single("turn 20 deg") {
                    v2().turn(25.0)
                    deg = 25.0
                }
            } else if (tapeSide == TapeSide.Right)
            {
                single("turn 20 deg") {
                    v2().turn(-65.0)
                    deg = -65.0
                    v2().move(-50.0)
                }
            }

            single("open left claw") {
                clawSubsystem.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Left,
                    ExtendableClaw.ClawState.Open
                )
                Thread.sleep(350L)
                clawSubsystem.toggleExtender(ExtendableClaw.ExtenderState.Deposit)
            }

            single("move back") {
                if (deg != 0.0)
                {
                    v2().move(150.0)
                    v2().turn(-deg)
                }
                v2().move(-RedRight.MoveBackFromTape)
                v2().turn(90.0)
                v2().move(-600.0)
            }
        }
}
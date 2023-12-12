package org.riverdell.robotics.xdk.opmodes.pipeline.red

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.single
import org.riverdell.robotics.xdk.opmodes.Global
import org.riverdell.robotics.xdk.opmodes.pipeline.AbstractAutoPipeline
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TapeSide
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TeamColor
import org.riverdell.robotics.xdk.opmodes.pipeline.red.RedRight.MoveBackFromTape
import org.riverdell.robotics.xdk.opmodes.pipeline.red.RedRight.MoveForwardToTape
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ExtendableClaw

/**
 * @author Subham
 * @since 10/23/2023
 */
@Config
object RedRight
{
    /*@JvmField var MovePixelToSpike = 825.0
    @JvmField var MoveBackFromSpike = -300.0
    @JvmField var TurnDegreesTowardBackboard = -90.0
    @JvmField var GoToBackboard = 700.0
    @JvmField var StrafeIntoBackboardPosition = -650.0
    @JvmField var ElevateElevatorAtBackboard = 0.4
    @JvmField var BackUpFromBackboard = -150.0
    // elevator
    @JvmField var StrafeIntoParkingZone = 975.0*/
    @JvmField var MoveForwardToTape = 550.0
    @JvmField var MoveBackFromTape = -200.0
}

@Autonomous(name = "Red | Right", group = "Red", preselectTeleOp = Global.RobotCentricTeleOpName)
class AutoPipelineRedRight : AbstractAutoPipeline()
{
    init
    {
        monoShouldDoLogging = false
    }

    override fun getTeamColor() = TeamColor.Red

    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono
        .buildExecutionGroup {
            // TODO: turn on at first
            single("move forward") {
                clawSubsystem.toggleExtender(
                    ExtendableClaw.ExtenderState.Intake
                )
                if (tapeSide == TapeSide.Middle)
                {
                    v2().move(-MoveForwardToTape + 60)
                } else
                {
                    v2().move(-MoveForwardToTape + 100)
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
                    v2().move(160.0)
                    v2().turn(-deg)
                }
                v2().move(-MoveBackFromTape)
                v2().turn(-90.0)
                v2().move(-600.0)
            }
        }
}
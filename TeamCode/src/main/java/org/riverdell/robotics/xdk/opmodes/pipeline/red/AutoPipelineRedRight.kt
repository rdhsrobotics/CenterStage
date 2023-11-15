package org.riverdell.robotics.xdk.opmodes.pipeline.red

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.single
import org.riverdell.robotics.xdk.opmodes.pipeline.AbstractAutoPipeline
import org.riverdell.robotics.xdk.opmodes.pipeline.red.RedRight.BackUpFromBackboard
import org.riverdell.robotics.xdk.opmodes.pipeline.red.RedRight.ElevateElevatorAtBackboard
import org.riverdell.robotics.xdk.opmodes.pipeline.red.RedRight.GoToBackboard
import org.riverdell.robotics.xdk.opmodes.pipeline.red.RedRight.MoveBackFromSpike
import org.riverdell.robotics.xdk.opmodes.pipeline.red.RedRight.MovePixelToSpike
import org.riverdell.robotics.xdk.opmodes.pipeline.red.RedRight.StrafeIntoBackboardPosition
import org.riverdell.robotics.xdk.opmodes.pipeline.red.RedRight.StrafeIntoParkingZone
import org.riverdell.robotics.xdk.opmodes.pipeline.red.RedRight.TurnDegreesTowardBackboard
import org.riverdell.robotics.xdk.opmodes.pipeline.contexts.DrivebaseContext
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TapeSide
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TeamColor
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ExtendableClaw

/**
 * @author Subham
 * @since 10/23/2023
 */
@Config
object RedRight
{
    @JvmField var MovePixelToSpike = 825.0
    @JvmField var MoveBackFromSpike = -300.0
    @JvmField var TurnDegreesTowardBackboard = -90.0
    @JvmField var GoToBackboard = 700.0
    @JvmField var StrafeIntoBackboardPosition = -650.0
    @JvmField var ElevateElevatorAtBackboard = 0.4
    @JvmField var BackUpFromBackboard = -150.0
    // elevator
    @JvmField var StrafeIntoParkingZone = 975.0
}

@Autonomous(name = "Red | Right", preselectTeleOp = "prod")
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
            single<DrivebaseContext>("move pixel to spike") {
                forward(MovePixelToSpike)
            }

            single<DrivebaseContext>("move back from spike") {
                forward(MoveBackFromSpike)
                Thread.sleep(500)
            }

            single<DrivebaseContext>("turn towards backboard") {
                turn(TurnDegreesTowardBackboard)
            }

            single<DrivebaseContext>("go to backboard") {
                forward(GoToBackboard)
            }

            single<DrivebaseContext>("align with backboard") {
                strafe(StrafeIntoBackboardPosition)
            }

            single("elevator") {
                elevatorSubsystem.configureElevatorManually(ElevateElevatorAtBackboard)
            }

            single<DrivebaseContext>("meow") {
                PIDToDistance(12.0)
            }

            single("drop shi") {
                Thread.sleep(350L)
                clawSubsystem.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Both,
                    ExtendableClaw.ClawState.Open
                )
                Thread.sleep(1000L)
                clawSubsystem.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Both,
                    ExtendableClaw.ClawState.Closed
                )
            }

            single("drop shi2") {
                elevatorSubsystem.configureElevatorManually(0.0)
            }

            single<DrivebaseContext>("meow") {
                forward(BackUpFromBackboard)
            }

            single<DrivebaseContext>("Straf") {
                strafe(StrafeIntoParkingZone)
            }
        }
}
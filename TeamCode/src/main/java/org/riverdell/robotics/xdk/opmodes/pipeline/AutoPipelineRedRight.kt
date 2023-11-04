package org.riverdell.robotics.xdk.opmodes.pipeline

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.single
import org.riverdell.robotics.xdk.opmodes.pipeline.RedRight.ElevateElevatorAtBackboard
import org.riverdell.robotics.xdk.opmodes.pipeline.RedRight.GoForwardFlushWithTheBackboard
import org.riverdell.robotics.xdk.opmodes.pipeline.RedRight.GoToBackboard
import org.riverdell.robotics.xdk.opmodes.pipeline.RedRight.MoveBackFromSpike
import org.riverdell.robotics.xdk.opmodes.pipeline.RedRight.MovePixelToSpike
import org.riverdell.robotics.xdk.opmodes.pipeline.RedRight.StrafeIntoBackboardPosition
import org.riverdell.robotics.xdk.opmodes.pipeline.RedRight.StrafeIntoParkingZone
import org.riverdell.robotics.xdk.opmodes.pipeline.RedRight.TurnDegreesTowardBackboard
import org.riverdell.robotics.xdk.opmodes.pipeline.contexts.DrivebaseContext
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TapeSide

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
    @JvmField var ElevateElevatorAtBackboard = 1.0
    @JvmField var GoForwardFlushWithTheBackboard = 210.0
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

    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono
        .buildExecutionGroup {
            single<DrivebaseContext>("move pixel to spike") {
                forward(MovePixelToSpike)
            }

            single<DrivebaseContext>("move back") {
                forward(MoveBackFromSpike)
                sleep(500)
            }

            single<DrivebaseContext>("turn towards backboard") {
                turn(TurnDegreesTowardBackboard)
            }

            single<DrivebaseContext>("go to backboard") {
                forward(GoToBackboard)
            }

            single<DrivebaseContext>("strafe into db oposition") {
                strafe(StrafeIntoBackboardPosition)
            }

            single("elevator") {
                elevatorSubsystem.configureElevatorManually(ElevateElevatorAtBackboard)
            }

            single<DrivebaseContext>("meow") {
                forward(GoForwardFlushWithTheBackboard)
            }

            single("drop shi") {
                Thread.sleep(350L)
                clawSubsystem.expandClaw(1.0)
                Thread.sleep(1000L)
            }

            single("drop shi2") {
                elevatorSubsystem.configureElevatorManually(0.0)
            }

            single<DrivebaseContext>("meow") {
                forward(-GoForwardFlushWithTheBackboard)
            }

            single<DrivebaseContext>("Straf") {
                strafe(StrafeIntoParkingZone)
            }
        }
}
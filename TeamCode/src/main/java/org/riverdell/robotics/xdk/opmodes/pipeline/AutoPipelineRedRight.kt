package org.riverdell.robotics.xdk.opmodes.pipeline

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.parallel
import io.liftgate.robotics.mono.pipeline.single
import org.riverdell.robotics.xdk.opmodes.pipeline.RedRight.ForwardIntoParkingZone
import org.riverdell.robotics.xdk.opmodes.pipeline.RedRight.GoToBackboard
import org.riverdell.robotics.xdk.opmodes.pipeline.RedRight.MoveBackFromSpike
import org.riverdell.robotics.xdk.opmodes.pipeline.RedRight.MovePixelToSpike
import org.riverdell.robotics.xdk.opmodes.pipeline.RedRight.StrafeIntoParkingZone
import org.riverdell.robotics.xdk.opmodes.pipeline.RedRight.TurnDegreesTowardBackboard
import org.riverdell.robotics.xdk.opmodes.pipeline.contexts.DrivebaseContext
import org.riverdell.robotics.xdk.opmodes.pipeline.contexts.ElevatorContext
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TapeSide

/**
 * @author Subham
 * @since 10/23/2023
 */
@Config
object RedRight
{
    @JvmField var MovePixelToSpike = 800.0
    @JvmField var MoveBackFromSpike = -150.0
    @JvmField var GoToBackboard = 1500.0
    @JvmField var StrafeIntoParkingZone = -200.0
    @JvmField var ForwardIntoParkingZone = 100.0

    @JvmField var TurnDegreesTowardBackboard = 90.0
    @JvmField var ElevateElevatorAtBackboard = 0.4
    @JvmField var GoForwardFlushWithTheBackboard = 50.0
}

@Autonomous(name = "Red | Right", preselectTeleOp = "prod")
class AutoPipelineRedRight : AbstractAutoPipeline()
{
    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono
        .buildExecutionGroup {
            single<DrivebaseContext>("move pixel to spike") {
                forward(MovePixelToSpike)
            }

            single<DrivebaseContext>("move back") {
                forward(MoveBackFromSpike)
            }

            single<DrivebaseContext>("turn towards backboard") {
                turn(TurnDegreesTowardBackboard)
            }

            single<DrivebaseContext>("go to backboard") {
                forward(GoToBackboard)
            }

            single<ElevatorContext>("elevator") {
                elevatorSubsystem.configureElevatorManually(0.4)
            }

            single<ElevatorContext>("elevator") {
                elevatorSubsystem.configureElevatorManually(0.4)
            }



            single<DrivebaseContext>("Straf") {
                strafe(StrafeIntoParkingZone)
            }

            single<DrivebaseContext>("Straf") {
                forward(ForwardIntoParkingZone)
            }
        }
}
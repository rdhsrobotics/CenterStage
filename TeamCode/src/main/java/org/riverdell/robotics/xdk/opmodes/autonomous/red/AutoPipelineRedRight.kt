package org.riverdell.robotics.xdk.opmodes.autonomous.red

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.single
import org.riverdell.robotics.xdk.opmodes.Global
import org.riverdell.robotics.xdk.opmodes.autonomous.AbstractAutoPipeline
import org.riverdell.robotics.xdk.opmodes.autonomous.detection.TapeSide
import org.riverdell.robotics.xdk.opmodes.autonomous.detection.TeamColor
import org.riverdell.robotics.xdk.opmodes.autonomous.red.RedRight.MoveBackFromTape
import org.riverdell.robotics.xdk.opmodes.autonomous.red.RedRight.MoveForwardToTape
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

    override fun buildExecutionGroup(_tapeSide: TapeSide) = Mono
        .buildExecutionGroup {
            val tapeSide = TapeSide.Middle
            single("forward") {
                v2().move(-925.0)
                Thread.sleep(500L)
                v2().move(725.0)
                Thread.sleep(500L)
                v2().turn(-90.0)
                Thread.sleep(500L)

                v2().move(-1025.0)
                Thread.sleep(100L)
                v2().strafe(1000.0)
                Thread.sleep(125L)

                v2().turn(-90.0)
            }
        }
}
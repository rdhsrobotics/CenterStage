package org.riverdell.robotics.xdk.opmodes.pipeline

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.single
import org.riverdell.robotics.xdk.opmodes.pipeline.contexts.DrivebaseContext
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TapeSide

@Autonomous(name = "Auto | Testing", preselectTeleOp = "prod")
class IsolatedAutoTesting : AbstractAutoPipeline()
{
    init
    {
        monoShouldDoLogging = false
    }

    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono
        .buildExecutionGroup {
            single<DrivebaseContext>("move pixel to spike") {
                PIDToDistance(12.0)
            }
        }
}
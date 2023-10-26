package org.riverdell.robotics.xdk.opmodes.pipeline

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.single
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TapeSide

/**
 * @author Subham
 * @since 10/23/2023
 */
//@Disabled
@Autonomous(name = "Red | Right", preselectTeleOp = "prod")
class AutoPipelineRedRight : AbstractAutoPipeline()
{
    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono
        .buildExecutionGroup {
            single("forward 12 inches") {
                forwardInches(12)
            }

            /*single("turn 90 degrees") {
                turnDegrees(90.0)
            }*/
        }
}
package org.riverdell.robotics.xdk.opmodes.pipeline

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.parallel
import io.liftgate.robotics.mono.pipeline.single
import org.riverdell.robotics.xdk.opmodes.pipeline.contexts.DrivebaseContext
import org.riverdell.robotics.xdk.opmodes.pipeline.contexts.ElevatorContext
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TapeSide
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TeamColor

/**
 * @author Subham
 * @since 10/23/2023
 */
@Autonomous(name = "Red | Left", preselectTeleOp = "prod")
class AutoPipelineRedLeft : AbstractAutoPipeline()
{
    companion object
    {
        private val teamColor = TeamColor.Red
    }

    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono.buildExecutionGroup {
        single("update team element metadata") {
            put("tape", tapeSide)
        }

        // TODO: RoadRunner compat?
        parallel(
            "move and lower elevator"
        ) {
            single<DrivebaseContext>("move forward towards tape") {
                forward(10.0)
            }

            single<ElevatorContext>("lower elevator") {
                Thread.sleep(1000L)
                // TODO
            }
        }
        single<DrivebaseContext>("turn towards tape") {
            Thread.sleep(1000L)
            // TODO
        }
        single("deposit yellow and purple pixel") {
            Thread.sleep(1000L)
            // TODO
        }
        single<ElevatorContext>("elevate elevator") {
            Thread.sleep(1000L)
            // TODO
        }
        single("grab yellow pixel") {
            Thread.sleep(1000L)
            // TODO
        }
        parallel(
            "turn towards side bar while elevating claw",
        ) {
            single<DrivebaseContext>("turn towards side bar/backdrop") {
                forward(90.0)
                // TODO
            }

            single("elevate claw") {
                Thread.sleep(1000L)
                // TODO
            }
        }

        // ---- depends on position of robot
        single<DrivebaseContext>("go forward") {
            forward(10.0)
            // TODO
        }
        // ----

        parallel(
            "strafe into position while elevating elevator"
        ) {
            single<DrivebaseContext>("strafe into position") {

            }

            single<ElevatorContext>("elevate claw") {
                Thread.sleep(1000L)
                // TODO
            }
        }

        single("deposit yellow pixel") {
            // no detection was found, go ahead and
            // park and drop the pixel that way?
            if (containsKey("deposit-exempt"))
                return@single

            Thread.sleep(1000L)
            // TODO
        }

        parallel(
            "back up and reset claw"
        ) {
            single<DrivebaseContext>("back up") {
                if (it.containsKey("deposit-exempt"))
                    return@single

                Thread.sleep(1000L)
                // TODO
            }

            single("reset claw") {
                if (containsKey("deposit-exempt"))
                    return@single

                Thread.sleep(1000L)
                // TODO
            }
        }

        single<DrivebaseContext>("move into parking zone") {
            Thread.sleep(1000L)
            // TODO
        }
    }
}
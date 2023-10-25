package org.riverdell.robotics.xdk.opmodes.pipeline

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.parallel
import io.liftgate.robotics.mono.pipeline.single
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
            single("move forward towards tape") {
                forwardInches(10)
            }

            single("lower elevator") {
                Thread.sleep(1000L)
                // TODO
            }
        }
        single("turn towards tape") {
            Thread.sleep(1000L)
            // TODO
        }
        single("deposit yellow and purple pixel") {
            Thread.sleep(1000L)
            // TODO
        }
        single("elevate elevator") {
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
            single("turn towards side bar/backdrop") {
                turnDegrees(90.0)
                // TODO
            }

            single("elevate claw") {
                Thread.sleep(1000L)
                // TODO
            }
        }

        // ---- depends on position of robot
        single("go forward") {
            forwardInches(10)
            // TODO
        }
        // ----

        parallel(
            "strafe into position while elevating elevator"
        ) {
            single("strafe into position") {
                // wait for the initial detection
                var detection = visionPipeline
                    .recognizeBackBoardAprilTag(teamColor, tapeSide)
                    .join()
                // if there is no detection, skip deposit
                    ?: return@single kotlin.run {
                        put("deposit-exempt", true)
                    }

                while (true)
                {
                    val locked = visionPipeline
                        // lock onto the april tag detection
                        .recognizeBackBoardAprilTag(
                            teamColor, tapeSide, lock = true
                        )
                        .join()

                    // we lost the april tag :(
                    if (locked == null)
                    {
                        put("deposit-exempt", true)
                        return@single
                    }

                    // TODO: align with the april tag lol
                }
            }

            single("elevate claw") {
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
            single("back up") {
                if (containsKey("deposit-exempt"))
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

        single("move into parking zone") {
            Thread.sleep(1000L)
            // TODO
        }
    }
}
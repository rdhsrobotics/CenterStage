package org.riverdell.robotics.xdk.opmodes.game.pipeline

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.parallel
import io.liftgate.robotics.mono.pipeline.single
import org.riverdell.robotics.xdk.opmodes.game.pipeline.detection.TapeSide

@Autonomous(name = "RedA", preselectTeleOp = "prod")
class AutoPipelineRedA : LinearOpMode() {




    override fun runOpMode() {
        val group = Mono.buildExecutionGroup {
            single("detect team element") {
                // TODO
                Thread.sleep(1000L)
                // do some detection logic and find the tape side
                put("tape", TapeSide.Left)
            }

            // executed simultaneously
            parallel(
                    "move and lower elevator"
            ) {
                single("move towards tape") {
                    Thread.sleep(1000L)
                    // TODO
                }

                single("lower elevator") {
                    Thread.sleep(1000L)
                    // TODO
                }
            }
            single("turn towards tape") {
                // get the tape side which we calculated from the first step
                val tapeSide = require<TapeSide>("tape")
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
                    Thread.sleep(1000L)
                    // TODO
                }

                single("elevate claw") {
                    Thread.sleep(1000L)
                    // TODO
                }
            }

            // ---- depends on position of robot
            single("go forward") {
                Thread.sleep(1000L)
                // TODO
            }
            // ----

            // TODO: do we even need to detect the april tags
            parallel(
                    "strafe into position while elevating elevator"
            ) {
                single("strafe into position") {
                    // align with thing
                    val tapeSide = require<TapeSide>("tape")
                    Thread.sleep(1000L)
                    // TODO
                }

                single("elevate claw") {
                    Thread.sleep(1000L)
                    // TODO
                }
            }

            single("deposit yellow pixel") {
                Thread.sleep(1000L)
                // TODO
            }
        }

        group.executeBlocking()
    }
}
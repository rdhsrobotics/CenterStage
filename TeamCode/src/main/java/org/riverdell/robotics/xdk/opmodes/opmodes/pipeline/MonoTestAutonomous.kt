package org.riverdell.robotics.xdk.opmodes.opmodes.pipeline

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.parallel
import io.liftgate.robotics.mono.pipeline.single

@Autonomous(name = "Mono | Test Auto")
class MonoTestAutonomous : LinearOpMode()
{
    override fun runOpMode()
    {
        telemetry.isAutoClear = false

        Mono.logSink = {
            telemetry.addLine(it)
            telemetry.update()
        }

        val executionGroup = Mono
            .buildExecutionGroup {
                single("asdf") {
                    telemetry.addLine("Hey")
                    telemetry.update()

                    Thread.sleep(1000)
                }

                single("asdf2") {
                    telemetry.addLine("Hey2")
                    telemetry.update()

                    Thread.sleep(1000)
                }

                parallel("asd3") {
                    single("14") {
                        Thread.sleep(1000)
                    }
                    single("15") {
                        Thread.sleep(2000)
                    }
                }

                single("asdf2") {
                    telemetry.addLine("Hey2")
                    telemetry.update()

                    Thread.sleep(15 * 1000)
                }

                single("asdf3") {
                    telemetry.addLine("Hey3")
                    telemetry.update()

                    Thread.sleep(15 * 1000)
                }
            }

        waitForStart()

        executionGroup.describe()
        telemetry.addLine("STARTING EXECUTION!")
        executionGroup.executeBlocking()
    }
}
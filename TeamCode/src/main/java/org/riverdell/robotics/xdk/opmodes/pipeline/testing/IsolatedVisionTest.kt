package org.riverdell.robotics.xdk.opmodes.pipeline.testing

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TeamColor
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.VisionPipeline
import org.riverdell.robotics.xdk.opmodes.pipeline.hardware

@TeleOp(name = "Test | Vision")
class IsolatedVisionTest : LinearOpMode()
{
    private val pipeline by lazy {
        VisionPipeline(
            teamColor = TeamColor.Red,
            webcam = hardware("webcam1")
        )
    }

    override fun runOpMode()
    {
        pipeline.start()

        telemetry.addLine("Waiting for start. Started vision pipeline.")
        telemetry.update()

        waitForStart()

        while (opModeIsActive())
        {
            telemetry.addLine("Pipelining")
            telemetry.addData("Side", pipeline.getTapeSide())
        }

        pipeline.stop()
    }
}
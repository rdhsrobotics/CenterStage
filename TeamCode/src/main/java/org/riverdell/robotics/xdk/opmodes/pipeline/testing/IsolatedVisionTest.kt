package org.riverdell.robotics.xdk.opmodes.pipeline.testing

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.opencv.core.Scalar
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TeamColor
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.VisionPipeline
import org.riverdell.robotics.xdk.opmodes.pipeline.hardware


@TeleOp(name = "Test | Vision")
class IsolatedVisionTest : LinearOpMode()
{
    private val pipeline by lazy {
        VisionPipeline(
            teamColor = TeamColor.Blue,
            webcam = hardware("webcam1")
        )
    }

    override fun runOpMode()
    {
        pipeline.start(true)

        telemetry.addLine("Waiting for start. Started vision pipeline.")
        telemetry.update()

        val scale = Scalar(1 / 1000000.0, 1 / 1000000.0, 1 / 1000000.0)
        while (opModeInInit())
        {
            telemetry.addData("Location", pipeline.propPipeline.location)

            telemetry.addData("leftZone", pipeline.propPipeline.left.mul(scale).toString())
            telemetry.addData("centerZone", pipeline.propPipeline.center.mul(scale).toString())

            telemetry.addData("leftZone", pipeline.propPipeline.leftColor)
            telemetry.addData("centerZone", pipeline.propPipeline.centerColor)

            telemetry.update()
        }

        waitForStart()

        while (opModeIsActive())
        {
            telemetry.addLine("Pipelining")
            telemetry.addData("Side", pipeline.getTapeSide())
            telemetry.update()
        }

        pipeline.stop()
    }
}
package org.riverdell.robotics.xdk.opmodes.pipeline.testing

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TeamColor
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.VisionPipeline
import org.riverdell.robotics.xdk.opmodes.pipeline.hardware

@TeleOp(name = "Test | Vision RED")
class IsolatedVisionTestRed : LinearOpMode()
{
    private val pipeline by lazy {
        VisionPipeline(
            teamColor = TeamColor.Red,
            webcam = hardware("webcam1")
        )
    }

    override fun runOpMode()
    {
        val telemetry = MultipleTelemetry(
            this.telemetry,
            FtcDashboard.getInstance().telemetry
        )

        pipeline.start(true)

        telemetry.addLine("Waiting for start. Started vision pipeline.")
        telemetry.update()

        waitForStart()

        while (opModeIsActive())
        {
            telemetry.addLine("Running pipeline (RED):")
            telemetry.addData(
                "Tape Side",
                pipeline.propPipeline.tapeSide
            )
            telemetry.addData(
                "Percentage",
                pipeline.propPipeline.percentageColorMatch
            )
            telemetry.update()
        }

        pipeline.stop()
    }
}
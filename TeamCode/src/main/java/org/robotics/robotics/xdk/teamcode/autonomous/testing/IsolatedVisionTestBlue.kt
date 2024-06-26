package org.robotics.robotics.xdk.teamcode.autonomous.testing

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TeamColor
import org.robotics.robotics.xdk.teamcode.autonomous.detection.VisionPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.hardware

@TeleOp(name = "Test | Vision BLUE", group = "Vision")
class IsolatedVisionTestBlue : LinearOpMode()
{
    override fun runOpMode()
    {
        val pipeline = VisionPipeline(
            teamColor = TeamColor.Blue,
            opMode = this
        )

        val telemetry = MultipleTelemetry(
            this.telemetry,
            FtcDashboard.getInstance().telemetry
        )

        pipeline.start(VisionPipeline.StreamDestination.Dashboard)

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
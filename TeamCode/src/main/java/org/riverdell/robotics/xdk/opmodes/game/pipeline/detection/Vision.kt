package org.riverdell.robotics.xdk.opmodes.game.pipeline.detection

import android.annotation.SuppressLint
import android.util.Size
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import org.firstinspires.ftc.vision.tfod.TfodProcessor

class Vision(webcam: WebcamName) {
    private val processor: AprilTagProcessor = AprilTagProcessor.Builder()
            .setDrawTagOutline(true)
            .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
            .setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
            .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES) // == CAMERA CALIBRATION ==
            // If you do not manually specify calibration parameters, the SDK will attempt
            // to load a predefined calibration for your camera.
            //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)
            // ... these parameters are fx, fy, cx, cy.
            .build()

    @SuppressLint("SdCardPath")
    private val tfodProcessor = TfodProcessor.Builder()
            .setModelFileName("/sdcard/FIRST/tflitemodels/CenterStage.tflite")
            .setModelLabels(arrayOf("pixel"))
            .build()

    private var builder: VisionPortal.Builder = VisionPortal.Builder()
            .setCamera(webcam)
            .setCameraResolution(Size(640, 480))
            .enableLiveView(true)
            .setStreamFormat(VisionPortal.StreamFormat.YUY2)
            .setAutoStopLiveView(true)
            .addProcessors(processor, tfodProcessor)

    private var portal: VisionPortal? = null

    fun start()
    {
        portal = builder.build()
    }

    fun close()
    {
        portal!!.close()
    }

    /*private fun getAprilTagPose(): AprilTagPoseFtc? {
        var attempts = 0
        val DETECTION_ID = if (DETECTION.equals("left")) 1 else if (DETECTION.equals("middle")) 2 else 3
        while (true) {
            val currentDetections: List<AprilTagDetection> = processor.detections
            telemetry.addData("# AprilTags Detected", currentDetections.size)
            for (detection in currentDetections) {
                if (detection.id === DETECTION_ID) {
                    Vision.close()
                    return detection.ftcPose
                }
            }
            if (++attempts > 7) {
                return null
            }
            telemetry.update()
            sleep(20)
        }
    }*/

}

package org.riverdell.robotics.xdk.opmodes.pipeline.detection

import android.R.id
import android.annotation.SuppressLint
import android.util.Size
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import org.firstinspires.ftc.vision.tfod.TfodProcessor
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvCamera.AsyncCameraOpenListener
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvPipeline
import org.openftc.easyopencv.OpenCvWebcam
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import kotlin.math.roundToInt


/**
 * Some animal pipeline shit. Creates an [AprilTagProcessor] and
 * multiple [TfodProcessor]s under a single [VisionPortal] instance.
 *
 * @author Subham
 * @since 10/23/2023
 */
class VisionPipeline(
    private val webcam: WebcamName,
    private val telemetry: Telemetry
)
{
    companion object
    {
        @SuppressLint("SdCardPath")
        const val CENTER_STAGE_TFLITE_MODEL = "/sdcard/FIRST/tflitemodels/CenterStage.tflite"
        @JvmStatic
        val CENTER_STAGE_MODEL_LABELS = arrayOf("Pixel")
    }

    private val tfodCenterStageProcessor = TfodProcessor.Builder()
        .setModelFileName(CENTER_STAGE_TFLITE_MODEL)
        .setModelLabels(CENTER_STAGE_MODEL_LABELS)
        .build()

    private var portal: VisionPortal? = null

    fun start()
    {
        portal = VisionPortal.Builder()
            .setCamera(webcam)
            .setCameraResolution(Size(640, 480))
            .enableLiveView(true)
            .setStreamFormat(VisionPortal.StreamFormat.YUY2)
            .setAutoStopLiveView(true)
            .addProcessors(tfodCenterStageProcessor)
            .build()
    }

    fun stop()
    {
        portal!!.close()
    }

    private val visionExecutor = Executors.newSingleThreadScheduledExecutor()

    private val gameObjectToAngleEstimations = mutableMapOf(
        -90..-20 to TapeSide.Left,
        -19..19 to TapeSide.Middle,
        20..90 to TapeSide.Right
    )

    fun recognizeGameObjectTapeSide(): CompletableFuture<TapeSide>
    {
        return CompletableFuture.supplyAsync({
            var recogAttempts = 0

            while (recogAttempts++ < 3)
            {
                val matchingRecognition = tfodCenterStageProcessor.recognitions
                        .firstOrNull { it.label == "Pixel" }

                if (matchingRecognition == null)
                {
                    Thread.sleep(500L)
                    continue
                }

                val angleEstimation = matchingRecognition
                        .estimateAngleToObject(AngleUnit.DEGREES)

                val estimatedTapeSide = gameObjectToAngleEstimations.entries
                        .firstOrNull {
                            angleEstimation.roundToInt() in it.key
                        }

                if (estimatedTapeSide != null)
                {
                    return@supplyAsync estimatedTapeSide.value
                }
            }

            return@supplyAsync TapeSide.Middle
        }, visionExecutor)
    }
    /*return CompletableFuture.supplyAsync({
            var recogAttempts = 0

            while (recogAttempts++ < 3)
            {
                val matchingRecognition = tfodCenterStageProcessor.recognitions
                    .firstOrNull { it.label == "Pixel" }

                if (matchingRecognition == null)
                {
                    Thread.sleep(500L)
                    continue
                }

                val angleEstimation = matchingRecognition
                    .estimateAngleToObject(AngleUnit.DEGREES)

                val estimatedTapeSide = gameObjectToAngleEstimations.entries
                    .firstOrNull {
                        angleEstimation.roundToInt() in it.key
                    }

                if (estimatedTapeSide != null)
                {
                    return@supplyAsync estimatedTapeSide.value
                }
            }

            return@supplyAsync TapeSide.Middle
        }, visionExecutor)*/
}

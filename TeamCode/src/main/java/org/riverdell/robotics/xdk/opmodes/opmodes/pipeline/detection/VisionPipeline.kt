package org.riverdell.robotics.xdk.opmodes.opmodes.pipeline.detection

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

    private val aprilTagProcessor: AprilTagProcessor = AprilTagProcessor.Builder()
        .setDrawTagOutline(true)
        .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
        .setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
        .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
        .build()

    @SuppressLint("SdCardPath")
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
            .addProcessors(aprilTagProcessor, tfodCenterStageProcessor)
            .build()
    }

    fun stop()
    {
        portal!!.close()
    }

    private val visionExecutor = Executors.newSingleThreadScheduledExecutor()

    private val fallbackTapeSide = TapeSide.Middle

    // TODO: might need to fix if the camera is mounted under the aeroplane (:nerd:) launcher.
    private val gameObjectToAngleEstimations = mutableMapOf(
        -90..-5 to TapeSide.Left,
        -5..5 to TapeSide.Middle,
        5..90 to TapeSide.Right
    )

    private val teamColorMappings = mutableMapOf(
        TeamColor.Blue to mutableMapOf(
            TapeSide.Left to 1,
            TapeSide.Middle to 2,
            TapeSide.Right to 3
        ),
        TeamColor.Red to mutableMapOf(
            TapeSide.Left to 4,
            TapeSide.Middle to 5,
            TapeSide.Right to 6
        )
    )

    fun recognizeGameObjectTapeSide(): CompletableFuture<TapeSide>
    {
        return CompletableFuture.supplyAsync({
            var recogAttempts = 0

            while (recogAttempts++ < 3)
            {
                // TODO: use custom ML model processor when bill ciphers are printed
                val matchingRecognition = tfodCenterStageProcessor.recognitions
                    .firstOrNull { it.label == "Pixel" }

                if (matchingRecognition == null)
                {
                    Thread.sleep(500L)
                    continue
                }

                val angleEstimation = matchingRecognition.estimateAngleToObject(AngleUnit.DEGREES)
                telemetry.addData("Object estimation", angleEstimation)

                val estimatedTapeSide = gameObjectToAngleEstimations.entries
                    .firstOrNull {
                        angleEstimation.roundToInt() in it.key
                    }

                if (estimatedTapeSide != null)
                {
                    return@supplyAsync estimatedTapeSide.value
                }
            }

            telemetry.addLine("(!) No object was recognized within the range of view, using default $fallbackTapeSide.")
            return@supplyAsync fallbackTapeSide
        }, visionExecutor)
    }

    fun recognizeBackBoardAprilTag(
        teamColor: TeamColor,
        objectTapeSide: TapeSide,
        lock: Boolean = false
    ): CompletableFuture<AprilTagDetection?>
    {
        // in no fucking scenario will this be null
        val mapping = teamColorMappings[teamColor]!![objectTapeSide]

        return CompletableFuture.supplyAsync({
            var recogAttempts = 0

            while (recogAttempts++ < 3)
            {
                // TODO: use custom ML model processor when bill ciphers are printed
                val matchingAprilTagDetection = aprilTagProcessor.detections
                    .firstOrNull { it.id == mapping }

                if (matchingAprilTagDetection == null)
                {
                    if (!lock)
                    {
                        Thread.sleep(500L)
                        continue
                    }

                    return@supplyAsync null
                }

                return@supplyAsync matchingAprilTagDetection
            }

            return@supplyAsync null
        }, visionExecutor)
    }
}

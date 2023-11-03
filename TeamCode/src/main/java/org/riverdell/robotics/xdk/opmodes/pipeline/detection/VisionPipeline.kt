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
) : OpenCvPipeline()
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

    private var portal: VisionPortal? = null
    private var openCvWebcam: OpenCvWebcam? = null

    fun start()
    {
        portal = VisionPortal.Builder()
            .setCamera(webcam)
            .setCameraResolution(Size(640, 480))
            .enableLiveView(true)
            .setStreamFormat(VisionPortal.StreamFormat.YUY2)
            .setAutoStopLiveView(true)
            .addProcessors(aprilTagProcessor)
            .build()

        /*val ad = OpenCvCameraFactory
            .getInstance()
            .createWebcam(webcam)
        ad.setPipeline(this)
        ad.openCameraDeviceAsync(object : AsyncCameraOpenListener
        {
            override fun onOpened()
            {
                ad.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
            }

            override fun onError(errorCode: Int)
            {

            }
        })

        this.openCvWebcam = ad*/
    }

    fun stop()
    {
        portal!!.close()
//        openCvWebcam!!.closeCameraDevice()
    }

    private val visionExecutor = Executors.newSingleThreadScheduledExecutor()

    private val teamIDMappings = mutableMapOf(
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

    fun recognizeBackBoardAprilTag(
        teamColor: TeamColor,
        objectTapeSide: TapeSide,
        lock: Boolean = false
    ): CompletableFuture<AprilTagDetection?>
    {
        // in no fucking scenario will this be null
        val mapping = teamIDMappings[teamColor]!![objectTapeSide]

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

    var


            currentPosition = TapeSide.Left

    private val teamColorMappings = mutableMapOf(
        TeamColor.Red to Scalar(255.0, 0.0, 0.0),
        TeamColor.Blue to Scalar(0.0, 0.0, 255.0)
    )

    override fun processFrame(input: Mat): Mat
    {
        // "Mat" stands for matrix, which is basically the image that the detector will process
        // the input matrix is the image coming from the camera
        // the function will return a matrix to be drawn on your phone's screen

        // The detector detects regular stones. The camera fits two stones.
        // If it finds one regular stone then the other must be the skystone.
        // If both are regular stones, it returns NONE to tell the robot to keep looking

        // Make a working copy of the input matrix in HSV
        // "Mat" stands for matrix, which is basically the image that the detector will process
        // the input matrix is the image coming from the camera
        // the function will return a matrix to be drawn on your phone's screen

        // The detector detects regular stones. The camera fits two stones.
        // If it finds one regular stone then the other must be the skystone.
        // If both are regular stones, it returns NONE to tell the robot to keep looking

        // Make a working copy of the input matrix in HSV
        val mat = Mat()
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV)

        // if something is wrong, we assume there's no skystone

        // if something is wrong, we assume there's no skystone
        if (mat.empty())
        {
            currentPosition = TapeSide.Left
            return input
        }

        // We create a HSV range for yellow to detect regular stones
        // NOTE: In OpenCV's implementation,
        // Hue values are half the real value

        // We create a HSV range for yellow to detect regular stones
        // NOTE: In OpenCV's implementation,
        // Hue values are half the real value
        val lowHSV = Scalar(255.0, 0.0, 0.0) // lower bound HSV for yellow

        val highHSV = Scalar(193.0, 34.0, 34.0) // higher bound HSV for yellow

        val thresh = Mat()

        // We'll get a black and white image. The white regions represent the regular stones.
        // inRange(): thresh[i][j] = {255,255,255} if mat[i][i] is within the range

        // We'll get a black and white image. The white regions represent the regular stones.
        // inRange(): thresh[i][j] = {255,255,255} if mat[i][i] is within the range
        Core.inRange(mat, lowHSV, highHSV, thresh)

        // Use Canny Edge Detection to find edges
        // you might have to tune the thresholds for hysteresis

        // Use Canny Edge Detection to find edges
        // you might have to tune the thresholds for hysteresis
        val edges = Mat()
        Imgproc.Canny(thresh, edges, 100.0, 300.0)

        // https://docs.opencv.org/3.4/da/d0c/tutorial_bounding_rects_circles.html
        // Oftentimes the edges are disconnected. findContours connects these edges.
        // We then find the bounding rectangles of those contours

        // https://docs.opencv.org/3.4/da/d0c/tutorial_bounding_rects_circles.html
        // Oftentimes the edges are disconnected. findContours connects these edges.
        // We then find the bounding rectangles of those contours
        val contours: List<MatOfPoint> = ArrayList()
        val hierarchy = Mat()
        Imgproc.findContours(
            edges,
            contours,
            hierarchy,
            Imgproc.RETR_TREE,
            Imgproc.CHAIN_APPROX_SIMPLE
        )

        val contoursPoly = arrayOfNulls<MatOfPoint2f>(contours.size)
        val boundRect = arrayOfNulls<Rect>(contours.size)
        for (i in contours.indices)
        {
            contoursPoly[i] = MatOfPoint2f()
            Imgproc.approxPolyDP(MatOfPoint2f(*contours[i].toArray()), contoursPoly[i], 3.0, true)
            boundRect[i] = Imgproc.boundingRect(MatOfPoint(*contoursPoly[i]!!.toArray()))
        }

        // Iterate and check whether the bounding boxes
        // cover left and/or right side of the image

        // Iterate and check whether the bounding boxes
        // cover left and/or right side of the image
        val left_x: Double = 0.25 * 680
        val right_x: Double = 0.75 * 680
        var left = false // true if regular stone found on the left side

        var right = false // "" "" on the right side

        for (i in boundRect.indices)
        {
            if (boundRect[i]!!.x < left_x) left = true
            if (boundRect[i]!!.x + boundRect[i]!!.width > right_x) right = true

            // draw red bounding rectangles on mat
            // the mat has been converted to HSV so we need to use HSV as well
            Imgproc.rectangle(mat, boundRect[i], Scalar(0.5, 76.9, 89.8))
        }

        // if there is no yellow regions on a side
        // that side should be a Skystone

        // if there is no yellow regions on a side
        // that side should be a Skystone
        if (!left) currentPosition = TapeSide.Left else if (!right) currentPosition = TapeSide.Right else currentPosition = TapeSide.Left

        return mat // return the mat with rectangles drawn


    }
}

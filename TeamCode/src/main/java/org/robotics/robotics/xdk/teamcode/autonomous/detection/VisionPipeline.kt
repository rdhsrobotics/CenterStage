package org.robotics.robotics.xdk.teamcode.autonomous.detection

import android.util.Size
import com.acmerobotics.dashboard.FtcDashboard
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import io.liftgate.robotics.mono.subsystem.Subsystem
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import org.robotics.robotics.xdk.teamcode.autonomous.detection.elements.GameElementDetection
import org.robotics.robotics.xdk.teamcode.autonomous.hardware

/**
 * Manages and configures all [VisionPortal] processors
 * for an op mode.
 */
class VisionPipeline(
    private val teamColor: TeamColor,
    private val opMode: LinearOpMode
) : AbstractSubsystem()
{
    private lateinit var portal: VisionPortal
    lateinit var propPipeline: GameElementDetection
    lateinit var aprilTag: AprilTagProcessor

    fun getTapeSide() = propPipeline.tapeSide

    enum class StreamDestination
    {
        Dashboard, DriverStation, Both, None;

        fun encapsulates(destination: StreamDestination) =
            this != None && (this == Both || this == destination)
    }

    /**
     * Starts the vision portal with the option of
     * pushing the camera stream to FTCDashboard.
     */
    fun start(destination: StreamDestination)
    {
//        aprilTagLocalizer = AprilTagLocalizer(opMode)
        propPipeline = GameElementDetection(teamColor)

        portal = VisionPortal.Builder()
            .setCamera(
                opMode.hardware<WebcamName>("webcam1")
            )
            .setCameraResolution(Size(640, 480))
            .enableLiveView(destination.encapsulates(StreamDestination.DriverStation))
            .setAutoStopLiveView(true)
            .addProcessors(propPipeline, AprilTagProcessor.Builder().setTagLibrary(
                AprilTagGameDatabase.getCenterStageTagLibrary()
            ).build().apply {
                aprilTag = this
            })
            .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
            .build()

        if (destination.encapsulates(StreamDestination.Dashboard))
        {
            FtcDashboard.getInstance().startCameraStream(
                propPipeline,
                30.0
            )
        }
    }

//    fun aprilTagLocalizer() = aprilTagLocalizer

    fun stop() = portal.close()

    override fun composeStageContext() = TODO()
    override fun doInitialize()
    {
        start(StreamDestination.DriverStation)
    }

    override fun dispose()
    {
        stop()
    }

    override fun isCompleted() = true
}

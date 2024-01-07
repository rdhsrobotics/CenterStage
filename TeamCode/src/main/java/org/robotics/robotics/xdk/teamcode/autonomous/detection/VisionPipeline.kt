package org.robotics.robotics.xdk.teamcode.autonomous.detection

import android.util.Size
import com.acmerobotics.dashboard.FtcDashboard
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal
import org.robotics.robotics.xdk.teamcode.autonomous.detection.elements.GameElementDetection

/**
 * Manages and configures all [VisionPortal] processors
 * for an op mode.
 */
class VisionPipeline(
    private val teamColor: TeamColor,
    private val webcam: WebcamName
)
{
    private var portal: VisionPortal? = null
    val propPipeline by lazy {
        GameElementDetection(teamColor)
    }

    fun getTapeSide() = propPipeline.tapeSide

    /**
     * Starts the vision portal with the option of
     * pushing the camera stream to FTCDashboard.
     */
    @JvmOverloads
    fun start(dashboard: Boolean = false)
    {
        portal = VisionPortal.Builder()
            .setCamera(webcam)
            .setCameraResolution(Size(640, 480))
            .enableLiveView(dashboard)
            .setAutoStopLiveView(true)
            .addProcessors(propPipeline)
            .build()

        if (dashboard)
        {
            FtcDashboard.getInstance().startCameraStream(propPipeline, 30.0)
        }
    }

    fun stop() = portal?.close()
}

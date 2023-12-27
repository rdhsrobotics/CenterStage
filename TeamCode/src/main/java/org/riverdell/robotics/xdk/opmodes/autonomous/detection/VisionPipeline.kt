package org.riverdell.robotics.xdk.opmodes.autonomous.detection

import android.util.Size
import com.acmerobotics.dashboard.FtcDashboard
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal
import org.riverdell.robotics.xdk.opmodes.autonomous.detection.elements.GameElementDetection

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
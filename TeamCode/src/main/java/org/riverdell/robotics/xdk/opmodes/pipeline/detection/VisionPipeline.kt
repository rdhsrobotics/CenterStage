package org.riverdell.robotics.xdk.opmodes.pipeline.detection

import android.util.Size
import com.acmerobotics.dashboard.FtcDashboard
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.prop.PropPipeline

class VisionPipeline(
    private val teamColor: TeamColor,
    private val webcam: WebcamName
)
{
    private var portal: VisionPortal? = null
    val propPipeline by lazy {
        PropPipeline(teamColor)
    }

    fun getTapeSide() = propPipeline.location

    @JvmOverloads
    fun start(dashboard: Boolean = false)
    {
        portal = VisionPortal.Builder()
            .setCamera(webcam)
            .setCameraResolution(Size(1920, 1080))
            .enableLiveView(true)
            .setAutoStopLiveView(true)
            .addProcessors(propPipeline)
            .build()

        if (!dashboard)
        {
            return
        }

        FtcDashboard.getInstance().startCameraStream(
            propPipeline, 30.0
        )
    }

    fun stop()
    {
        portal!!.close()
    }
}

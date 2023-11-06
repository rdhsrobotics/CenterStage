package org.riverdell.robotics.xdk.opmodes.pipeline.detection

import android.util.Size
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.prop.PropPipeline

class VisionPipeline(
    private val teamColor: TeamColor,
    private val webcam: WebcamName
)
{
    private var portal: VisionPortal? = null
    private val propPipeline by lazy {
        PropPipeline(teamColor)
    }

    fun getTapeSide() = propPipeline.location

    fun start()
    {
        portal = VisionPortal.Builder()
            .setCamera(webcam)
            .setCameraResolution(Size(640, 480))
            .enableLiveView(true)
            .setStreamFormat(VisionPortal.StreamFormat.YUY2)
            .setAutoStopLiveView(true)
            .addProcessors(propPipeline)
            .build()
    }

    fun stop()
    {
        portal!!.close()
    }
}

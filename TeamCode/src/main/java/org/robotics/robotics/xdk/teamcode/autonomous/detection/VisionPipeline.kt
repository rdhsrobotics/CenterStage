package org.robotics.robotics.xdk.teamcode.autonomous.detection

import android.util.Size
import com.acmerobotics.dashboard.FtcDashboard
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import io.liftgate.robotics.mono.subsystem.Subsystem
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal
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
    private lateinit var propPipeline: GameElementDetection

    fun getTapeSide() = propPipeline.tapeSide

    /**
     * Starts the vision portal with the option of
     * pushing the camera stream to FTCDashboard.
     */
    @JvmOverloads
    fun start(dashboard: Boolean = false)
    {
        propPipeline = GameElementDetection(teamColor)
        portal = VisionPortal.Builder()
            .setCamera(
                opMode.hardware<WebcamName>("webcam1")
            )
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

    fun stop() = portal.close()

    override fun composeStageContext() = TODO()
    override fun doInitialize()
    {
        start(dashboard = false)
    }

    override fun dispose()
    {
        stop()
    }

    override fun isCompleted() = true
}

package org.riverdell.robotics.xdk.opmodes.subsystem

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.Subsystem
import org.riverdell.robotics.xdk.opmodes.pipeline.hardware
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ClawExpansionConstants

class AirplaneLauncher(private val opMode: LinearOpMode) : Subsystem
{
    private val backingServo by lazy {
        opMode.hardware<Servo>("launcher")
    }

    override fun composeStageContext() = object : StageContext
    {
        override fun isCompleted() = true
        override fun dispose()
        {
            backingServo.position = ClawExpansionConstants.DEFAULT_PLANE_POSITION
        }
    }

    fun launch()
    {
        backingServo.position = ClawExpansionConstants.MAX_PLANE_POSITION
    }

    fun reset()
    {
        backingServo.position = ClawExpansionConstants.DEFAULT_PLANE_POSITION
    }

    override fun initialize()
    {
        backingServo.position = ClawExpansionConstants.DEFAULT_PLANE_POSITION
    }

    override fun dispose()
    {
        backingServo.position = ClawExpansionConstants.DEFAULT_PLANE_POSITION
    }

    // Servos don't have isBusy states
    override fun isCompleted() = true
}
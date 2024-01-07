package org.robotics.robotics.xdk.asd.subsystem

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.robotics.robotics.xdk.asd.autonomous.hardware
import org.robotics.robotics.xdk.asd.subsystem.claw.ClawExpansionConstants

class AirplaneLauncher(private val opMode: LinearOpMode) : AbstractSubsystem()
{
    private val backingServo by lazy {
        opMode.hardware<Servo>("launcher")
    }

    override fun composeStageContext() = object : StageContext
    {
        override fun isCompleted() = true
        override fun dispose() = reset()
    }

    fun launch()
    {
        backingServo.position = ClawExpansionConstants.MAX_PLANE_POSITION
    }

    fun reset()
    {
        backingServo.position = ClawExpansionConstants.DEFAULT_PLANE_POSITION
    }

    override fun doInitialize() = reset()
    override fun dispose() = reset()

    // Servos don't have isBusy states
    override fun isCompleted() = true
}
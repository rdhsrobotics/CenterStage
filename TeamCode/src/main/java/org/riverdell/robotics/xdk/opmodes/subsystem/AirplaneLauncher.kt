package org.riverdell.robotics.xdk.opmodes.subsystem

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.Subsystem
import org.riverdell.robotics.xdk.opmodes.pipeline.hardware

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
            backingServo.position = 1.0
        }
    }

    fun launch()
    {
        backingServo.position = 0.45
    }

    fun reset()
    {
        backingServo.position = 1.0
    }

    override fun initialize()
    {
        backingServo.position = 1.0
    }

    override fun dispose()
    {
        backingServo.position = 1.0
    }

    // Servos don't have isBusy states
    override fun isCompleted() = true
}
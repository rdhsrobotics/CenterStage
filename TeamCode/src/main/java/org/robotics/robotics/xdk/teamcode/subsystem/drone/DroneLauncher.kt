package org.robotics.robotics.xdk.teamcode.subsystem.drone

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.robotics.robotics.xdk.teamcode.autonomous.hardware
import org.robotics.robotics.xdk.teamcode.subsystem.claw.ClawExpansionConstants

class DroneLauncher(private val opMode: LinearOpMode) : AbstractSubsystem()
{
    private lateinit var backingServo: Servo

    override fun composeStageContext() = object : StageContext
    {
        override fun isCompleted() = true
        override fun dispose() = reset()
    }

    /**
     * Launches the airplane.
     */
    fun launch()
    {
        backingServo.position = DroneLauncherConstants.MAX_PLANE_POSITION
    }

    /**
     * Arms the airplane servo.
     */
    fun reset()
    {
        backingServo.position = DroneLauncherConstants.DEFAULT_PLANE_POSITION
    }

    override fun doInitialize()
    {
        backingServo = opMode.hardware<Servo>("launcher")
        reset()
    }
    override fun dispose() = reset()

    // Servos don't have isBusy states
    override fun isCompleted() = true
}
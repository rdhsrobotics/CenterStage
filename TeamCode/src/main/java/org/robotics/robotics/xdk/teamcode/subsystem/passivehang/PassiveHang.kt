package org.robotics.robotics.xdk.teamcode.subsystem.passivehang

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.robotics.robotics.xdk.teamcode.autonomous.hardware

class PassiveHang(private val opMode: LinearOpMode) : AbstractSubsystem()
{
    private lateinit var servo: Servo

    override fun composeStageContext() = object : StageContext
    {
        override fun isCompleted() = true
        override fun dispose()
        {

        }
    }

    var hangState = PassiveHangState.Armed
    enum class PassiveHangState
    {
        Armed, Deployed
    }

    /**
     * Deploy both hang hooks.
     */
    fun deploy()
    {
        servo.position = PassiveHangConstants.RETRACTOR_DEPLOYED
        hangState = PassiveHangState.Deployed
    }

    /**
     * Arms the hang retractors.
     */
    fun arm()
    {
        servo.position = PassiveHangConstants.RETRACTOR_ARMED
        hangState = PassiveHangState.Armed
    }

    override fun doInitialize()
    {
        servo = opMode.hardware<Servo>("hang")
        arm()
    }

    override fun dispose()
    {
        arm()
    }

    // Servos don't have isBusy states
    override fun isCompleted() = true
}
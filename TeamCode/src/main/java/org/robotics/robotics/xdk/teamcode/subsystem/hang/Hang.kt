package org.robotics.robotics.xdk.teamcode.subsystem.hang

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.robotics.robotics.xdk.teamcode.autonomous.hardware
import org.robotics.robotics.xdk.teamcode.subsystem.stopAndResetEncoder

class Hang(private val opMode: LinearOpMode) : AbstractSubsystem()
{
    lateinit var actuator: DcMotorEx

    override fun composeStageContext() = object : StageContext
    {
        override fun isCompleted() = true
        override fun dispose()
        {

        }
    }

    var hangState = PassiveHangState.Braking
    enum class PassiveHangState
    {
        Armed, Deployed, Braking
    }

    fun brake()
    {
        actuator.power = 0.0
//        actuatorTwo.power = 0.0

        hangState = PassiveHangState.Braking
    }

    /**
     * Deploy both hang hooks.
     */
    fun deploy()
    {
        listOf(actuator).forEach {
            it.power = 1.0
            it.targetPosition = HangConstants.RETRACTED_ENCODER_TICKS
            it.mode = DcMotor.RunMode.RUN_TO_POSITION
        }

        hangState = PassiveHangState.Deployed
    }

    /**
     * Arms the hang retractors.
     */
    fun arm()
    {
        listOf(actuator).forEach {
            it.power = 1.0
            it.targetPosition = 0
            it.mode = DcMotor.RunMode.RUN_TO_POSITION
        }

        hangState = PassiveHangState.Armed
    }

    override fun doInitialize()
    {
        actuator = opMode.hardware<DcMotorEx>("hang")
        actuator.direction = DcMotorSimple.Direction.FORWARD

        actuator.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        actuator.stopAndResetEncoder()
        actuator.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    override fun dispose()
    {

    }

    // Servos don't have isBusy states
    override fun isCompleted() = true
}
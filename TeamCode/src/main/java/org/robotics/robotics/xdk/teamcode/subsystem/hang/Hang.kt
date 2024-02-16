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
    private lateinit var actuator: DcMotorEx

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
        actuator.power = 1.0
        actuator.targetPosition = HangConstants.RETRACTED_ENCODER_TICKS
        actuator.mode = DcMotor.RunMode.RUN_TO_POSITION

        hangState = PassiveHangState.Deployed
    }

    /**
     * Arms the hang retractors.
     */
    fun arm()
    {
        actuator.power = 1.0
        actuator.targetPosition = 0
        actuator.mode = DcMotor.RunMode.RUN_TO_POSITION

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
package org.riverdell.robotics.xdk.opmodes.subsystem

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.Subsystem
import org.riverdell.robotics.xdk.opmodes.pipeline.hardware

class Elevator(private val opMode: LinearOpMode) : Subsystem
{
    val backingMotor by lazy {
        opMode.hardware<DcMotorEx>("elevator")
    }

    override fun composeStageContext() = object : StageContext
    {
        override fun isCompleted() = !backingMotor.isBusy
    }

    override fun initialize()
    {
        backingMotor.direction = DcMotorSimple.Direction.FORWARD
        backingMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        backingMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    fun elevateTo(position: Int)
    {
        backingMotor.targetPosition = position
        backingMotor.power = 0.7
        backingMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
    }

    fun reset()
    {
        backingMotor.power = 0.0
    }

    override fun isCompleted() = !backingMotor.isBusy
}
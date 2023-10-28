package org.riverdell.robotics.xdk.opmodes.subsystem

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.Subsystem
import org.riverdell.robotics.xdk.opmodes.pipeline.hardware
import kotlin.math.max
import kotlin.math.min

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
        backingMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        backingMotor.stopAndResetEncoder()
        backingMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    fun configureElevator(stick: Double)
    {
        val target = min(
            max((backingMotor.currentPosition + stick * 175).toInt(), -1130),
            0
        )

        backingMotor.power = stick
        backingMotor.targetPosition = target
        backingMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
    }

    override fun isCompleted() = !backingMotor.isBusy
}
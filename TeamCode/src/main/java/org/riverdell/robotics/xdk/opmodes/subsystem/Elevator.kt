package org.riverdell.robotics.xdk.opmodes.subsystem

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.xdk.opmodes.autonomous.hardware
import kotlin.math.max
import kotlin.math.min

class Elevator(private val opMode: LinearOpMode) : AbstractSubsystem()
{
    val backingMotor by lazy {
        opMode.hardware<DcMotorEx>("elevator")
    }

    val backingHangMotor by lazy {
        opMode.hardware<DcMotorEx>("hang")
    }

    override fun composeStageContext() = object : StageContext
    {
        override fun isCompleted() = !backingMotor.isBusy
    }

    override fun dispose()
    {
        backingMotor.stopAndResetEncoder()
        backingHangMotor.stopAndResetEncoder()
    }

    override fun doInitialize()
    {
        listOf(backingMotor, backingHangMotor).forEach {
            it.direction = DcMotorSimple.Direction.REVERSE
            it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

            it.stopAndResetEncoder()
        }

        backingMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        backingHangMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    private var elevatorUpdateLock = Any()

    fun toggleHangLift(power: Double)
    {
        backingHangMotor.power = power
        backingHangMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    fun configureElevator(stick: Double) = synchronized(elevatorUpdateLock)
    {
        val target = min(
            max((backingMotor.currentPosition + stick * 175).toInt(), -1130),
            0
        )

        backingMotor.power = stick
        backingMotor.targetPosition = target
        backingMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
    }

    fun configureElevatorManually(position: Double) = synchronized(elevatorUpdateLock)
    {
        backingMotor.power = 0.86
        backingMotor.targetPosition = (position * -1130).toInt()
        backingMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
    }

    fun configureElevatorManuallyRaw(position: Int) = synchronized(elevatorUpdateLock)
    {
        backingMotor.power = 1.0
        backingMotor.targetPosition = position
        backingMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
    }

    override fun isCompleted() = !backingMotor.isBusy
}
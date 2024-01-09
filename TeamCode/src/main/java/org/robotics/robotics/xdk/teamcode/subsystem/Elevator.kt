package org.robotics.robotics.xdk.teamcode.subsystem

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.robotics.robotics.xdk.teamcode.autonomous.hardware
import kotlin.math.max
import kotlin.math.min

/**
 * A subsystem implementation for all of our elevators.
 *
 * @author Subham
 */
class Elevator(private val opMode: LinearOpMode) : AbstractSubsystem()
{
    private lateinit var backingMotor: DcMotorEx
    private lateinit var backingHangMotor: DcMotorEx

    override fun composeStageContext() = object : StageContext
    {
        override fun isCompleted() = !backingMotor.isBusy
    }

    fun getCurrentElevatorPosition() = backingMotor.currentPosition
    fun getTargetElevatorPosition() = backingMotor.targetPosition

    override fun dispose()
    {
        backingMotor.stopAndResetEncoder()
        backingMotor.close()

        backingHangMotor.stopAndResetEncoder()
        backingHangMotor.close()
    }

    /**
     * Initializes and configures the elevator hardware.
     */
    override fun doInitialize()
    {
        backingMotor = opMode.hardware<DcMotorEx>("elevator")
        backingMotor.direction = DcMotorSimple.Direction.FORWARD

        backingMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        backingMotor.stopAndResetEncoder()
        backingMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER

        backingHangMotor = opMode.hardware<DcMotorEx>("hang")
        backingHangMotor.direction = DcMotorSimple.Direction.REVERSE
        backingHangMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        backingHangMotor.stopAndResetEncoder()
        backingHangMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    private var elevatorUpdateLock = Any()

    /**
     * Runs the hang motor at a given power without the encoder.
     */
    fun toggleHangLift(power: Double)
    {
        backingHangMotor.power = power
        backingHangMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    /**
     * Increments the target position for the claw elevator
     * based on a given joystick value.
     */
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

    /**
     * Sets the target position to a value in the [0, 1] interval.
     */
    fun configureElevatorManually(position: Double) = synchronized(elevatorUpdateLock)
    {
        backingMotor.power = (if (position < backingMotor.targetPosition) -1 else 1) * 0.86
        backingMotor.targetPosition = (position * -1130).toInt()
        backingMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
    }

    /**
     * Sets the target position to a value in the [-1130, 0] interval.
     */
    fun configureElevatorManuallyRaw(position: Int) = synchronized(elevatorUpdateLock)
    {
        backingMotor.power = (if (position < backingMotor.targetPosition) -1 else 1) * 0.86
        backingMotor.targetPosition = position
        backingMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
    }

    override fun isCompleted() = !backingMotor.isBusy
}
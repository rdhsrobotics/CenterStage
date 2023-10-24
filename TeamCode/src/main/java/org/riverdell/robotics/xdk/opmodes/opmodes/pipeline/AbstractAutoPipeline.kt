package org.riverdell.robotics.xdk.opmodes.opmodes.pipeline

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import kotlin.math.abs

abstract class AbstractAutoPipeline : LinearOpMode() 
{
    private val frontRight by lazy { hardware<DcMotor>("frontRight") }
    private val frontLeft by lazy { hardware<DcMotor>("frontLeft") }

    private val backRight by lazy { hardware<DcMotor>("backRight") }
    private val backLeft by lazy { hardware<DcMotor>("backLeft") }

    override fun runOpMode()
    {
        frontLeft.direction = DcMotorSimple.Direction.REVERSE
        frontRight.direction = DcMotorSimple.Direction.FORWARD

        backLeft.direction = DcMotorSimple.Direction.REVERSE
        backRight.direction = DcMotorSimple.Direction.FORWARD
        stopAndResetMotors()
    }

    fun templatedMotorControl(target: Int, consumer: (Double) -> Unit)
    {
        var averagePosition = 0
        var error = target
        var velocity = 100
        var previous = 0
        var integral = 0

        stopAndResetMotors()
        runMotors()

        while (((abs(error) > 15) || (velocity > 15)) && opModeIsActive())
        {
            previous = averagePosition
            averagePosition = ((abs(frontLeft.currentPosition) + abs(frontRight.currentPosition) + abs(backLeft.currentPosition) + abs(backRight.currentPosition)) / 4) * (target / abs(target))
            error = averagePosition - target
            velocity = averagePosition - previous
            integral += error

            consumer(
                -0.002 * error - 0.00001 * integral + 0.05 * velocity
            )
        }
    }

    fun forward(target: Int) = templatedMotorControl(target, ::setPower)
    fun forwardInches(inches: Int)
    {
        forward((inches * AutoPipelineUtilities.UNITS_PER_INCH_FORWARD).toInt())
    }

    fun strafe(target: Int) = templatedMotorControl(target, ::setStrafePower)
    fun strafeInches(inches: Int)
    {
        strafe((inches * AutoPipelineUtilities.UNITS_PER_INCH_STRAFE).toInt())   
    }

    fun turn(target: Int) = templatedMotorControl(target, ::setTurnPower)
    fun turnDegrees(degrees: Double)
    {
        turn((degrees * AutoPipelineUtilities.UNITS_PER_DEGREE_TURN).toInt())  
    }

    fun stopAndResetMotors() = configureMotorsToDo {
        it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    }

    fun runMotors() = configureMotorsToDo {
        it.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    fun setPower(power: Double) = configureMotorsToDo {
        it.power = power
    }

    fun setTurnPower(power: Double)
    {
        frontLeft.power = power
        frontRight.power = -power

        backLeft.power = power
        backRight.power = -power   
    }

    fun setStrafePower(power: Double)
    {
        frontLeft.power = power
        frontRight.power = -power

        backLeft.power = -power
        backRight.power = power
    }

    fun configureMotorsToDo(consumer: (DcMotor) -> Unit)
    {
        listOf(frontRight, frontLeft, backRight, backLeft).forEach(consumer::invoke)
    }
}
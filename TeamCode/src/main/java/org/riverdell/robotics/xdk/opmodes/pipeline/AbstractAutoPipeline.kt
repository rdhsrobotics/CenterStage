package org.riverdell.robotics.xdk.opmodes.pipeline

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareDevice
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.RootExecutionGroup
import org.firstinspires.ftc.robotcore.external.Telemetry.Line
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TapeSide
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.VisionPipeline
import org.riverdell.robotics.xdk.opmodes.subsystem.Elevator
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ExtendableClaw
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.sign

abstract class AbstractAutoPipeline : LinearOpMode() 
{
    private val frontRight by lazy { hardware<DcMotor>("frontRight") }
    private val frontLeft by lazy { hardware<DcMotor>("frontLeft") }

    private val backRight by lazy { hardware<DcMotor>("backRight") }
    private val backLeft by lazy { hardware<DcMotor>("backLeft") }

    private val elevatorSubsystem by lazy { Elevator(this) }
    private val clawSubsystem by lazy { ExtendableClaw(this) }

    internal val visionPipeline by lazy {
        VisionPipeline(
            webcam = hardware("webcam1"),
            telemetry = this.telemetry
        )
    }

    abstract fun buildExecutionGroup(tapeSide: TapeSide): RootExecutionGroup

    override fun runOpMode()
    {
        visionPipeline.start()
        clawSubsystem.initialize()
        elevatorSubsystem.initialize()

        // keep all log entries
//        telemetry.isAutoClear = false

        Mono.logSink = {
            /*telemetry.addLine("[Mono] $it")
            telemetry.update()*/
        }

        frontLeft.direction = DcMotorSimple.Direction.REVERSE
        frontRight.direction = DcMotorSimple.Direction.FORWARD

        backLeft.direction = DcMotorSimple.Direction.REVERSE
        backRight.direction = DcMotorSimple.Direction.FORWARD

        stopAndResetMotors()

        telemetry.addLine("Waiting for start. Started detection...")
        telemetry.update()

        val tapeSide = visionPipeline
            .recognizeGameObjectTapeSide()
            .join()

        telemetry.addLine("Completed detection. Detected tape side: ${tapeSide.name}. Waiting for start...")
        telemetry.update()

        waitForStart()

        telemetry.addLine("Started! Executing the Mono execution group now with ${tapeSide.name}.")
        telemetry.update()

        val executionGroup = buildExecutionGroup(tapeSide)
        executionGroup.executeBlocking()

        stopAndResetMotors()
        terminateAllMotors()

        visionPipeline.stop()

        clawSubsystem.dispose()
        elevatorSubsystem.dispose()
    }

    fun templatedMotorControl(target: Int, motorControl: (Double) -> Unit)
    {
        var averagePosition = 0.0
        var error: Double = target.toDouble()
        var velocity = 100.0
        var previous: Double
        var integral = 0.0

        var previousTelemetryLine: Line? = null
        fun removePreviousStatusLine()
        {
            if (previousTelemetryLine != null)
            {
                telemetry.removeLine(previousTelemetryLine)
            }
        }

        stopAndResetMotors()
        runMotors()

        while (
            (error.absoluteValue > 15 || velocity > 15) &&
            opModeIsActive()
        )
        {
            val frontLeftPos = frontLeft.currentPosition.absoluteValue
            val frontRightPos = frontRight.currentPosition.absoluteValue
            val backRightPos = backRight.currentPosition.absoluteValue
            val backLeftPos = backLeft.currentPosition.absoluteValue

            val averageMotorPositions = (frontRightPos + frontLeftPos + backRightPos + backLeftPos) / 4.0
            previous = averagePosition

            // TODO: possible weird behavior when target is 0 ?
            averagePosition = averageMotorPositions * target.sign

            val percentError = abs((averagePosition - target) / target) * 100
            error = averagePosition - target
            velocity = averagePosition - previous
            integral += error

            removePreviousStatusLine()
            previousTelemetryLine = telemetry.addLine(
                "Current: ${"%.3f".format(averagePosition.toFloat())} | " +
                "Previous: ${"%.3f".format(previous.toFloat())} | " +
                    "Error: ${"%.3f".format(error.toFloat())} | " +
                    "Velocity: ${"%.3f".format(velocity.toFloat())} | " +
                    "Integral: ${"%.3f".format(integral.toFloat())} |" +
                    "Percent Error: ${"%.1f".format(percentError.toFloat())}"
            )
            telemetry.update()

            motorControl(
                (-0.002 * error) - (0.00001 * integral) + (0.05 * velocity)
            )
        }

        removePreviousStatusLine()
        lockUntilMotorsFree()
    }

    fun lockUntilMotorsFree(maximumTimeMillis: Long = 150L)
    {
        val start = System.currentTimeMillis()
        while (
            System.currentTimeMillis() < start + maximumTimeMillis &&
            (frontLeft.isBusy ||
                frontRight.isBusy ||
                backLeft.isBusy ||
                backRight.isBusy)
        )
        {
            sleep(50L)
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

    fun terminateAllMotors() = configureMotorsToDo(HardwareDevice::close)

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
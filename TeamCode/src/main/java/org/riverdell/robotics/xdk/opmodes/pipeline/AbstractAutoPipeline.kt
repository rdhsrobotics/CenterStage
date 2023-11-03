package org.riverdell.robotics.xdk.opmodes.pipeline

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.IMU
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.RootExecutionGroup
import org.firstinspires.ftc.robotcore.external.Telemetry.Line
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles
import org.riverdell.robotics.xdk.opmodes.pipeline.contexts.DrivebaseContext
import org.riverdell.robotics.xdk.opmodes.pipeline.contexts.ElevatorContext
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TapeSide
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.VisionPipeline
import org.riverdell.robotics.xdk.opmodes.subsystem.Elevator
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ExtendableClaw
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.sign

abstract class AbstractAutoPipeline : LinearOpMode()
{
    private val frontRight by lazy { hardware<DcMotor>("frontRight") }
    private val frontLeft by lazy { hardware<DcMotor>("frontLeft") }

    private val backRight by lazy { hardware<DcMotor>("backRight") }
    private val backLeft by lazy { hardware<DcMotor>("backLeft") }

    internal val elevatorSubsystem by lazy { Elevator(this) }
    internal val clawSubsystem by lazy { ExtendableClaw(this) }

    internal var monoShouldDoLogging = true

    lateinit var imu: IMU

    internal val visionPipeline by lazy {
        VisionPipeline(
            webcam = hardware("webcam1"),
            telemetry = this.telemetry
        )
    }

    abstract fun buildExecutionGroup(tapeSide: TapeSide): RootExecutionGroup

    override fun runOpMode()
    {
        this.imu = hardware("imu")
        this.imu.initialize(
            IMU.Parameters(
                RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                    RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                )
            )
        )
        this.imu.resetYaw()

        visionPipeline.start()
        clawSubsystem.initialize()
        elevatorSubsystem.initialize()

        // keep all log entries
        if (monoShouldDoLogging)
        {
            telemetry.isAutoClear = false

            Mono.logSink = {
                telemetry.addLine("[Mono] $it")
                telemetry.update()
            }
        }

        frontLeft.direction = DcMotorSimple.Direction.REVERSE
        frontRight.direction = DcMotorSimple.Direction.FORWARD

        backLeft.direction = DcMotorSimple.Direction.REVERSE
        backRight.direction = DcMotorSimple.Direction.FORWARD

        stopAndResetMotors()

        telemetry.addLine("Waiting for start. Started detection...")
        telemetry.update()

        val tapeSide = visionPipeline.currentPosition

        telemetry.addLine("Completed detection. Detected tape side: ${tapeSide.name}. Waiting for start...")
        telemetry.update()

        waitForStart()

        telemetry.addLine("Started! Executing the Mono execution group now with ${tapeSide.name}.")
        telemetry.update()

        val executionGroup = buildExecutionGroup(tapeSide)
        executionGroup.providesContext { _ ->
            DrivebaseContext(
                listOf(frontRight, frontLeft, backRight, backLeft), this
            )
        }
        executionGroup.providesContext { _ ->
            ElevatorContext(elevatorSubsystem.backingMotor, this)
        }
        executionGroup.executeBlocking()

        telemetry.addLine("Hors 0")
        telemetry.update()
        println("Hors 1")
        telemetry.addLine("Hors 1")
        telemetry.update()
        terminateAllMotors()
        println("Hors 2")
        telemetry.addLine("Hors 2")
        telemetry.update()

        visionPipeline.stop()
        println("Hors 3")

        telemetry.addLine("Hors 3")
        telemetry.update()
        clawSubsystem.dispose()
        println("Hors 4")
        telemetry.addLine("Hors 4")
        telemetry.update()
        elevatorSubsystem.dispose()
        println("Hors 5")
        telemetry.addLine("Hors 5")
        telemetry.update()

        Mono.logSink = { }
        println("Hors 6")
        telemetry.addLine("Hors 6")
        telemetry.update()
    }

    fun runMovementPID(target: Double, motorControl: (Double) -> Unit)
    {
        if (target == 0.0)
        {
            return
        }

        var averagePosition = 0.0
        var error = target
        var velocity = 100.0
        var previous: Double
        var integral = 0.0

        val startTime = System.currentTimeMillis()
        var rampUp: Double

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
            (error.absoluteValue > AutoPipelineUtilities.MOVEMENT_MAX_ERROR ||
                    velocity > AutoPipelineUtilities.MOVEMENT_MAX_VELOCITY) &&
            opModeIsActive()
        )
        {
            val frontLeftPos = frontLeft.currentPosition.absoluteValue
            val frontRightPos = frontRight.currentPosition.absoluteValue
            val backRightPos = backRight.currentPosition.absoluteValue
            val backLeftPos = backLeft.currentPosition.absoluteValue

            rampUp = ((System.currentTimeMillis() - startTime) / AutoPipelineUtilities.MOVEMENT_RAMP_UP_TIME).coerceAtMost(1.0)

            val averageMotorPositions =
                (frontRightPos + frontLeftPos + backRightPos + backLeftPos) / 4.0
            previous = averagePosition

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
//            telemetry.update()

            motorControl(
                -rampUp * (AutoPipelineUtilities.PID_MOVEMENT_KP * error - AutoPipelineUtilities.PID_MOVEMENT_KD * velocity)
            )
        }

        removePreviousStatusLine()
        lockUntilMotorsFree()
    }

    fun lockUntilMotorsFree(maximumTimeMillis: Long = 500L)
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
            if (!opModeIsActive())
            {
                break
            }

            sleep(50L)
        }
    }

    fun runRotationPID(target: Double, block: (Double) -> Unit)
    {
        if (target == 0.0)
        {
            return
        }

        var orientation: YawPitchRollAngles
        var angularVelocity = imu.getRobotAngularVelocity(AngleUnit.DEGREES)

        var yaw: Double

        var velocity = angularVelocity.zRotationRate
        var rampUp: Double

        val startTime = System.currentTimeMillis()
        var totalError = 0.0

        var error = target

        stopAndResetMotors()
        runMotors()

        while (
            (error.absoluteValue > AutoPipelineUtilities.ROTATION_END_YAW ||
                    velocity.absoluteValue > AutoPipelineUtilities.ROTATION_END_VELOCITY)
        )
        {
            if (!opModeIsActive())
            {
                break
            }

            orientation = imu.robotYawPitchRollAngles
            angularVelocity = imu.getRobotAngularVelocity(AngleUnit.DEGREES)
            yaw = orientation.getYaw(AngleUnit.DEGREES)
            velocity = angularVelocity.zRotationRate
            error = DegreeUtilities.degFrom(yaw, target)
            totalError += error

            rampUp = ((System.currentTimeMillis() - startTime) / AutoPipelineUtilities.ROTATION_RAMP_UP_TIME).coerceAtMost(1.0)

            val rawPower = (AutoPipelineUtilities.PID_ROTATION_KP * error
                    + AutoPipelineUtilities.PID_ROTATION_KD * velocity /*+
                        AutoPipelineUtilities.PID_ROTATION_KI * totalError*/)
            val power = Math.min(1.0, Math.max(-1.0, rawPower))

            var previousTelemetryLine: Line? = null
            fun removePreviousStatusLine()
            {
                if (previousTelemetryLine != null)
                {
                    telemetry.removeLine(previousTelemetryLine)
                }
            }

            removePreviousStatusLine()
            previousTelemetryLine = telemetry.addLine(
                "Current Yaw: ${"%.3f".format(yaw.toFloat())} | " +
                        "Error: ${"%.3f".format(error.toFloat())} | " +
                        "Velocity: ${"%.3f".format(velocity)} | " +
                        "Ramp Up: ${"%.3f".format(rampUp)} | " +
                        "Power: ${"%.3f".format(power)} | " +
                        "Raw Power: ${"%.3f".format(rawPower)} | " +
                        "FInal Power: ${"%.3f".format(rampUp * power)} | " +
                        "Stop req: ${opModeIsActive()} | "
            )
            telemetry.update()

            block(-(rampUp * power))
        }

        lockUntilMotorsFree()
    }

    fun forward(target: Double) = runMovementPID(target, ::setPower)
    fun strafe(target: Double) = runMovementPID(target, ::setStrafePower)
    fun turn(target: Double) = runRotationPID(target, ::setTurnPower)

    fun terminateAllMotors() = configureMotorsToDo(HardwareDevice::close)

    fun stopAndResetMotors() = configureMotorsToDo {
        it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    }

    fun runMotors() = configureMotorsToDo {
        it.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
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
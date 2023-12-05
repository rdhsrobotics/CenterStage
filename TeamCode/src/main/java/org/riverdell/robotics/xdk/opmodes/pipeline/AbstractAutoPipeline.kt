package org.riverdell.robotics.xdk.opmodes.pipeline

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.DistanceSensor
import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.IMU
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.RootExecutionGroup
import io.liftgate.robotics.mono.subsystem.Subsystem
import org.firstinspires.ftc.robotcore.external.Telemetry.Line
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles
import org.riverdell.robotics.xdk.opmodes.pipeline.contexts.DrivebaseContext
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TapeSide
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TeamColor
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.VisionPipeline
import org.riverdell.robotics.xdk.opmodes.pipeline.pid.PIDController
import org.riverdell.robotics.xdk.opmodes.pipeline.utilities.AutoPipelineUtilities
import org.riverdell.robotics.xdk.opmodes.pipeline.utilities.DegreeUtilities
import org.riverdell.robotics.xdk.opmodes.subsystem.AirplaneLauncher
import org.riverdell.robotics.xdk.opmodes.subsystem.Elevator
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ExtendableClaw
import kotlin.math.absoluteValue
import kotlin.math.sign

abstract class AbstractAutoPipeline : LinearOpMode(), io.liftgate.robotics.mono.subsystem.System
{
    override val subsystems = mutableSetOf<Subsystem>()

    private val frontRight by lazy { hardware<DcMotor>("frontRight") }
    private val frontLeft by lazy { hardware<DcMotor>("frontLeft") }

    private val backRight by lazy { hardware<DcMotor>("backRight") }
    private val backLeft by lazy { hardware<DcMotor>("backLeft") }

    internal val elevatorSubsystem by lazy { Elevator(this) }
    internal val clawSubsystem by lazy { ExtendableClaw(this) }
    internal val airplaneSubsystem by lazy { AirplaneLauncher(this) }

    internal var monoShouldDoLogging = true

    lateinit var imu: IMU

    internal val visionPipeline by lazy {
        VisionPipeline(
            webcam = hardware("webcam1"),
            teamColor = getTeamColor()
        )
    }

    abstract fun getTeamColor(): TeamColor

    abstract fun buildExecutionGroup(tapeSide: TapeSide): RootExecutionGroup

    override fun runOpMode()
    {
        frontDistanceSensor

        this.imu = hardware("imu")
        this.imu.initialize(
            IMU.Parameters(
                RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                    RevHubOrientationOnRobot.UsbFacingDirection.UP
                )
            )
        )

        visionPipeline.start()

        register(
            clawSubsystem,
            elevatorSubsystem,
            airplaneSubsystem
        )

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

        this.imu.resetYaw()
        this.clawSubsystem.toggleExtender(ExtendableClaw.ExtenderState.Deposit)

        while (!isStarted)
        {
            telemetry.addLine("Auto in initialized")
            telemetry.addData("Tape side", visionPipeline.getTapeSide())
        }

        val tapeSide = visionPipeline.getTapeSide()
        this.clawSubsystem.toggleExtender(ExtendableClaw.ExtenderState.Deposit)

        telemetry.addLine("Started! Executing the Mono execution group now with ${tapeSide.name}.")
        telemetry.update()

        val executionGroup = buildExecutionGroup(tapeSide)
        executionGroup.providesContext { _ ->
            DrivebaseContext(
                listOf(frontRight, frontLeft, backRight, backLeft), this
            )
        }
        executionGroup.executeBlocking()

        stopAndResetMotors()
        terminateAllMotors()

        visionPipeline.stop()
        disposeOfAll()

        Mono.logSink = { }
    }

    private val v2 = V2()
    fun v2() = v2

    inner class V2
    {
        // get all lynx modules so we can reset their caches later on
        private val lynxModules = hardwareMap.getAll(LynxModule::class.java)
        private val drivebaseMotors = listOf(frontLeft, frontRight, backLeft, backRight)

        private fun buildPIDControllerMovement(setPoint: Double) = PIDController(
            kP = AutoPipelineUtilities.PID_MOVEMENT_KP,
            kD = AutoPipelineUtilities.PID_MOVEMENT_KD,
            kI = AutoPipelineUtilities.PID_MOVEMENT_KI,
            setPoint = setPoint,
            setPointTolerance = AutoPipelineUtilities.PID_MOVEMENT_TOLERANCE,
            maxTotalError = AutoPipelineUtilities.PID_MOVEMENT_MAX_ERROR
        )

        private fun buildPIDControllerRotation(setPoint: Double) = PIDController(
            kP = AutoPipelineUtilities.PID_ROTATION_KP,
            kD = AutoPipelineUtilities.PID_ROTATION_KD,
            kI = AutoPipelineUtilities.PID_ROTATION_KI,
            setPoint = setPoint,
            setPointTolerance = AutoPipelineUtilities.PID_ROTATION_TOLERANCE,
            maxTotalError = AutoPipelineUtilities.PID_ROTATION_MAX_ERROR
        )

        private fun buildPIDControllerDistance(setPoint: Double) = PIDController(
            kP = AutoPipelineUtilities.PID_DISTANCE_KP,
            kD = AutoPipelineUtilities.PID_DISTANCE_KD,
            kI = AutoPipelineUtilities.PID_DISTANCE_KI,
            setPoint = setPoint,
            setPointTolerance = AutoPipelineUtilities.PID_DISTANCE_TOLERANCE,
            maxTotalError = AutoPipelineUtilities.PID_DISTANCE_MAX_ERROR
        )

        fun move(ticks: Double) = movementPID(
            setPoint = ticks,
            setMotorPowers = this@AbstractAutoPipeline::setPower
        )

        fun strafe(ticks: Double) = movementPID(
            setPoint = ticks,
            setMotorPowers = this@AbstractAutoPipeline::setStrafePower
        )

        fun turn(degrees: Double) = rotationPID(
            setPoint = degrees,
            setMotorPowers = this@AbstractAutoPipeline::setTurnPower
        )

        private fun shortestAngleDistance(target: Double, current: Double): Double
        {
            val diff = target - current

            return if (diff.absoluteValue <= 180)
                diff
            else
                360 - diff.absoluteValue
        }

        private fun rotationPID(
            setPoint: Double,
            setMotorPowers: (Double) -> Unit
        ) = driveBasePID(
            controller = buildPIDControllerRotation(setPoint = setPoint)
                .customErrorCalculator {
                    shortestAngleDistance(setPoint, it)
                },
            currentPositionBlock = {
                val imuResult = imu.robotYawPitchRollAngles
                val currentYaw = imuResult.getYaw(AngleUnit.DEGREES)

                if (currentYaw < 0)
                {
                    360 + currentYaw
                } else
                {
                    currentYaw
                }
            },
            setMotorPowers = setMotorPowers
        )

        private fun movementPID(
            setPoint: Double,
            setMotorPowers: (Double) -> Unit
        ) = driveBasePID(
            controller = buildPIDControllerMovement(setPoint = setPoint),
            currentPositionBlock = {
                drivebaseMotors
                    .map { it.currentPosition.absoluteValue }
                    .average()
            },
            setMotorPowers = setMotorPowers
        )

        private fun movementDistancePID(
            setPoint: Double,
            setMotorPowers: (Double) -> Unit
        ) = driveBasePID(
            controller = buildPIDControllerDistance(setPoint = setPoint),
            currentPositionBlock = {
                frontDistanceSensor.getDistance(DistanceUnit.CM)
            },
            setMotorPowers = setMotorPowers
        )

        private fun driveBasePID(
            controller: PIDController,
            currentPositionBlock: () -> Double,
            setMotorPowers: (Double) -> Unit
        )
        {
            stopAndResetMotors()
            runMotors()

            val startTime = System.currentTimeMillis()
            while (opModeIsActive())
            {
                // clear cached motor positions
                lynxModules.forEach(LynxModule::clearBulkCache)

                val realCurrentPosition = currentPositionBlock()
                if (controller.atSetPoint(realCurrentPosition))
                {
                    break
                }

                val millisDiff = System.currentTimeMillis() - startTime
                val rampUp = (millisDiff / AutoPipelineUtilities.RAMP_UP_SPEED)
                    .coerceIn(0.0, 1.0)

                setMotorPowers(
                    rampUp * -controller.calculate(realCurrentPosition)
                )
            }

            stopAndResetMotors()
            lockUntilMotorsFree()
        }
    }

    fun runMovementPID(target: Double, motorControl: (Double) -> Unit)
    {
        var averagePosition = 0.0
        var error = target
        var velocity = 100.0
        var previous: Double
        var integral = 0.0

        val startTime = System.currentTimeMillis()
        stopAndResetMotors()

        var previousTelemetryLine: Line? = null
        fun removePreviousStatusLine()
        {
            if (previousTelemetryLine != null)
            {
                telemetry.removeLine(previousTelemetryLine)
            }
        }

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

            val averageMotorPositions =
                (frontRightPos + frontLeftPos + backRightPos + backLeftPos) / 4.0
            previous = averagePosition

            averagePosition = averageMotorPositions * target.sign
            error = averagePosition - target
            velocity = averagePosition - previous
            integral += error

            val rawPidPower = ((AutoPipelineUtilities.PID_MOVEMENT_KP * error +
                        AutoPipelineUtilities.PID_MOVEMENT_KI - AutoPipelineUtilities.PID_MOVEMENT_KD * velocity))
                .coerceIn(-1.0..1.0)

            val millisDiff = System.currentTimeMillis() - startTime
            val rampUp = (millisDiff / AutoPipelineUtilities.RAMP_UP_SPEED).coerceIn(0.0, 1.0)

            removePreviousStatusLine()
            previousTelemetryLine = telemetry.addLine(
                "Current: ${"%.3f".format(averagePosition.toFloat())} | " +
                    "Previous: ${"%.3f".format(previous.toFloat())} | " +
                    "Error: ${"%.3f".format(error.toFloat())} | " +
                    "Velocity: ${"%.3f".format(velocity.toFloat())} | " +
                    "Integral: ${"%.3f".format(integral.toFloat())} |"
            )
            telemetry.update()


            motorControl(rampUp * -rawPidPower)
        }

        stopAndResetMotors()
        removePreviousStatusLine()
        lockUntilMotorsFree()
    }

    fun lockUntilMotorsFree(maximumTimeMillis: Long = 1500L)
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
                return
            }

            sleep(50L)
        }
    }

    fun runRotationPID(target: Double, block: (Double) -> Unit)
    {
        var orientation: YawPitchRollAngles
        var angularVelocity = imu.getRobotAngularVelocity(AngleUnit.DEGREES)

        var yaw: Double

        var velocity = angularVelocity.zRotationRate

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
                return
            }

            if (startTime + 5000L < System.currentTimeMillis())
            {
                break
            }

            orientation = imu.robotYawPitchRollAngles
            angularVelocity = imu.getRobotAngularVelocity(AngleUnit.DEGREES)
            yaw = orientation.getYaw(AngleUnit.DEGREES)
            velocity = angularVelocity.zRotationRate
            error = DegreeUtilities.degFrom(yaw, target)
            totalError += error

            val millisDiff = System.currentTimeMillis() - startTime
            val rampUp = (millisDiff / AutoPipelineUtilities.RAMP_UP_SPEED).coerceIn(0.0, 1.0)

            val rawPower = (AutoPipelineUtilities.PID_ROTATION_KP * error
                + AutoPipelineUtilities.PID_ROTATION_KD * velocity /*+
                        AutoPipelineUtilities.PID_ROTATION_KI * totalError*/)
            val power = rawPower.coerceIn(-1.0..1.0)

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

        stopAndResetMotors()
        lockUntilMotorsFree()
    }

    private val frontDistanceSensor by lazy { hardware<DistanceSensor>("frontsensor") }

    fun PIDToDistance(distanceTarget: Double)
    {
        var distance = 0.0
        var error = distanceTarget
        var velocity = 2.0
        var previous: Double
        var integral = 0.0

        val startTime = System.currentTimeMillis()
        stopAndResetMotors()

        var previousTelemetryLine: Line? = null
        fun removePreviousStatusLine()
        {
            if (previousTelemetryLine != null)
            {
                telemetry.removeLine(previousTelemetryLine)
            }
        }

        runMotors()

        while ((error.absoluteValue > 1.0 || velocity > 1.0))
        {
            if (!opModeIsActive())
            {
                return
            }

            if (startTime + 5000L < System.currentTimeMillis())
            {
                break
            }

            distance = frontDistanceSensor.getDistance(DistanceUnit.CM)
            previous = distance
            error = distance - distanceTarget
            velocity = distance - previous
            integral += error

            val rawPidPower = ((AutoPipelineUtilities.PID_DISTANCE_KP * error - AutoPipelineUtilities.PID_DISTANCE_KD * velocity))
                .coerceIn(-1.0..1.0)

            val millisDiff = System.currentTimeMillis() - startTime
            val rampUp = (millisDiff / AutoPipelineUtilities.RAMP_UP_SPEED).coerceIn(0.0, 1.0)

            removePreviousStatusLine()
            previousTelemetryLine = telemetry.addLine(
                "Current: ${"%.3f".format(distance.toFloat())} | " +
                    "Previous: ${"%.3f".format(previous.toFloat())} | " +
                    "Error: ${"%.3f".format(error.toFloat())} | " +
                    "Velocity: ${"%.3f".format(velocity.toFloat())} | " +
                    "Integral: ${"%.3f".format(integral.toFloat())} |" +
                    "Distance: ${"%.3f".format(distance.toFloat())} |"
            )
            telemetry.update()

            setPower(rampUp * rawPidPower)
        }

        stopAndResetMotors()
        removePreviousStatusLine()
        lockUntilMotorsFree()

    }

    fun forward(target: Double)
    {
        runMovementPID(target, ::setPower)
        Thread.sleep(450L)
    }

    fun strafe(target: Double)
    {
        runMovementPID(target, ::setStrafePower)
        sleep(450)
    }

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

    fun setStrafePower(_power: Double)
    {
        val power = _power * 0.425
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
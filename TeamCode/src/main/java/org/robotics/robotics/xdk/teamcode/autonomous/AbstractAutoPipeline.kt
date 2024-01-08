package org.robotics.robotics.xdk.teamcode.autonomous

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.DistanceSensor
import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.IMU
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.ExecutionGroup
import io.liftgate.robotics.mono.pipeline.RootExecutionGroup
import io.liftgate.robotics.mono.subsystem.Subsystem
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.robotics.robotics.xdk.teamcode.autonomous.contexts.BothClawFinger
import org.robotics.robotics.xdk.teamcode.autonomous.contexts.ExtenderContext
import org.robotics.robotics.xdk.teamcode.autonomous.contexts.LeftClawFinger
import org.robotics.robotics.xdk.teamcode.autonomous.contexts.RightClawFinger
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TapeSide
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TeamColor
import org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.PIDController
import org.robotics.robotics.xdk.teamcode.autonomous.detection.VisionPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.profiles.AutonomousProfile
import org.robotics.robotics.xdk.teamcode.autonomous.utilities.AutoPipelineUtilities
import org.robotics.robotics.xdk.teamcode.subsystem.AirplaneLauncher
import org.robotics.robotics.xdk.teamcode.subsystem.Elevator
import org.robotics.robotics.xdk.teamcode.subsystem.claw.ExtendableClaw
import kotlin.concurrent.thread
import kotlin.math.absoluteValue
import kotlin.math.sign

abstract class AbstractAutoPipeline(
    private val autonomousProfile: AutonomousProfile,
    private val teamColor: TeamColor = autonomousProfile.teamColor,
    internal val blockExecutionGroup: ExecutionGroup.(AbstractAutoPipeline, TapeSide) -> Unit = autonomousProfile.buildExecutionGroup()
) : LinearOpMode(), io.liftgate.robotics.mono.subsystem.System
{
    override val subsystems = mutableSetOf<Subsystem>()

    private val frontRight by lazy { hardware<DcMotor>("frontRight") }
    private val frontLeft by lazy { hardware<DcMotor>("frontLeft") }

    private val backRight by lazy { hardware<DcMotor>("backRight") }
    private val backLeft by lazy { hardware<DcMotor>("backLeft") }

    private val frontDistanceSensor by lazy { hardware<DistanceSensor>("frontSensor") }

    internal val elevatorSubsystem by lazy { Elevator(this) }
    internal val clawSubsystem by lazy { ExtendableClaw(this) }

    internal var monoShouldDoLogging = true

    private val airplaneSubsystem by lazy { AirplaneLauncher(this) }
    private lateinit var imu: IMU

    private val visionPipeline by lazy {
        VisionPipeline(
            webcam = hardware("webcam1"),
            teamColor = teamColor
        )
    }

    private val multipleTelemetry by lazy {
        MultipleTelemetry(
            this.telemetry,
            FtcDashboard.getInstance().telemetry
        )
    }

    override fun runOpMode()
    {
        register(
            clawSubsystem,
            elevatorSubsystem,
            airplaneSubsystem
        )

        this.imu = hardware("imu")
        this.imu.initialize(
            IMU.Parameters(
                RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD,
                    RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
                )
            )
        )

        visionPipeline.start()

        // keep all log entries
        if (monoShouldDoLogging)
        {
            Mono.logSink = {
                multipleTelemetry.addLine("[Mono] $it")
                multipleTelemetry.update()
            }
        }

        frontLeft.direction = DcMotorSimple.Direction.REVERSE
        frontLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        frontRight.direction = DcMotorSimple.Direction.FORWARD
        frontRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        backLeft.direction = DcMotorSimple.Direction.REVERSE
        backLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        backRight.direction = DcMotorSimple.Direction.FORWARD
        backRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        stopAndResetMotors()

        multipleTelemetry.addLine("Waiting for start. Started detection...")
        multipleTelemetry.update()

        initializeAll()

        var i = 0
        while (opModeInInit())
        {
            clawSubsystem.periodic()

            multipleTelemetry.addLine("Auto in initialized")
            multipleTelemetry.addData("Tape side", visionPipeline.getTapeSide())
            multipleTelemetry.addData("Iteration", i++)

            multipleTelemetry.addLine("=== PID Tuning Graph Outputs ===")
            multipleTelemetry.addData("Error", 0.0)
            multipleTelemetry.addData("Target", 0.0)
            multipleTelemetry.addData("Input", 0.0)
            multipleTelemetry.addData("Output", 0.0)
            multipleTelemetry.addData("Velocity", 0.0)
            multipleTelemetry.update()
        }

        waitForStart()
        val tapeSide = visionPipeline.getTapeSide()

        this.imu.resetYaw()

        val executionGroup = Mono.buildExecutionGroup {
            providesContext { _ ->
                RightClawFinger(claw = clawSubsystem)
            }

            providesContext { _ ->
                LeftClawFinger(claw = clawSubsystem)
            }

            providesContext { _ ->
                BothClawFinger(claw = clawSubsystem)
            }

            providesContext { _ ->
                ExtenderContext(claw = clawSubsystem)
            }
        }

        executionGroup.apply {
            blockExecutionGroup(this@AbstractAutoPipeline, tapeSide)
        }

        thread(isDaemon = true) {
            while (!isStopRequested)
            {
                clawSubsystem.periodic()
            }
        }

        executionGroup.executeBlocking()

        stopAndResetMotors()
        terminateAllMotors()

        kotlin.runCatching {
            visionPipeline.stop()
        }
        disposeOfAll()

        Mono.logSink = { }
    }

    private val v2 by lazy(::V2)

    fun move(ticks: Double) = v2.move(-ticks)
    fun turn(ticks: Double) = v2.turn(ticks)
    fun strafe(ticks: Double) = v2.strafe(-ticks)

    inner class V2
    {
        private val drivebaseMotors by lazy {
            listOf(frontLeft, frontRight, backLeft, backRight)
        }

        /**
         * Creates a PID controller with the given constants for basic movement.
         */
        private fun buildPIDControllerMovement(setPoint: Double, maintainHeading: Boolean) = PIDController(
            kP = AutoPipelineUtilities.PID_MOVEMENT_KP,
            kD = AutoPipelineUtilities.PID_MOVEMENT_KD,
            kI = AutoPipelineUtilities.PID_MOVEMENT_KI,
            setPoint = setPoint,
            setPointTolerance = AutoPipelineUtilities.PID_MOVEMENT_TOLERANCE,
            minimumVelocity = AutoPipelineUtilities.PID_MOVEMENT_MIN_VELOCITY,
            telemetry = multipleTelemetry,
            maintainHeading = maintainHeading,
            maintainHeadingValue = imu.robotYawPitchRollAngles.getYaw(AngleUnit.DEGREES)
        )

        /**
         * Creates a PID controller with the given constants for robot rotation.
         */
        private fun buildPIDControllerRotation(setPoint: Double) = PIDController(
            kP = AutoPipelineUtilities.PID_ROTATION_KP,
            kD = AutoPipelineUtilities.PID_ROTATION_KD,
            kI = AutoPipelineUtilities.PID_ROTATION_KI,
            setPoint = setPoint,
            setPointTolerance = AutoPipelineUtilities.PID_ROTATION_TOLERANCE,
            minimumVelocity = AutoPipelineUtilities.PID_ROTATION_MIN_VELOCITY,
            telemetry = multipleTelemetry
        )

        /**
         * Creates a PID controller with the given constants for PID with distance sensor.
         */
        private fun buildPIDControllerDistance(setPoint: Double) = PIDController(
            kP = AutoPipelineUtilities.PID_DISTANCE_KP,
            kD = AutoPipelineUtilities.PID_DISTANCE_KD,
            kI = AutoPipelineUtilities.PID_DISTANCE_KI,
            setPoint = setPoint,
            setPointTolerance = AutoPipelineUtilities.PID_DISTANCE_TOLERANCE,
            minimumVelocity = AutoPipelineUtilities.PID_DISTANCE_MIN_VELOCITY,
            telemetry = multipleTelemetry
        )

        /**
         * Moves forward/backward with a value of [ticks] using the movement
         * PID. Uses the IMU for automatic heading correction.
         */
        fun move(ticks: Double) = movementPID(
            setPoint = ticks,
            setMotorPowers = { left, right ->
                setRelativePowers(left, right)
            }
        )

        fun moveUntilDistanceReached(distance: Double) = movementDistancePID(
            setPoint = distance,
            setMotorPowers = this@AbstractAutoPipeline::setPower
        )

        /**
         * Moves left/right with a value of [ticks] using the movement
         * PID.
         */
        fun strafe(ticks: Double) = movementPID(
            setPoint = ticks,
            setMotorPowers = { left, right ->
                setRelativeStrafePower(0.7 * left, 0.7 * right)
            },
            requiresRelativeSign = true,
            maintainHeading = false
        )

        /**
         * Rotates the robot to [degrees] degrees relative to the heading at start
         * using the rotation PID.
         */
        fun turn(degrees: Double) = rotationPID(
            setPoint = degrees,
            setMotorPowers = this@AbstractAutoPipeline::setTurnPower
        )

        /**
         * Gets the shortest angle required to turn
         * to meet a given target [target].
         */
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
                }
                .customVelocityCalculator {
                    imu.getRobotAngularVelocity(AngleUnit.DEGREES)
                        .zRotationRate.toDouble()
                },
            currentPositionBlock = imu::normalizedYaw,
            setMotorPowers = { _, power ->
                setMotorPowers(power)
            }
        )

        private fun movementPID(
            setPoint: Double,
            setMotorPowers: (Double, Double) -> Unit,
            requiresRelativeSign: Boolean = true,
            maintainHeading: Boolean = true
        ) = driveBasePID(
            controller = buildPIDControllerMovement(setPoint = setPoint, maintainHeading = maintainHeading),
            currentPositionBlock = {
                (if (requiresRelativeSign) setPoint.sign.toInt() else 1) * drivebaseMotors
                    .map {
                        it.currentPosition.absoluteValue
                    }
                    .average()
            },
            setMotorPowers = { controller, positionOutput ->
                if (!controller.maintainHeading)
                {
                    setMotorPowers(positionOutput, positionOutput)
                    return@driveBasePID
                }

                val headingError = controller.maintainHeadingValue - controller.currentRobotHeading
                val turnCorrectionFactor = AutoPipelineUtilities.PID_MOVEMENT_TURN_CORRECTION_FACTOR * headingError

                val leftMotorPower = positionOutput + turnCorrectionFactor
                val rightMotorPower = positionOutput - turnCorrectionFactor

                setMotorPowers(leftMotorPower, rightMotorPower)
            }
        )

        private fun movementDistancePID(
            setPoint: Double,
            setMotorPowers: (Double) -> Unit
        ) = driveBasePID(
            controller = buildPIDControllerDistance(setPoint = setPoint),
            currentPositionBlock = {
                frontDistanceSensor.getDistance(DistanceUnit.CM)
            },
            setMotorPowers = { _, positionOutput ->
                setMotorPowers(positionOutput)
            }
        )

        private fun driveBasePID(
            controller: PIDController,
            currentPositionBlock: () -> Double,
            setMotorPowers: (PIDController, Double) -> Unit
        )
        {
            stopAndResetMotors()
            runMotors()

            val startTime = System.currentTimeMillis()
            while (opModeIsActive())
            {
                // clear cached motor positions
                val realCurrentPosition = currentPositionBlock()
                val imuHeading = imu.robotYawPitchRollAngles.getYaw(AngleUnit.DEGREES)

                if (controller.atSetPoint(realCurrentPosition))
                {
                    break
                }

                if (System.currentTimeMillis() - startTime > 3000L)
                {
                    break
                }

                val millisDiff = System.currentTimeMillis() - startTime
                val rampUp = (millisDiff / AutoPipelineUtilities.RAMP_UP_SPEED)
                    .coerceIn(0.0, 1.0)

                val pid = controller.calculate(realCurrentPosition, imuHeading)
                val finalPower = pid.coerceIn(-1.0..1.0)
                setMotorPowers(controller, rampUp * finalPower)

                multipleTelemetry.update()
            }

            stopAndResetMotors()
            lockUntilMotorsFree()
        }
    }

    private fun lockUntilMotorsFree(maximumTimeMillis: Long = 1500L)
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

    private fun terminateAllMotors() = configureMotorsToDo(HardwareDevice::close)
    private fun stopAndResetMotors() = configureMotorsToDo {
        it.power = 0.0
        it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    }

    private fun runMotors() = configureMotorsToDo {
        it.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    private fun setPower(power: Double) = configureMotorsToDo {
        it.power = power
    }

    private fun setRelativePowers(left: Double, right: Double)
    {
        frontLeft.power = left
        frontRight.power = right

        backLeft.power = left
        backRight.power = right
    }

    private fun setTurnPower(power: Double)
    {
        frontLeft.power = power
        frontRight.power = -power

        backLeft.power = power
        backRight.power = -power
    }

    private fun setRelativeStrafePower(left: Double, right: Double)
    {
        frontLeft.power = left
        frontRight.power = -right

        backLeft.power = -left
        backRight.power = right
    }

    private fun configureMotorsToDo(consumer: (DcMotor) -> Unit)
    {
        listOf(frontRight, frontLeft, backRight, backLeft).forEach(consumer::invoke)
    }
}
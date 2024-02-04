package org.robotics.robotics.xdk.teamcode.autonomous.controlsystem

import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.ElapsedTime
import io.liftgate.robotics.mono.pipeline.RootExecutionGroup
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.odometry.Pose
import org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.safety.EvictingSample
import org.robotics.robotics.xdk.teamcode.autonomous.hardware
import org.robotics.robotics.xdk.teamcode.autonomous.normalizedYaw
import org.robotics.robotics.xdk.teamcode.autonomous.utilities.AutoPipelineUtilities
import org.robotics.robotics.xdk.teamcode.subsystem.motionprofile.AsymmetricMotionProfile
import org.robotics.robotics.xdk.teamcode.subsystem.motionprofile.ProfileConstraints
import kotlin.math.absoluteValue
import kotlin.math.sign

class MovementHandler(private val opMode: AbstractAutoPipeline, private val executionGroup: RootExecutionGroup)
{
    private val pose = Pose()

    private val lateralPod = runCatching {
        MotorEx(opMode.hardwareMap, "lateralPod").encoder
    }.getOrNull()

    private val horizontalPod = runCatching {
        MotorEx(opMode.hardwareMap, "horizontalPod").encoder
    }.getOrNull()

    /**
     * Creates a PID controller with the given constants for basic movement.
     */
    private fun buildPIDControllerMovement(
        setPoint: Double, maintainHeading: Boolean,
        maintainHeadingValue: Double = opMode.drivebase.getIMUYawPitchRollAngles().getYaw(AngleUnit.DEGREES)
    ) = PIDController(
        kP = AutoPipelineUtilities.PID_MOVEMENT_KP,
        kD = AutoPipelineUtilities.PID_MOVEMENT_KD,
        kI = AutoPipelineUtilities.PID_MOVEMENT_KI,
        setPoint = setPoint,
        setPointTolerance = AutoPipelineUtilities.PID_MOVEMENT_TOLERANCE,
        minimumVelocity = AutoPipelineUtilities.PID_MOVEMENT_MIN_VELOCITY,
        telemetry = opMode.multipleTelemetry,
        maintainHeading = maintainHeading,
        maintainHeadingValue = maintainHeadingValue
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
        telemetry = opMode.multipleTelemetry
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
        telemetry = opMode.multipleTelemetry
    )

    /**
     * Moves forward/backward with a value of [ticks] using the movement
     * PID. Uses the IMU for automatic heading correction.
     */
    fun move(ticks: Double, heading: Double) = movementPID(
        setPoint = ticks,
        setMotorPowers = { left, right ->
            opMode.setRelativePowers(left, right)
        },
        maintainHeading = true,
        maintainHeadingValue = heading
    )

    fun moveUntilDistanceReached(distance: Double) = movementDistancePID(
        setPoint = distance,
        setMotorPowers = opMode::setPower
    )

    /**
     * Moves left/right with a value of [ticks] using the movement
     * PID.
     */
    fun strafe(ticks: Double) = movementPID(
        setPoint = ticks,
        setMotorPowers = { left, right ->
            opMode.setRelativeStrafePower(left, right)
        },
        requiresRelativeSign = true,
        maintainHeading = false,
        maintainHeadingValue = 0.0
    )

    /**
     * Rotates the robot to [degrees] degrees relative to the heading at start
     * using the rotation PID.
     */
    fun turn(degrees: Double) = rotationPID(
        setPoint = degrees,
        setMotorPowers = opMode::setTurnPower
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
                opMode.drivebase.backingImu().getRobotAngularVelocity(AngleUnit.DEGREES)
                    .zRotationRate.toDouble()
            },
        currentPositionBlock = opMode.drivebase.backingImu()::normalizedYaw,
        setMotorPowers = { _, power ->
            setMotorPowers(power)
        }
    )

    private fun movementPID(
        setPoint: Double,
        setMotorPowers: (Double, Double) -> Unit,
        requiresRelativeSign: Boolean = true,
        maintainHeading: Boolean = true,
        maintainHeadingValue: Double
    )
    {
        val rotationPid = buildPIDControllerRotation(maintainHeadingValue)

        return driveBasePID(
            controller = buildPIDControllerMovement(
                setPoint = setPoint, maintainHeading = maintainHeading,
                maintainHeadingValue = maintainHeadingValue
            ),
            currentPositionBlock = {
                (if (requiresRelativeSign) setPoint.sign.toInt() else 1) * opMode.drivebase.allDriveBaseMotors
                    .map {
                        it.currentPosition.absoluteValue
                    }
                    .average()
            },
            setMotorPowers = { controller, positionOutput ->
                setMotorPowers(positionOutput, positionOutput)
                if (!controller.maintainHeading)
                {
                    setMotorPowers(positionOutput, positionOutput)
                    return@driveBasePID
                }

                val yaw = opMode.drivebase.getIMUYawPitchRollAngles().getYaw(AngleUnit.DEGREES)
                if (rotationPid.atSetPoint(yaw))
                {
                    setMotorPowers(positionOutput, positionOutput)
                    return@driveBasePID
                }

                val turnCorrectionFactor = rotationPid.calculate(yaw, yaw)

                val leftMotorPower = positionOutput + turnCorrectionFactor
                val rightMotorPower = positionOutput - turnCorrectionFactor

                setMotorPowers(leftMotorPower, rightMotorPower)
            }
        )
    }

    private fun movementDistancePID(
        setPoint: Double,
        setMotorPowers: (Double) -> Unit
    ) = driveBasePID(
        controller = buildPIDControllerDistance(setPoint = setPoint),
        currentPositionBlock = {
            opMode.frontDistanceSensor?.getDistance(DistanceUnit.CM) ?: 0.0
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
        opMode.stopAndResetMotors()
        opMode.runMotors()

        val startTime = System.currentTimeMillis()
        var hasResultedInCanContinue = false

        val evictingSample = EvictingSample(
            size = 10,
            requiredSampleTime = 500L
        )

        val motionProfile = AsymmetricMotionProfile(
            0.0,
            controller.setPoint,
            ProfileConstraints(
                AutoPipelineUtilities.MOTION_PROFILE_VELOCITY,
                AutoPipelineUtilities.MOTION_PROFILE_ACCEL,
                AutoPipelineUtilities.MOTION_PROFILE_DECEL
            )
        )

        val elapsedTime = ElapsedTime()
        while (true)
        {
            if (opMode.isStopRequested)
            {
                executionGroup.terminateMidExecution()
                return
            }

            val realCurrentPosition = currentPositionBlock()
            val imuHeading = opMode.drivebase.getIMUYawPitchRollAngles().getYaw(AngleUnit.DEGREES)

            if (!hasResultedInCanContinue)
            {
                evictingSample.submit(imuHeading)
                when (evictingSample.analyze())
                {
                    EvictingSample.AnalyzeResult.RequiresExit ->
                    {
                        executionGroup.terminateMidExecution()
                        return
                    }
                    EvictingSample.AnalyzeResult.CanContinue ->
                    {
                        hasResultedInCanContinue = true
                    }
                    EvictingSample.AnalyzeResult.NotNeeded -> { }
                }
            }

            if (controller.atSetPoint(realCurrentPosition))
            {
                break
            }

            if (System.currentTimeMillis() - startTime > 3000L)
            {
                break
            }

            val motionProfileValue = motionProfile.calculate(elapsedTime.time())
            val pid = controller.calculate(realCurrentPosition, imuHeading)
            val finalPower = pid.coerceIn(-1.0..1.0)

            setMotorPowers(
                controller,
                (finalPower * motionProfileValue.v)
                    .coerceIn(-1.0..1.0)
            )

            opMode.multipleTelemetry.update()
        }

        opMode.stopAndResetMotors()
        opMode.lockUntilMotorsFree()
    }
}
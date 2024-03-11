package org.robotics.robotics.xdk.teamcode.autonomous.controlsystem

import io.liftgate.robotics.mono.pipeline.RootExecutionGroup
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.normalizedYaw
import org.robotics.robotics.xdk.teamcode.autonomous.utilities.AutoPipelineUtilities
import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sqrt

class MovementHandler(private val opMode: AbstractAutoPipeline, private val executionGroup: RootExecutionGroup)
{
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
            /*.customVelocityCalculator {
                opMode.drivebase.backingImu().getRobotAngularVelocity(AngleUnit.DEGREES)
                    .zRotationRate.toDouble()
            }*/,
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
        var previousLoopTime = 0L

        while (true)
        {
            if (opMode.isStopRequested)
            {
                executionGroup.terminateMidExecution()
                return
            }


            val thing = System.currentTimeMillis()

            val realCurrentPosition = currentPositionBlock()
            val imuHeading = opMode.drivebase.getIMUYawPitchRollAngles().getYaw(AngleUnit.DEGREES)

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

            val pid = controller.calculate(realCurrentPosition, imuHeading, previousLoopTime)
            val finalPower = pid.coerceIn(-1.0..1.0)
            setMotorPowers(controller, rampUp * finalPower)

            previousLoopTime = System.currentTimeMillis() - thing
        }

        opMode.stopAndResetMotors()
        opMode.lockUntilMotorsFree()
    }

    fun relocalize(pose: AprilTagPoseFtc): Boolean
    {
        // Calculate the distance to the target pose
        val distanceToTarget = sqrt(pose.x.pow(2) + pose.y.pow(2))

        if (distanceToTarget <= AutoPipelineUtilities.APRIL_TAG_LOCALIZATION_THRESHOLD) {
            opMode.drivebase.backingDriveBase
                .driveRobotCentric(0.0, 0.0, 0.0)
            return true
        }

        val angleToTarget = atan2(pose.y, pose.x)
        val turnSpeed = angleToTarget * AutoPipelineUtilities.APRIL_TAG_TURN_FACTOR

        val forwardSpeed = AutoPipelineUtilities.APRIL_TAG_FORWARD_FACTOR * pose.y
        val strafeSpeed = AutoPipelineUtilities.APRIL_TAG_STRAFE_FACTOR * pose.x

        opMode.telemetry.addLine("=== Relocalizing ===")
        opMode.telemetry.addData("Forward speed", forwardSpeed)
        opMode.telemetry.addData("Strafe speed", strafeSpeed)
        opMode.telemetry.addData("Turn speed", turnSpeed)

        opMode.telemetry.addData("Distance to Target", distanceToTarget)
        opMode.telemetry.addData("Angle to Target", angleToTarget)
        opMode.telemetry.update()

        opMode.drivebase.backingDriveBase.driveRobotCentric(strafeSpeed, forwardSpeed, turnSpeed)
        return false
    }
}
package org.robotics.robotics.xdk.teamcode.autonomous.controlsystem

import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.absoluteValue

/**
 * An implementation of a PID controller.
 */
class PIDController(
    private val kP: Double,
    private val kI: Double,
    private val kD: Double,
    val setPoint: Double,
    private val setPointTolerance: Double,
    private val minimumVelocity: Double,
    private val telemetry: Telemetry,
    val maintainHeading: Boolean = false,
    val maintainHeadingValue: Double = 0.0,
)
{
    private var integral = 0.0
    private var previousError = 0.0
    private var totalError = 0.0
    private var previousValue: Double? = null
    private var velocity = 0.0

    private var customErrorCalculator: ((current: Double) -> Double)? = null
    private var customVelocityCalculator: (() -> Double)? = null

    fun customErrorCalculator(block: (current: Double) -> Double) =
        apply { customErrorCalculator = block }

    fun customVelocityCalculator(block: () -> Double) =
        apply { customVelocityCalculator = block }

    /**
     * Calculates the current power based on the
     * given input and robot heading.
     */
    fun calculate(currentValue: Double, velocityMultiplier: Long = 1L): Double
    {
        val error = customErrorCalculator?.invoke(currentValue)
            ?: (setPoint - currentValue)

        val prevValue = previousValue ?: currentValue
        val derivative = error - previousError

        integral += error

        val output = (kP * error) + (kI * integral) + (kD * derivative)

        telemetry.addData("Error", error)
        telemetry.addData("Target", setPoint)
        telemetry.addData("Output", output)
        telemetry.addData("Input", currentValue)
        telemetry.addData("Velocity", velocity)
        telemetry.update()

        previousError = error
        totalError += error

        velocity = (customVelocityCalculator?.invoke()
            ?: ((prevValue - currentValue) / velocityMultiplier))
        previousValue = currentValue

        return output
    }

    fun atSetPoint(currentValue: Double): Boolean
    {
        return velocity.absoluteValue < minimumVelocity && (
                (customErrorCalculator?.let { it(currentValue) } ?: (setPoint - currentValue)).absoluteValue < setPointTolerance
        )
    }
}

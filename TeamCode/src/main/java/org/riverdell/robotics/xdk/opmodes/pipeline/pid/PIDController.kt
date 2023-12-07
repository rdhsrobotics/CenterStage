package org.riverdell.robotics.xdk.opmodes.pipeline.pid

import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.absoluteValue

class PIDController(
    private val kP: Double,
    private val kI: Double,
    private val kD: Double,
    val setPoint: Double,
    private val setPointTolerance: Int,
    private val maxTotalError: Int,
    private val telemetry: Telemetry
)
{
    private var integral = 0.0
    private var previousError = 0.0
    private var totalError = 0.0

    private var customErrorCalculator: ((current: Double) -> Double)? = null

    fun customErrorCalculator(block: (current: Double) -> Double) =
        apply { customErrorCalculator = block }

    fun calculate(currentValue: Double): Double {
        val error = customErrorCalculator?.invoke(currentValue)
            ?: (setPoint - currentValue)

        telemetry.addData("error", error)
        integral += error
        val derivative = error - previousError

        val output = (kP * error) + (kI * integral) + (kD * derivative)

        previousError = error
        totalError += error

        return output
    }

    fun atSetPoint(currentValue: Double): Boolean
    {
        return /*(totalError.absoluteValue <= maxTotalError) ||*/ (setPoint - setPointTolerance <= currentValue && currentValue <= setPoint + setPointTolerance)
    }
}

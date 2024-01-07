package org.robotics.robotics.xdk.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.IMU
import io.liftgate.robotics.mono.Mono
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import java.lang.IllegalStateException
import java.util.concurrent.TimeUnit

/**
 * An extension function of [LinearOpMode] to get a [HardwareDevice] [T] from the hardware
 * map. An exception is thrown if the hardware device does not exist in the hardware map.
 */
inline fun <reified T : HardwareDevice> LinearOpMode.hardware(id: String) = runCatching {
    hardwareMap.get(id) as T
}.getOrNull() ?: throw IllegalStateException("$id is null")

/**
 * Normalize the robot heading given from the [IMU] from
 * a [-180, 180] range to a [0, 360] range.
 */
fun IMU.normalizedYaw(): Double
{
    val imuResult = robotYawPitchRollAngles
    val currentYaw = imuResult.getYaw(AngleUnit.DEGREES)

    return if (currentYaw < 0)
    {
        360 + currentYaw
    } else
    {
        currentYaw
    }
}

/**
 * Run a delayed task asynchronously on the Mono executor pool.
 */
fun scheduleAsyncExecution(inMillis: Long, block: () -> Unit) = Mono.EXECUTION.schedule(block, inMillis, TimeUnit.MILLISECONDS)

/**
 * Runs a repeating task asynchrounuosly on the Mono executor pool.
 */
fun runRepeating(everyMillis: Long, block: () -> Unit) =
    Mono.EXECUTION.scheduleAtFixedRate(block, 0L, everyMillis, TimeUnit.MILLISECONDS)
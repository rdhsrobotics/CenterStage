package org.riverdell.robotics.xdk.opmodes.pipeline

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareDevice
import io.liftgate.robotics.mono.Mono
import java.util.concurrent.TimeUnit

/**
 * A little extension function to get a non-null [HardwareDevice].
 *
 * @author Subham
 * @since 10/23/2023
 */
inline fun <reified T : HardwareDevice> LinearOpMode.hardware(id: String) = hardwareMap.get(id) as T

fun scheduleAsyncExecution(inMillis: Long, block: () -> Unit) = Mono.EXECUTION.schedule(block, inMillis, TimeUnit.MILLISECONDS)
package org.riverdell.robotics.xdk.opmodes.opmodes.pipeline

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareDevice

/**
 * A little extension function to get a non-null [HardwareDevice].
 *
 * @author Subham
 * @since 10/23/2023
 */
inline fun <reified T : HardwareDevice> LinearOpMode.hardware(id: String) = hardwareMap.get(id) as T
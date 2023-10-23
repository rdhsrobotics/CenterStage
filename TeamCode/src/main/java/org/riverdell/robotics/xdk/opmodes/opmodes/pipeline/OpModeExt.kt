package org.riverdell.robotics.xdk.opmodes.opmodes.pipeline

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareDevice

inline fun <reified T : HardwareDevice> LinearOpMode.hardware(id: String) = hardwareMap.get(id) as T
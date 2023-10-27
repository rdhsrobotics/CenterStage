package org.riverdell.robotics.xdk.opmodes.subsystem

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx

fun DcMotorEx.stopAndResetEncoder()
{
    mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
}
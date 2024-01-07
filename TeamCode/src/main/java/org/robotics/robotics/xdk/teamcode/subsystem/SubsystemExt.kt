package org.robotics.robotics.xdk.teamcode.subsystem

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx

/**
 * Stops and resets the encoder for a [DcMotorEx].
 * @author Subham
 */
fun DcMotorEx.stopAndResetEncoder()
{
    mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
}
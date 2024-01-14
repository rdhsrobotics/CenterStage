package org.robotics.robotics.xdk.teamcode

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.robotics.robotics.xdk.teamcode.subsystem.Drivebase

/**
 * A TeleOp implementation which drives a mecanum drivebase
 * with field-centric controls using the built-in IMU.
 *
 * @author Subham
 */
//@Disabled
@TeleOp(name = "Prod | Field Centric")
class FieldCentricTeleOp : AbstractTeleOp()
{
    override fun driveRobot(
        drivebase: Drivebase,
        driverOp: GamepadEx,
        multiplier: Double
    )
    {
        drivebase.driveFieldCentric(driverOp, multiplier)
    }
}
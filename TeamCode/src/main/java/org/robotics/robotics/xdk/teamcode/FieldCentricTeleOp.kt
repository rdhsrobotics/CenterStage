package org.robotics.robotics.xdk.teamcode

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.robotics.robotics.xdk.teamcode.subsystem.Drivebase

@Disabled
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
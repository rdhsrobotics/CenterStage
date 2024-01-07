package org.robotics.robotics.xdk.asd

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.robotics.robotics.xdk.asd.subsystem.Drivebase

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
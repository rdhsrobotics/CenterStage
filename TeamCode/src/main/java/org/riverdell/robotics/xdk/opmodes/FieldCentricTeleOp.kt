package org.riverdell.robotics.xdk.opmodes

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.riverdell.robotics.xdk.opmodes.subsystem.Drivebase

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
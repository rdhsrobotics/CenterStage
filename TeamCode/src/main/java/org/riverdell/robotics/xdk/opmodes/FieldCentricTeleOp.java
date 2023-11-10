package org.riverdell.robotics.xdk.opmodes;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.jetbrains.annotations.NotNull;
import org.riverdell.robotics.xdk.opmodes.subsystem.Drivebase;

@TeleOp(name = "Prod | Field Centric")
public class FieldCentricTeleOp extends AbstractTeleOpOpMode {

    @Override
    public void driveRobot(
            @NotNull final Drivebase drivebase,
            @NotNull final GamepadEx driverOp,
            double multiplier
    ) {
        drivebase.driveFieldCentric(driverOp, multiplier);
    }
}

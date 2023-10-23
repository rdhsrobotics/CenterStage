package org.riverdell.robotics.xdk.opmodes.game.pipeline;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.riverdell.robotics.xdk.opmodes.game.AbstractHardwareAwareOpMode;

@TeleOp(name = "prod")
public class TestOpMode extends AbstractHardwareAwareOpMode {
    @Override
    public boolean isAutonomous() {
        return false;
    }
}

package org.riverdell.robotics.xdk.opmodes;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.riverdell.robotics.xdk.opmodes.subsystem.AirplaneLauncher;
import org.riverdell.robotics.xdk.opmodes.subsystem.Drivebase;
import org.riverdell.robotics.xdk.opmodes.subsystem.Elevator;

import io.liftgate.robotics.mono.Mono;
import io.liftgate.robotics.mono.gamepad.ButtonType;
import io.liftgate.robotics.mono.gamepad.GamepadCommands;
import kotlin.Unit;

/**
 * Configures Mono gamepad commands and FTCLib drive systems for TeleOp.
 *
 * @author Subham
 * @since 9/5/2023
 */
public abstract class AbstractTeleOpOpMode extends LinearOpMode {

    @MonotonicNonNull
    private GamepadCommands gp1Commands;

    @MonotonicNonNull
    private GamepadCommands gp2Commands;

    @MonotonicNonNull
    private AirplaneLauncher paperPlaneLauncher;

    @MonotonicNonNull
    private Drivebase drivebase;

    @MonotonicNonNull
    private Elevator elevator;

    @Override
    public void runOpMode() {
        this.drivebase = new Drivebase(this);
        this.drivebase.initialize();

        this.paperPlaneLauncher = new AirplaneLauncher(this);
        this.paperPlaneLauncher.initialize();

        this.elevator = new Elevator(this);
        this.elevator.initialize();

        gp1Commands = Mono.INSTANCE.commands(gamepad1);
        gp2Commands = Mono.INSTANCE.commands(gamepad2);

        final GamepadEx driverOp = new GamepadEx(gamepad1);
        buildCommands();

        telemetry.addLine("Configured commands. Waiting for start...");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            final double multiplier = 0.6 + (gamepad1.right_trigger * 0.4);
            drivebase.driveRobotCentric(driverOp, multiplier);
        }

        gp1Commands.stopListening();
        gp2Commands.stopListening();

        paperPlaneLauncher.dispose();
        drivebase.dispose();
    }

    private void buildCommands() {
        gp1Commands
                .where(ButtonType.ButtonY)
                .triggers(() -> {
                    this.paperPlaneLauncher.launch();
                    return Unit.INSTANCE;
                })
                .andIsHeldUntilReleasedWhere(() -> {
                    this.paperPlaneLauncher.reset();
                    return Unit.INSTANCE;
                });

        gp1Commands
                .where(ButtonType.ButtonX)
                .triggers(() -> {
                    this.elevator.elevateTo(5);
                    return Unit.INSTANCE;
                })
                .andIsHeldUntilReleasedWhere(() -> {
                    this.elevator.reset();
                    return Unit.INSTANCE;
                });

        gp1Commands.startListening();
        gp2Commands.startListening();
    }
}
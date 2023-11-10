package org.riverdell.robotics.xdk.opmodes;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jetbrains.annotations.NotNull;
import org.riverdell.robotics.xdk.opmodes.subsystem.AirplaneLauncher;
import org.riverdell.robotics.xdk.opmodes.subsystem.Drivebase;
import org.riverdell.robotics.xdk.opmodes.subsystem.Elevator;
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ExtendableClaw;

import io.liftgate.robotics.mono.Mono;
import io.liftgate.robotics.mono.gamepad.ButtonType;
import io.liftgate.robotics.mono.gamepad.GamepadCommands;
import kotlin.Unit;

/**
 * Configures Mono gamepad commands and FTCLib
 * drive systems for TeleOp.
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
    private Elevator elevator;
    @MonotonicNonNull
    private ExtendableClaw extendableClaw;

    public abstract void driveRobot(
            @NotNull final Drivebase drivebase,
            @NotNull final GamepadEx driverOp,
            final double multiplier
    );

    @Override
    public void runOpMode() {
        final Drivebase drivebase = new Drivebase(this);
        drivebase.initialize();

        this.paperPlaneLauncher = new AirplaneLauncher(this);
        this.paperPlaneLauncher.initialize();

        this.elevator = new Elevator(this);
        this.elevator.initialize();

        this.extendableClaw = new ExtendableClaw(this);
        this.extendableClaw.initialize();

        gp1Commands = Mono.INSTANCE.commands(gamepad1);
        gp2Commands = Mono.INSTANCE.commands(gamepad2);

        final GamepadEx driverOp = new GamepadEx(gamepad1);
        buildCommands();

        telemetry.addLine("Configured commands. Waiting for start...");
        telemetry.update();

        waitForStart();

        this.extendableClaw.toggleExtender(ExtendableClaw.ClawState.Deposit);

        while (opModeIsActive()) {
            final double multiplier = 0.6 + (gamepad1.right_trigger * 0.4);
            driveRobot(drivebase, driverOp, multiplier);

            extendableClaw.expandClaw(gamepad2.left_trigger);
            elevator.configureElevator(gamepad2.right_stick_y);
        }

        gp1Commands.stopListening();
        gp2Commands.stopListening();

        paperPlaneLauncher.dispose();
        elevator.dispose();

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

        gp2Commands
                .where(ButtonType.ButtonY)
                .triggers(() -> {
                    this.extendableClaw.toggleExtender();
                    return Unit.INSTANCE;
                })
                .whenPressedOnce();

        // extender expansion ranges
        gp2Commands
                .where(ButtonType.DPadDown)
                .triggers(() -> {
                    this.extendableClaw.decrementAddition();
                    return Unit.INSTANCE;
                })
                .whenPressedOnce();

        gp2Commands
                .where(ButtonType.DPadUp)
                .triggers(() -> {
                    this.extendableClaw.incrementAddition();
                    return Unit.INSTANCE;
                })
                .whenPressedOnce();

        // claw expansion
        gp2Commands
                .where(ButtonType.DPadLeft)
                .triggers(() -> {
                    this.extendableClaw.decrementClawAddition();
                    return Unit.INSTANCE;
                })
                .whenPressedOnce();

        gp2Commands
                .where(ButtonType.DPadRight)
                .triggers(() -> {
                    this.extendableClaw.incrementClawAddition();
                    return Unit.INSTANCE;
                })
                .whenPressedOnce();

        gp1Commands.startListening();
        gp2Commands.startListening();
    }
}
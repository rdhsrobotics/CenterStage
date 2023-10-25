package org.riverdell.robotics.xdk.opmodes.opmodes;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import java.util.concurrent.atomic.AtomicInteger;

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
    private Servo paperPlaneLauncher;

    @Override
    public void runOpMode() {
        final Motor backLeft = new Motor(hardwareMap, "backLeft");
        final Motor backRight = new Motor(hardwareMap, "backRight");
        final Motor frontLeft = new Motor(hardwareMap, "frontLeft");
        final Motor frontRight = new Motor(hardwareMap, "frontRight");

        this.paperPlaneLauncher = hardwareMap.get(Servo.class, "launcher");
        this.paperPlaneLauncher.setPosition(1.0);

        MecanumDrive driveBase = new MecanumDrive(frontLeft, frontRight,
                backLeft, backRight);

        gp1Commands = Mono.INSTANCE.commands(gamepad1);
        gp2Commands = Mono.INSTANCE.commands(gamepad2);

        final GamepadEx driverOp = new GamepadEx(gamepad1);

        buildCommands();
        telemetry.addLine("Configured commands. Waiting for start...");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            final double multiplier = 0.6 + (gamepad1.right_trigger * 0.4);

            driveBase.driveRobotCentric(
                    driverOp.getLeftX() * multiplier,
                    -driverOp.getLeftY() * multiplier,
                    driverOp.getRightX() * multiplier,
                    true
            );
        }

        gp1Commands.stopListening();
        gp2Commands.stopListening();
        driveBase.stop();
    }

    private void buildCommands() {
        gp1Commands
                .where(ButtonType.ButtonY)
                .triggers(() -> {
                    this.paperPlaneLauncher.setPosition(0.45);
                    return Unit.INSTANCE;
                })
                .andIsHeldUntilReleasedWhere(() -> {
                    this.paperPlaneLauncher.setPosition(1.0);
                    return Unit.INSTANCE;
                });

        gp1Commands.startListening();
        gp2Commands.startListening();
    }
}

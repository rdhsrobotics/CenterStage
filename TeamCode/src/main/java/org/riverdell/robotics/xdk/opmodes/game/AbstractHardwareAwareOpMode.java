package org.riverdell.robotics.xdk.opmodes.game;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import io.liftgate.ftc.scripting.opmode.DevLinearOpMode;
import io.liftgate.robotics.mono.Mono;
import io.liftgate.robotics.mono.gamepad.ButtonType;
import io.liftgate.robotics.mono.gamepad.GamepadCommands;
import kotlin.Unit;

/**
 * @author GrowlyX
 * @since 9/5/2023
 */
public abstract class AbstractHardwareAwareOpMode extends LinearOpMode {

    @MonotonicNonNull
    private GamepadCommands gp1Commands;

    @MonotonicNonNull
    private GamepadCommands gp2Commands;

    @MonotonicNonNull
    private MecanumDrive driveBase;

    public abstract boolean isAutonomous();

    @Override
    public void runOpMode() {
        final Motor frontLeft = new Motor(hardwareMap, "frontLeft");
        final Motor frontRight = new Motor(hardwareMap, "frontRight");
        final Motor backLeft = new Motor(hardwareMap, "backLeft");
        final Motor backRight = new Motor(hardwareMap, "backRight");

        driveBase = new MecanumDrive(frontLeft, frontRight,
                backLeft, backRight);

        gp1Commands = Mono.INSTANCE.commands(gamepad1);
        gp2Commands = Mono.INSTANCE.commands(gamepad2);

        if (!isAutonomous()) {
            buildCommands();

            while (opModeIsActive()) {
                final double multiplier = 0.3 + (gamepad1.right_trigger * 0.7);

                driveBase.driveRobotCentric(
                        gamepad1.left_stick_x * multiplier,
                        gamepad1.left_stick_y * multiplier,
                        gamepad1.right_stick_y * multiplier
                );
            }
        }

        if (!isAutonomous()) {
            gp1Commands.stopListening();
            gp2Commands.stopListening();
        }
    }

    private void buildCommands() {
        gp1Commands
                .where(ButtonType.ButtonX)
                .triggers(() -> {
                    System.out.println("hey :)");
                    return Unit.INSTANCE;
                })
                .whenPressedOnce();

        gp1Commands.startListening();
        gp2Commands.startListening();
    }

    /*@Override
    public @NotNull List<ImpliedVariable> getImpliedVariables() {
        return Collections.emptyList();
    }*/
}

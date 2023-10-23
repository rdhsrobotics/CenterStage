package org.riverdell.robotics.xdk.opmodes.game;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

//import io.liftgate.ftc.scripting.opmode.DevLinearOpMode;
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
        final Motor backLeft = new Motor(hardwareMap, "frontLeft");
        final Motor backRight = new Motor(hardwareMap, "frontRight");
        final Motor frontLeft = new Motor(hardwareMap, "backLeft");
        final Motor frontRight = new Motor(hardwareMap, "backRight");

        driveBase = new MecanumDrive(frontLeft, frontRight,
                backLeft, backRight);

        gp1Commands = Mono.INSTANCE.commands(gamepad1);
        gp2Commands = Mono.INSTANCE.commands(gamepad2);

        final GamepadEx driverOp = new GamepadEx(gamepad1);

        if (!isAutonomous()) {
            buildCommands();
            waitForStart();

            while (opModeIsActive()) {
                final double multiplier = 0.6 + (gamepad1.right_trigger * 0.4);

                driveBase.driveRobotCentric(
                        driverOp.getLeftX() * multiplier,
                        driverOp.getLeftY() * multiplier,
                        driverOp.getRightX() * multiplier,
                        true
                );
            }
        }

        if (!isAutonomous()) {
            gp1Commands.stopListening();
            gp2Commands.stopListening();
        }

        driveBase.stop();
    }

    private void buildCommands() {
        gp1Commands
                .where(ButtonType.ButtonX)
                .triggers(() -> {
                    telemetry.addLine("HII");
                    telemetry.addLine("HII");
                    telemetry.addLine("HII");
                    telemetry.addLine("HII");
                    telemetry.addLine("HII");
                    telemetry.addLine("HII");
                    telemetry.addLine("HII");
                    telemetry.addLine("HII");
                    telemetry.addLine("HII");
                    telemetry.addLine("HII");
                    telemetry.addLine("HII");
                    telemetry.addLine("HII");
                    telemetry.addLine("HII");
                    telemetry.update();
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

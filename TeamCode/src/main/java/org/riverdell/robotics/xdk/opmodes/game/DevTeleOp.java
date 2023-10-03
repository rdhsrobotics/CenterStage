package org.riverdell.robotics.xdk.opmodes.game;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import io.liftgate.ftc.scripting.opmode.DevLinearOpMode;
import io.liftgate.robotics.mono.Mono;
import io.liftgate.robotics.mono.gamepad.ButtonType;
import io.liftgate.robotics.mono.gamepad.GamepadCommands;
import kotlin.Unit;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author GrowlyX
 * @since 9/5/2023
 */
@TeleOp(name = "Dev", group = "game")
public class DevTeleOp extends DevLinearOpMode {

    @Override
    public void runOpMode() {
        buildCommands();
        super.runOpMode();
    }

    private void buildCommands() {
        final GamepadCommands gp1Commands = Mono
            .INSTANCE.commands(gamepad1);

        gp1Commands.where(ButtonType.ButtonX)
            .triggers(() -> {
                System.out.println("hey :)");
                return Unit.INSTANCE;
            })
            .whenPressedOnce();

        gp1Commands.startListening();
    }

    @Override
    public @NotNull String getScriptName() {
        return "Dev.ts";
    }

    @Override
    public @NotNull List<ImpliedVariable> getImpliedVariables() {
        return Collections.emptyList();
    }
}

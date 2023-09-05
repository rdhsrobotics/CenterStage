package org.riverdell.robotics;

import com.qualcomm.robotcore.hardware.Gamepad;
import kotlin.Unit;
import org.riverdell.robotics.gamepad.ButtonType;
import org.riverdell.robotics.gamepad.GamepadCommands;

/**
 * @author GrowlyX
 * @since 9/4/2023
 */
public class TestWhere {
    public static void main(String[] args) throws InterruptedException {
        final Gamepad gamepad = new Gamepad();
        final GamepadCommands commands = new GamepadCommands(gamepad);

        commands
            .where(ButtonType.ButtonA)
            .and(ButtonType.ButtonX)
            .or((builder) -> builder.and(ButtonType.BumperLeft))
            .runs(() -> {
                System.out.println("woof (single)");
                return Unit.INSTANCE;
            })
            .whenPressedOnce();

        commands
            .where(ButtonType.ButtonX)
            .or((builder) -> builder.and(ButtonType.DPadLeft))
            .onlyWhenNot(TestWhere::someSuperBadThing)
            .runs(() -> {
                System.out.println("hey! continuous");
                return Unit.INSTANCE;
            })
            .whileItIsBeingPressed();

        commands.startListening();

        Thread.sleep(1000L);
        gamepad.a = true;

        Thread.sleep(1000L);
        gamepad.dpad_left = true;
        gamepad.x = true;

        Thread.sleep(250L);
        gamepad.reset();
    }

    public static boolean someSuperBadThing() {
        return false;
    }
}

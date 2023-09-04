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
    public static boolean someSuperBadThing() {
        return true;
    }

    public void test(Gamepad gamepad) {
        final GamepadCommands commands = new GamepadCommands(gamepad);

        commands
            .where(ButtonType.ButtonA)
            .and(ButtonType.ButtonX)
            .or((builder) -> builder.and(ButtonType.BumperLeft))
            .runs(() -> {
                System.out.println("woof");
                return Unit.INSTANCE;
            })
            .whenPressedOnce();

        commands
            .where(ButtonType.ButtonX)
            .or((builder) -> builder.and(ButtonType.ButtonA).and(ButtonType.DPadLeft))
            .onlyWhenNot(TestWhere::someSuperBadThing)
            .runs(() -> {
                System.out.println("hey");
                return Unit.INSTANCE;
            })
            .whileItIsBeingPressed();

        commands.startListening();
    }
}

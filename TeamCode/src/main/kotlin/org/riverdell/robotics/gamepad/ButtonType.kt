package org.riverdell.robotics.gamepad

import com.qualcomm.robotcore.hardware.Gamepad

/**
 * @author GrowlyX
 * @since 9/4/2023
 */
enum class ButtonType(
    val gamepadMapping: (Gamepad) -> Boolean
)
{
    ButtonA(Gamepad::a),
    ButtonB(Gamepad::b),
    ButtonX(Gamepad::x),
    ButtonY(Gamepad::y),

    BumperLeft(Gamepad::left_bumper),
    BumperRight(Gamepad::right_bumper),

    DPadLeft(Gamepad::dpad_left),
    DPadDown(Gamepad::dpad_down),
    DPadRight(Gamepad::dpad_right),
    DPadUp(Gamepad::dpad_up)
}

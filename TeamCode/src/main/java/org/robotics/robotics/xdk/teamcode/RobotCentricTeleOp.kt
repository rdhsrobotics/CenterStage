package org.robotics.robotics.xdk.teamcode

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.robotics.robotics.xdk.teamcode.subsystem.Drivebase

/**
 * A TeleOp implementation which drives a mecanum drivebase
 * with robot-centric controls.
 *
 * @author Subham
 */
@TeleOp(name = Global.RobotCentricTeleOpName)
class RobotCentricTeleOp : AbstractTeleOp()
{
    override fun driveRobot(
        drivebase: Drivebase,
        driverOp: GamepadEx,
        multiplier: Double
    )
    {
        drivebase.driveRobotCentric(driverOp, multiplier)
    }
}
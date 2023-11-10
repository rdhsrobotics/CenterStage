package org.riverdell.robotics.xdk.opmodes.subsystem

import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.IMU
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import io.liftgate.robotics.mono.subsystem.Subsystem
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.riverdell.robotics.xdk.opmodes.pipeline.hardware
import kotlin.math.min

class Drivebase(private val opMode: LinearOpMode) : AbstractSubsystem()
{
    private val motors by lazy {
        mutableListOf<DcMotor>(
            opMode.hardware("backLeft"),
            opMode.hardware("backRight"),
            opMode.hardware("frontLeft"),
            opMode.hardware("frontRight")
        )
    }

    private val imu by lazy { opMode.hardware<IMU>("imu") }

    private val backingDriveBase by lazy {
        val backLeft = Motor(opMode.hardwareMap, "backLeft")
        val backRight = Motor(opMode.hardwareMap, "backRight")
        val frontLeft = Motor(opMode.hardwareMap, "frontLeft")
        val frontRight = Motor(opMode.hardwareMap, "frontRight")

        MecanumDrive(frontLeft, frontRight, backLeft, backRight)
    }

    override fun composeStageContext() = object : StageContext
    {
        override fun dispose()
        {
            motors.onEach {
                it.power = 0.0
                it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            }
        }

        override fun isCompleted() = motors.none { it.isBusy }
    }

    fun driveRobotCentric(driverOp: GamepadEx, scaleFactor: Double)
    {
        backingDriveBase.driveRobotCentric(
            -driverOp.leftX * scaleFactor,
            -driverOp.leftY * scaleFactor,
            driverOp.rightX * min(0.7, scaleFactor),
            true
        )
    }

    fun driveFieldCentric(driverOp: GamepadEx, scaleFactor: Double)
    {
        val heading = imu.robotYawPitchRollAngles.getYaw(AngleUnit.DEGREES)

        backingDriveBase.driveFieldCentric(
            -driverOp.leftX * scaleFactor,
            -driverOp.leftY * scaleFactor,
            driverOp.rightX * min(0.7, scaleFactor),
            heading,
            true
        )
    }

    override fun doInitialize()
    {
        imu.initialize(
            IMU.Parameters(
                RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                    RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                )
            )
        )
        imu.resetYaw()

        backingDriveBase
    }

    override fun isCompleted() = motors.none { it.isBusy }

    override fun dispose()
    {
        backingDriveBase.stop()
    }
}
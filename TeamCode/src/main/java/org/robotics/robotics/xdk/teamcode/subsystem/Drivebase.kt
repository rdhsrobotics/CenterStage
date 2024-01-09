package org.robotics.robotics.xdk.teamcode.subsystem

import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.hardware.ImuOrientationOnRobot
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles
import org.robotics.robotics.xdk.teamcode.autonomous.hardware
import kotlin.concurrent.thread

class Drivebase(private val opMode: LinearOpMode) : AbstractSubsystem()
{
    lateinit var motors: List<DcMotor>
    private val imu by lazy { opMode.hardware<IMU>("imu") }

    private val backingDriveBase by lazy {
        val backLeft = Motor(opMode.hardwareMap, "backLeft")
        val backRight = Motor(opMode.hardwareMap, "backRight")
        val frontLeft = Motor(opMode.hardwareMap, "frontLeft")
        val frontRight = Motor(opMode.hardwareMap, "frontRight")

        MecanumDrive(frontLeft, frontRight, backLeft, backRight)
    }

    override fun composeStageContext() = TODO()

    fun driveRobotCentric(driverOp: GamepadEx, scaleFactor: Double)
    {
        backingDriveBase.driveRobotCentric(
            -driverOp.leftX * scaleFactor,
            -driverOp.leftY * scaleFactor,
            driverOp.rightX * scaleFactor,
            true
        )
    }

    fun driveFieldCentric(driverOp: GamepadEx, scaleFactor: Double)
    {
        val heading = imu.robotYawPitchRollAngles.getYaw(AngleUnit.DEGREES)

        backingDriveBase.driveFieldCentric(
            -driverOp.leftX * scaleFactor,
            -driverOp.leftY * scaleFactor,
            driverOp.rightX * scaleFactor,
            heading,
            true
        )
    }

    /**
     * Initializes both the IMU and all drivebase motors.
     */
    override fun doInitialize()
    {
        motors = listOf(
            opMode.hardware("frontLeft"), opMode.hardware("frontRight"),
            opMode.hardware("backLeft"), opMode.hardware("backRight")
        )

        backingDriveBase
    }

    override fun isCompleted() = true

    override fun dispose()
    {
        backingDriveBase.stop()
    }
}
package org.robotics.robotics.xdk.teamcode.subsystem

import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.IMU
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles
import org.robotics.robotics.xdk.teamcode.autonomous.hardware
import kotlin.concurrent.thread

class Drivebase(private val opMode: LinearOpMode) : AbstractSubsystem()
{
    lateinit var allDriveBaseMotors: List<DcMotor>
    private val mutex = Any()

    private lateinit var imu: IMU
    private var imuYPR: YawPitchRollAngles? = null

    private lateinit var backingDriveBase: MecanumDrive

    override fun composeStageContext() = TODO()

    fun driveRobotCentric(driverOp: GamepadEx, scaleFactor: Double)
    {
        backingDriveBase.driveRobotCentric(
            driverOp.leftX * scaleFactor,
            driverOp.leftY * scaleFactor,
            driverOp.rightX * scaleFactor,
            true
        )
    }

    fun driveFieldCentric(driverOp: GamepadEx, scaleFactor: Double)
    {
        val heading = getIMUYawPitchRollAngles().getYaw(AngleUnit.DEGREES)

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
        allDriveBaseMotors = listOf(
            opMode.hardware("frontLeft"),
            opMode.hardware("frontRight"),
            opMode.hardware("backLeft"),
            opMode.hardware("backRight")
        )

        imu = opMode.hardware<IMU>("imu")
        imu.resetDeviceConfigurationForOpMode()
        imu.initialize(
            IMU.Parameters(
                RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD,
                    RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
                )
            )
        )

        imu.resetYaw()

        /*thread(isDaemon = true) {
            while (!opMode.isStopRequested)
            {
                synchronized(mutex) { imuYPR = imu.robotYawPitchRollAngles }
            }
        }*/

        val backLeft = Motor(opMode.hardwareMap, "backLeft")
        val backRight = Motor(opMode.hardwareMap, "backRight")
        val frontLeft = Motor(opMode.hardwareMap, "frontLeft")
        val frontRight = Motor(opMode.hardwareMap, "frontRight")

        backingDriveBase = MecanumDrive(
            frontLeft, frontRight, backLeft, backRight
        )
    }

    fun getIMUYawPitchRollAngles() = imuYPR ?: imu.robotYawPitchRollAngles
    fun backingImu() = imu

    override fun isCompleted() = true
    override fun dispose()
    {

    }
}
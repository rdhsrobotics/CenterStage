package org.robotics.robotics.xdk.teamcode.autonomous

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.DistanceSensor
import com.qualcomm.robotcore.hardware.HardwareDevice
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.ExecutionGroup
import io.liftgate.robotics.mono.subsystem.Subsystem
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.robotics.robotics.xdk.teamcode.autonomous.contexts.BothClawFinger
import org.robotics.robotics.xdk.teamcode.autonomous.contexts.ExtenderContext
import org.robotics.robotics.xdk.teamcode.autonomous.contexts.LeftClawFinger
import org.robotics.robotics.xdk.teamcode.autonomous.contexts.RightClawFinger
import org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.MovementHandler
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TapeSide
import org.robotics.robotics.xdk.teamcode.autonomous.detection.TeamColor
import org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.PIDController
import org.robotics.robotics.xdk.teamcode.autonomous.detection.VisionPipeline
import org.robotics.robotics.xdk.teamcode.autonomous.profiles.AutonomousProfile
import org.robotics.robotics.xdk.teamcode.autonomous.utilities.AutoPipelineUtilities
import org.robotics.robotics.xdk.teamcode.subsystem.AirplaneLauncher
import org.robotics.robotics.xdk.teamcode.subsystem.Drivebase
import org.robotics.robotics.xdk.teamcode.subsystem.Elevator
import org.robotics.robotics.xdk.teamcode.subsystem.claw.ExtendableClaw
import kotlin.concurrent.thread
import kotlin.math.absoluteValue
import kotlin.math.sign

abstract class AbstractAutoPipeline(
    private val autonomousProfile: AutonomousProfile,
    private val teamColor: TeamColor = autonomousProfile.teamColor,
    internal val blockExecutionGroup: ExecutionGroup.(AbstractAutoPipeline, TapeSide) -> Unit =
        autonomousProfile.buildExecutionGroup()
) : LinearOpMode(), io.liftgate.robotics.mono.subsystem.System
{
    override val subsystems = mutableSetOf<Subsystem>()

    private lateinit var frontRight: DcMotor
    private lateinit var frontLeft: DcMotor

    private lateinit var backRight: DcMotor
    private lateinit var backLeft: DcMotor

    lateinit var movementHandler: MovementHandler

    var frontDistanceSensor: DistanceSensor? = null
    val drivebase by lazy { Drivebase(this) }

    internal val elevatorSubsystem by lazy { Elevator(this) }
    internal val clawSubsystem by lazy { ExtendableClaw(this) }

    private val airplaneSubsystem by lazy { AirplaneLauncher(this) }
    private val visionPipeline by lazy { VisionPipeline(teamColor, this) }

    val multipleTelemetry by lazy {
        MultipleTelemetry(
            this.telemetry,
            FtcDashboard.getInstance().telemetry
        )
    }

    override fun runOpMode()
    {
        register(
            clawSubsystem,
            elevatorSubsystem,
            airplaneSubsystem,
            drivebase,
            visionPipeline
        )

        runCatching {
            hardware<DistanceSensor>("frontSensor").apply {
                frontDistanceSensor = this
            }
        }

        // keep all log entries
        Mono.logSink = {
            multipleTelemetry.addLine("[Mono] $it")
            multipleTelemetry.update()
        }

        frontLeft = hardware<DcMotor>("frontLeft")
        frontLeft.direction = DcMotorSimple.Direction.REVERSE
        frontLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        frontRight = hardware<DcMotor>("frontRight")
        frontRight.direction = DcMotorSimple.Direction.FORWARD
        frontRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        backLeft = hardware<DcMotor>("backLeft")
        backLeft.direction = DcMotorSimple.Direction.REVERSE
        backLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        backRight = hardware<DcMotor>("backRight")
        backRight.direction = DcMotorSimple.Direction.FORWARD
        backRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        movementHandler = MovementHandler(this)

        multipleTelemetry.addLine("Waiting for start. Started detection...")
        multipleTelemetry.update()

        initializeAll()
        stopAndResetMotors()

        while (opModeInInit())
        {
            clawSubsystem.periodic()

            multipleTelemetry.addLine("Auto in initialized")
            multipleTelemetry.addData("Tape side", visionPipeline.getTapeSide())

            multipleTelemetry.addLine("=== PID Tuning Graph Outputs ===")
            multipleTelemetry.addData("Error", 0.0)
            multipleTelemetry.addData("Target", 0.0)
            multipleTelemetry.addData("Input", 0.0)
            multipleTelemetry.addData("Output", 0.0)
            multipleTelemetry.addData("Velocity", 0.0)

            runCatching {
                multipleTelemetry.addData(
                    "IMU",
                    drivebase.getIMUYawPitchRollAngles()
                        .getYaw(AngleUnit.DEGREES)
                )
            }.onFailure {
                multipleTelemetry.addData("IMU", 0.0)
            }

            multipleTelemetry.update()
        }

        waitForStart()

        val tapeSide = visionPipeline.getTapeSide()
        val executionGroup = Mono.buildExecutionGroup {
            providesContext { _ ->
                RightClawFinger(claw = clawSubsystem)
            }

            providesContext { _ ->
                LeftClawFinger(claw = clawSubsystem)
            }

            providesContext { _ ->
                BothClawFinger(claw = clawSubsystem)
            }

            providesContext { _ ->
                ExtenderContext(claw = clawSubsystem)
            }
        }

        thread {
            while (!isStopRequested)
            {
                clawSubsystem.periodic()
            }
        }

        executionGroup.apply {
            blockExecutionGroup(
                this@AbstractAutoPipeline, tapeSide
            )
        }

        executionGroup.executeBlocking()
        disposeOfAll()

        Mono.logSink = { }
    }

    fun move(ticks: Double, heading: Double) = movementHandler.move(-ticks, heading)
    fun turn(ticks: Double) = movementHandler.turn(ticks)
    fun strafe(ticks: Double) = movementHandler.strafe(-ticks)

    fun lockUntilMotorsFree(maximumTimeMillis: Long = 1500L)
    {
        val start = System.currentTimeMillis()
        while (
            System.currentTimeMillis() < start + maximumTimeMillis &&
            (frontLeft.isBusy ||
                frontRight.isBusy ||
                backLeft.isBusy ||
                backRight.isBusy)
        )
        {
            if (isStopRequested)
            {
                return
            }

            sleep(50L)
        }
    }

    fun stopAndResetMotors() = configureMotorsToDo {
        it.power = 0.0
        it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    }

    fun runMotors() = configureMotorsToDo {
        it.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    fun setPower(power: Double) = configureMotorsToDo {
        it.power = power
    }

    fun setRelativePowers(left: Double, right: Double)
    {
        frontLeft.power = left
        frontRight.power = right

        backLeft.power = left
        backRight.power = right
    }

    fun setTurnPower(power: Double)
    {
        frontLeft.power = power
        frontRight.power = -power

        backLeft.power = power
        backRight.power = -power
    }

    fun setRelativeStrafePower(left: Double, right: Double)
    {
        frontLeft.power = left
        frontRight.power = -right

        backLeft.power = -left
        backRight.power = right
    }

    private fun configureMotorsToDo(consumer: (DcMotor) -> Unit)
    {
        drivebase.allDriveBaseMotors.forEach(consumer::invoke)
    }
}
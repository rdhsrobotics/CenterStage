package org.robotics.robotics.xdk.teamcode.autonomous.thing;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline;
import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Pose;
import org.robotics.robotics.xdk.teamcode.subsystem.drivebasee.MecanumDrivetrain;

@Config
public class PositionCommand {
    AbstractAutoPipeline drivetrain;
    public Pose targetPose;

    public static double xP = 0.07;
    public static double xD = 0.012;

    public static double yP = 0.07;
    public static double yD = 0.012;

    public static double hP = 1;
    public static double hD = 0.075;

    public static PIDFController xController = new PIDFController(xP, 0.0, xD, 0);
    public static PIDFController yController = new PIDFController(yP, 0.0, yD, 0);
    public static PIDFController hController = new PIDFController(hP, 0.0, hD, 0);

    public static double ALLOWED_TRANSLATIONAL_ERROR = 0.75;
    public static double ALLOWED_HEADING_ERROR = 0.02;

    private ElapsedTime timer;
    private ElapsedTime stable;

    public static double STABLE_MS = 100;
    public static double DEAD_MS = 2500;

    private static double  MAX_TRANSLATIONAL_SPEED = 1.0;
    private static double  MAX_ROTATIONAL_SPEED = 1.0;
    private static double K_STATIC = 1.85;



    private final MecanumDrivetrain asd;

    public PositionCommand(Pose targetPose, AbstractAutoPipeline pipeline) {
        this.drivetrain = pipeline;
        this.targetPose = targetPose;

        xController.reset();
        yController.reset();
        hController.reset();

        asd = new MecanumDrivetrain();
    }

    /**
     *
     */
    public void runLol() {
        drivetrain.runWithoutEncoders();

        while (!drivetrain.isStopRequested())
        {
            if (timer == null) timer = new ElapsedTime();
            if (stable == null) stable = new ElapsedTime();

            if (isFinished())
            {
                return;
            }

            Pose robotPose = drivetrain.getLocalizer().getPose();

//        System.out.println("TARGET POSE " + targetPose);


            Pose powers = getPower(robotPose);
            drivetrain.getMultipleTelemetry().addData("Target Pose Y", targetPose.y);
            drivetrain.getMultipleTelemetry().addData("Target Pose X", targetPose.x);
            drivetrain.getMultipleTelemetry().addData("Target Pose Heading", targetPose.heading);

            drivetrain.getMultipleTelemetry().addData("Current Pose Y", robotPose.y);
            drivetrain.getMultipleTelemetry().addData("Current Pose X", robotPose.x);
            drivetrain.getMultipleTelemetry().addData("Current Pose Heading", robotPose.heading);

            drivetrain.getMultipleTelemetry().addData("Power for Y", powers.y);
            drivetrain.getMultipleTelemetry().addData("Power for X", powers.y);
            drivetrain.getMultipleTelemetry().addData("Power for Turn", powers.heading);
            drivetrain.getMultipleTelemetry().update();
            final double[] thing = asd.set(powers);


            /**
             *             opMode.hardware("frontLeft"),
             *             opMode.hardware("frontRight"),
             *             opMode.hardware("backLeft"),
             *             opMode.hardware("backRight")
             */

            /**
             *         wheelSpeeds[RobotDrive.MotorType.kFrontLeft.value] = forwardSpeed + strafeSpeed + turnSpeed;
             *         wheelSpeeds[RobotDrive.MotorType.kFrontRight.value] = forwardSpeed - strafeSpeed - turnSpeed;
             *         wheelSpeeds[RobotDrive.MotorType.kBackLeft.value] = (forwardSpeed - strafeSpeed + turnSpeed);
             *         wheelSpeeds[RobotDrive.MotorType.kBackRight.value] = (forwardSpeed + strafeSpeed - turnSpeed);
             */
            drivetrain.getDrivebase().getAllDriveBaseMotors().get(0).setPower(thing[0]);
            drivetrain.getDrivebase().getAllDriveBaseMotors().get(1).setPower(thing[1]);
            drivetrain.getDrivebase().getAllDriveBaseMotors().get(2).setPower(thing[2]);
            drivetrain.getDrivebase().getAllDriveBaseMotors().get(3).setPower(thing[3]);
        }
    }

    public boolean isFinished() {
        Pose robotPose = drivetrain.getLocalizer().getPose();
        Pose delta = targetPose.subtract(robotPose);

        if (delta.toVec2D().magnitude() > ALLOWED_TRANSLATIONAL_ERROR
                || Math.abs(delta.heading) > ALLOWED_HEADING_ERROR) {
            stable.reset();
        }

        return timer.milliseconds() > DEAD_MS || stable.milliseconds() > STABLE_MS;
    }

    public Pose getPower(Pose robotPose) {
        if(targetPose.heading - robotPose.heading > Math.PI) targetPose.heading -= 2 * Math.PI;
        if(targetPose.heading - robotPose.heading < -Math.PI) targetPose.heading += 2 * Math.PI;

        double xPower = xController.calculate(robotPose.x, targetPose.x);
        double yPower = yController.calculate(robotPose.y, targetPose.y);
        double hPower = hController.calculate(robotPose.heading, targetPose.heading);

        double x_rotated = xPower * Math.cos(-robotPose.heading) - yPower * Math.sin(-robotPose.heading);
        double y_rotated = xPower * Math.sin(-robotPose.heading) + yPower * Math.cos(-robotPose.heading);

        hPower = Range.clip(hPower, -MAX_ROTATIONAL_SPEED, MAX_ROTATIONAL_SPEED);
        x_rotated = Range.clip(x_rotated, -MAX_TRANSLATIONAL_SPEED / K_STATIC, MAX_TRANSLATIONAL_SPEED / K_STATIC);
        y_rotated = Range.clip(y_rotated, -MAX_TRANSLATIONAL_SPEED, MAX_TRANSLATIONAL_SPEED);

        return new Pose(x_rotated * K_STATIC, y_rotated, hPower);
    }
}

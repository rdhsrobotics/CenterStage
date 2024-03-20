package org.robotics.robotics.xdk.teamcode.autonomous.position;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline;
import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Pose;

import java.util.List;
import java.util.function.Supplier;

import io.liftgate.robotics.mono.pipeline.RootExecutionGroup;

@Config
public class PositionCommand {
    private final AbstractAutoPipeline drivetrain;
    private final Pose targetPose;

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

    private final ElapsedTime timer = new ElapsedTime();
    private final ElapsedTime stable = new ElapsedTime();

    public static double STABLE_MS = 100;
    public static double DEAD_MS = 2500;

    private static double MAX_TRANSLATIONAL_SPEED = 1.0;
    private static double MAX_ROTATIONAL_SPEED = 1.0;
    private static double K_STATIC = 1.85;

    private final MecanumAutoDrive mecanumAutoDrive;
    private final RootExecutionGroup executionGroup;

    public PositionCommand(final Pose targetPose, final RootExecutionGroup executionGroup) {
        this.drivetrain = AbstractAutoPipeline.getInstance();
        this.targetPose = targetPose;

        xController.reset();
        yController.reset();
        hController.reset();

        this.executionGroup = executionGroup;
        this.mecanumAutoDrive = new MecanumAutoDrive();
    }

    private Supplies<Pose, Pose> targetPoseSupplier = (pose) -> new Pose();
    private Pose getTargetPose(Pose robotPose) {
        if (targetPose != null) {
            return targetPose;
        }

        return targetPoseSupplier.supply(robotPose);
    }

    public void supplyCustomTargetPose(Supplies<Pose, Pose> supplier) {
        this.targetPoseSupplier = supplier;
    }

    public void execute() {
        while (true) {
            if (drivetrain.isStopRequested()) {
                executionGroup.terminateMidExecution();
                return;
            }

            final Pose robotPose = drivetrain.getLocalizer().getPose();
            final Pose targetPose = getTargetPose(robotPose);
            if (isFinished(targetPose)) {
                break;
            }

            final Pose powers = getPower(robotPose, targetPose);

            final double[] drivetrainPowers = mecanumAutoDrive.getPowers(powers);
            driveWithPowers(drivetrainPowers);
        }

        driveWithPowers(mecanumAutoDrive.getPowers(new Pose()));
    }

    private void driveWithPowers(double[] drivetrainPowers) {
        final List<DcMotor> motors = drivetrain.getDrivebase().getAllDriveBaseMotors();

        motors.get(0).setPower(drivetrainPowers[0]);
        motors.get(1).setPower(drivetrainPowers[1]);
        motors.get(2).setPower(drivetrainPowers[2]);
        motors.get(3).setPower(drivetrainPowers[3]);
    }

    public boolean isFinished(Pose targetPose) {
        Pose robotPose = drivetrain.getLocalizer().getPose();
        Pose delta = targetPose.subtract(robotPose);

        if (delta.toVec2D().magnitude() > ALLOWED_TRANSLATIONAL_ERROR
                || Math.abs(delta.heading) > ALLOWED_HEADING_ERROR) {
            stable.reset();
        }

        return timer.milliseconds() > DEAD_MS || stable.milliseconds() > STABLE_MS;
    }

    public Pose getPower(Pose robotPose, Pose targetPose) {
        if (targetPose.heading - robotPose.heading > Math.PI) targetPose.heading -= 2 * Math.PI;
        if (targetPose.heading - robotPose.heading < -Math.PI) targetPose.heading += 2 * Math.PI;

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

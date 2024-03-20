package org.robotics.robotics.xdk.teamcode.autonomous.position;

import com.arcrobotics.ftclib.drivebase.RobotDrive;
import com.qualcomm.robotcore.util.Range;

import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline;
import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Pose;
import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Vector2D;

public class MecanumAutoDrive {

    public double[] getPowers(Pose pose) {
        return getPowers(pose, 0);
    }

    public double[] getPowers(double strafeSpeed, double forwardSpeed,
                              double turnSpeed, double gyroAngle) {
        Vector2D input = new Vector2D(strafeSpeed, forwardSpeed).rotate(-gyroAngle);

        strafeSpeed = Range.clip(input.x, -1, 1);
        forwardSpeed = Range.clip(input.y, -1, 1);
        turnSpeed = Range.clip(turnSpeed, -1, 1);

        double[] wheelSpeeds = new double[4];

        wheelSpeeds[RobotDrive.MotorType.kFrontLeft.value] = forwardSpeed + strafeSpeed + turnSpeed;
        wheelSpeeds[RobotDrive.MotorType.kFrontRight.value] = forwardSpeed - strafeSpeed - turnSpeed;
        wheelSpeeds[RobotDrive.MotorType.kBackLeft.value] = (forwardSpeed - strafeSpeed + turnSpeed);
        wheelSpeeds[RobotDrive.MotorType.kBackRight.value] = (forwardSpeed + strafeSpeed - turnSpeed);
        // 1.06, 1.04

        // feedforward & voltage comp
        double correction = 12 / AbstractAutoPipeline.getInstance().getVoltage();
        for (int i = 0; i < wheelSpeeds.length; i++) {
            wheelSpeeds[i] = Math.abs(wheelSpeeds[i]) < 0.01 ?
                    wheelSpeeds[i] * correction :
                    (wheelSpeeds[i] + Math.signum(wheelSpeeds[i]) * 0.085) * correction;
        }

        double max = 1;
        for (double wheelSpeed : wheelSpeeds) max = Math.max(max, Math.abs(wheelSpeed));

        if (max > 1) {
            wheelSpeeds[RobotDrive.MotorType.kFrontLeft.value] /= max;
            wheelSpeeds[RobotDrive.MotorType.kFrontRight.value] /= max;
            wheelSpeeds[RobotDrive.MotorType.kBackLeft.value] /= max;
            wheelSpeeds[RobotDrive.MotorType.kBackRight.value] /= max;
        }

        final double[] ws = new double[4];
        ws[0] = wheelSpeeds[0];
        ws[1] = wheelSpeeds[1];
        ws[2] = wheelSpeeds[2];
        ws[3] = wheelSpeeds[3];
        return ws;
    }

    public double[] getPowers(Pose pose, double angle) {
        return getPowers(pose.x, pose.y, pose.heading, angle);
    }
}

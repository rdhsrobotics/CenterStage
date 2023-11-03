package org.riverdell.robotics.xdk.opmodes.pipeline;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class DegreeUtilities {

    public static void Hors(double target, AbstractAutoPipeline pipe) {
        YawPitchRollAngles orientation = pipe.getImu().getRobotYawPitchRollAngles();
        AngularVelocity angularVelocity = pipe.getImu().getRobotAngularVelocity(AngleUnit.DEGREES);
        double yawbegin = orientation.getYaw(AngleUnit.DEGREES);
        double yaw = 0;
        double vel = angularVelocity.zRotationRate;
        double startTime = System.currentTimeMillis();

        double rampUpTime = 300;
        double rampUp;

        double totalerror = 0;
        double powerLimit = 1;

        double error = target-yaw;

        double velEnd = 0.15;
        double yawEnd = 1;

        pipe.stopAndResetMotors();
        pipe.runMotors();
        while (((Math.abs(error) > yawEnd) ||(Math.abs(vel) > velEnd)) &&!pipe.isStopRequested()) {
            // Retrieve Rotational Angles and Velocities
            orientation = pipe.getImu().getRobotYawPitchRollAngles();
            angularVelocity = pipe.getImu().getRobotAngularVelocity(AngleUnit.DEGREES);
            yaw = orientation.getYaw(AngleUnit.DEGREES)-yawbegin;
            vel = angularVelocity.zRotationRate;
            error = degFrom(yaw, target);
            totalerror+=error;
            rampUp = Math.min(1, (System.currentTimeMillis()-startTime)/rampUpTime);

            pipe.setTurnPower(rampUp*(Math.max(Math.min(powerLimit, (AutoPipelineUtilities.PID_ROTATION_KP*error+AutoPipelineUtilities.PID_ROTATION_KD*vel/*+AutoPipelineUtilities.PID_ROTATION_KI*totalerror*/)), -powerLimit)));

            pipe.telemetry.addData("Yaw (Z)", "%.2f Deg. (Heading)", orientation.getYaw(AngleUnit.DEGREES));
            pipe.telemetry.addData("Yaw (Z) velocity", "%.2f Deg/Sec", vel = angularVelocity.zRotationRate);
            pipe.telemetry.addData("Error: ", error);
            pipe.telemetry.update();
        }

        pipe.lockUntilMotorsFree(300L);
    }

    public static double degFrom(double current, double targetDeg) { //returns the minimum deg from input, solves 180 -180 problem
        double d1 = (targetDeg- current);
        double d2 = (360-targetDeg+ current);
        if (Math.abs(d1)<Math.abs(d2)) {
            return -d1;
        } else {
            return -d2;
        }
    }
}

package org.robotics.robotics.xdk.teamcode.subsystem.drivebasee;


import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Pose;

public interface Drivetrain {
    double[] set(Pose pose);
}
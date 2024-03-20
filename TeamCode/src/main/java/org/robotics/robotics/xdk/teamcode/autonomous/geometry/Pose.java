package org.robotics.robotics.xdk.teamcode.autonomous.geometry;

import com.arcrobotics.ftclib.geometry.Vector2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;

import java.util.Locale;

public class Pose extends Point {

    public double heading;

    public Pose(double x, double y, double heading) {
        super(x, y);
        this.heading = AngleUnit.normalizeRadians(heading);
    }

    public Pose(Point p, double heading) {
        this(p.x, p.y, heading);
    }

    public Pose(Vector2d vec, double heading) {
        this(vec.getX(), vec.getY(), heading);
    }

    public Pose() {
        this(0, 0, 0);
    }

    public Pose(AprilTagPoseFtc ftcPose) {
        double heading = Math.toRadians(-ftcPose.yaw);
        this.x = ftcPose.x * Math.cos(heading) - ftcPose.y * Math.sin(heading);
        this.y = ftcPose.x * Math.sin(heading) + ftcPose.y * Math.cos(heading);
        this.heading = heading;
    }

    public void set(Pose other) {
        this.x = other.x;
        this.y = other.y;
        this.heading = other.heading;
    }

    public Pose add(Pose other) {
        return new Pose(x + other.x, y + other.y, heading + other.heading);
    }

    public Pose subtract(Pose other) {
        return new Pose(this.x - other.x, this.y - other.y, AngleUnit.normalizeRadians(this.heading - other.heading));
    }

    public Pose divide(Pose other) {
        return new Pose(this.x / other.x, this.y / other.y, this.heading / other.heading);
    }


    public Pose subt(Pose other) {
        return new Pose(x - other.x, y - other.y, heading - other.heading);
    }

    public Vector2d toVec2D() {
        return new Vector2d(x, y);
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%.2f %.2f %.3f", x, y, heading);
    }
}
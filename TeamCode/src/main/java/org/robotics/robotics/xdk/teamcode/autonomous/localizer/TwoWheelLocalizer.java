package org.robotics.robotics.xdk.teamcode.autonomous.localizer;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.TwoTrackingWheelLocalizer;
import com.arcrobotics.ftclib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline;
import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Pose;

import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleSupplier;

/*
 * Sample tracking wheel localizer implementation assuming the standard configuration:
 *
 *    ^
 *    |
 *    | ( x direction)
 *    |
 *    v
 *    <----( y direction )---->

 *        (forward)
 *    /--------------\
 *    |     ____     |
 *    |     ----     |    <- Perpendicular Wheel
 *    |           || |
 *    |           || |    <- Parallel Wheel
 *    |              |
 *    |              |
 *    \--------------/
 *
 */
public class TwoWheelLocalizer extends TwoTrackingWheelLocalizer {

    public static double TICKS_PER_REV = 2000;
    public static double WHEEL_RADIUS = 0.94488; // 48
    public static double GEAR_RATIO = 1;

    private final AbstractAutoPipeline pipeline;

    private final Motor.Encoder lateral;
    private final Motor.Encoder horizontal;

    public TwoWheelLocalizer(AbstractAutoPipeline pipeline) {
        super(Arrays.asList(
                new Pose2d(0, 0, 0), // left + right
                new Pose2d(0, 0, Math.toRadians(90)) // front
        ));
        this.pipeline = pipeline;
        lateral = new Motor(pipeline.hardwareMap, "backRight").encoder;
        horizontal = new Motor(pipeline.hardwareMap, "frontLeft").encoder;
        setPose(new Pose());
    }

    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }



    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        return Arrays.asList(
                encoderTicksToInches(lateral.getPosition()),
                encoderTicksToInches(horizontal.getPosition())
        );
    }

    @Override
    public double getHeading() {
        return pipeline.getDrivebase().getIMUYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    @NonNull
    @Override
    public List<Double> getWheelVelocities() {
        // TODO: If your encoder velocity can exceed 32767 counts / second (such as the REV Through Bore and other
        //  competing magnetic encoders), change Encoder.getRawVelocity() to Encoder.getCorrectedVelocity() to enable a
        //  compensation method

        return Arrays.asList(0.0, 0.0);
    }

    public Pose getPose() {
        Pose2d pose = getPoseEstimate();
        return new Pose(-pose.getY(), pose.getX(), pose.getHeading());
    }

    public void setPose(Pose pose) {
        super.setPoseEstimate(new Pose2d(pose.y, -pose.x, pose.heading));
    }


}
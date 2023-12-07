package org.riverdell.robotics.xdk.opmodes.pipeline.detection.prop;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TapeSide;
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TeamColor;

import java.util.concurrent.atomic.AtomicReference;

@Config
public class PropPipeline implements VisionProcessor, CameraStreamSource {

    private final TeamColor teamColor;

    private final AtomicReference<Bitmap> lastFrame = new AtomicReference<>(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565));

    private TapeSide location = TapeSide.Middle;
    public MatOfKeyPoint keyPoints = new MatOfKeyPoint();

    private final Mat finalMat = new Mat();

    public static int width = 80; // Adjusted width value for the zone
    public static int height = 80; // Adjusted height value for the zone

    // Adjusted coordinates to fit within the 640x480 resolution
    public static int blueLeftX = 280;
    public static int blueLeftY = 200;

    public static int blueCenterX = 400;
    public static int blueCenterY = 100;

    public static int redLeftX = 300;
    public static int redLeftY = 190;

    public static int redCenterX = 425;
    public static int redCenterY = 80;

    public static double redThreshold = 2.5;
    public static double blueThreshold = 0.2;
    public static double threshold = 0;

    public double leftColor = 0.0;
    public double centerColor = 0.0;

    public Scalar left = new Scalar(0, 0, 0);
    public Scalar center = new Scalar(0, 0, 0);

    public PropPipeline(TeamColor teamColor) {
        this.teamColor = teamColor;
    }

    public TapeSide getLocation() {
        return location;
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        lastFrame.set(Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565));

        if (teamColor == TeamColor.Red) {
            threshold = redThreshold;
        } else {
            threshold = blueThreshold;
        }
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {

        if (teamColor == TeamColor.Red) {
            threshold = redThreshold;
        } else {
            threshold = blueThreshold;
        }

        frame.copyTo(finalMat);
        Imgproc.GaussianBlur(finalMat, finalMat, new Size(5, 5), 0.0);


        Rect leftZoneArea = new Rect(teamColor == TeamColor.Red ? redLeftX : blueLeftX, teamColor == TeamColor.Red ? redLeftY : blueLeftY, width, height);
        Rect centerZoneArea = new Rect(teamColor == TeamColor.Red ? redCenterX : blueCenterX, teamColor == TeamColor.Red ? redCenterY : blueCenterY, width, height);

        Mat leftZone = finalMat.submat(leftZoneArea);
        Mat centerZone = finalMat.submat(centerZoneArea);

        left = Core.sumElems(leftZone);
        center = Core.sumElems(centerZone);

        leftColor = left.val[0] / 1000000.0;
        centerColor = center.val[0] / 1000000.0;

        if (teamColor == TeamColor.Blue) {
            if (leftColor < threshold) {
                // left zone has it
                location = TapeSide.Left;
                Imgproc.rectangle(frame, leftZoneArea, new Scalar(255, 255, 255));
            } else if (centerColor < threshold) {
                // center zone has it
                location = TapeSide.Middle;
                Imgproc.rectangle(frame, centerZoneArea, new Scalar(255, 255, 255));
            } else {
                // right zone has it
                location = TapeSide.Right;
                Imgproc.rectangle(frame, centerZoneArea, new Scalar(255, 255, 255));
            }
        } else {
            if (leftColor > threshold) {
                // left zone has it
                location = TapeSide.Middle;
                Imgproc.rectangle(frame, leftZoneArea, new Scalar(255, 255, 255));
            } else if (centerColor > threshold) {
                // center zone has it
                location = TapeSide.Right;
                Imgproc.rectangle(frame, leftZoneArea, new Scalar(255, 255, 255));
            } else {
                // right zone has it
                location = TapeSide.Left;
                Imgproc.rectangle(frame, leftZoneArea, new Scalar(255, 255, 255));
            }
        }

        Imgproc.rectangle(finalMat, leftZoneArea, new Scalar(255, 255, 255));
        Imgproc.rectangle(finalMat, centerZoneArea, new Scalar(255, 255, 255));

        Bitmap b = Bitmap.createBitmap(finalMat.width(), finalMat.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(finalMat, b);
        lastFrame.set(b);

        leftZone.release();
        centerZone.release();

        return null;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }

    @Override
    public void getFrameBitmap(Continuation<? extends Consumer<Bitmap>> continuation) {
        continuation.dispatch(bitmapConsumer -> bitmapConsumer.accept(lastFrame.get()));
    }
}
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
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TapeSide;
import org.riverdell.robotics.xdk.opmodes.pipeline.detection.TeamColor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Config
public class PropPipelineV2 implements CameraStreamSource, VisionProcessor {

    private final TeamColor teamColor;
    public PropPipelineV2(final TeamColor teamColor) {
        this.teamColor = teamColor;
    }

    private final Mat hsvMat = new Mat();
    private final Mat mask = new Mat();

    private Point leftBlobCenter = new Point();
    private Point centerBlobCenter = new Point();
    private Point rightBlobCenter = new Point();

    private final AtomicReference<Bitmap> lastFrame = new AtomicReference<>(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565));

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        lastFrame.set(Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565));
    }

    @Override
    public Object processFrame(Mat input, long captureTimeNanos) {
        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV);

        Scalar lowerRedBound = new Scalar(0, 100, 100);
        Scalar upperRedBound = new Scalar(10, 255, 255);
        Scalar lowerBlueBound = new Scalar(90, 100, 100);
        Scalar upperBlueBound = new Scalar(130, 255, 255);

        if (teamColor == TeamColor.Red) {
            Core.inRange(hsvMat, lowerRedBound, upperRedBound, mask);
        } else {
            Core.inRange(hsvMat, lowerBlueBound, upperBlueBound, mask);
        }

        detectBlobs(mask, input);
        drawRectanglesAroundBlobs(input, leftBlobCenter, centerBlobCenter, rightBlobCenter);

        final Bitmap bitmap = Bitmap.createBitmap(
                input.width(), input.height(), Bitmap.Config.RGB_565
        );
        Utils.matToBitmap(input, bitmap);
        lastFrame.set(bitmap);

        return input;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }

    private Rect leftRect;
    private Rect centerRect;
    private Rect rightRect;

    private void detectBlobs(Mat mask, Mat input) {
        // Define regions of interest for detecting blobs
        leftRect = new Rect(
                0,
                (int) (0.4 * mask.rows()),
                mask.cols() / 3,
                (int) (mask.rows() * 0.6)
        );
        centerRect = new Rect(
                mask.cols() / 3,
                (int) (0.4 * mask.rows()),
                mask.cols() / 3,
                (int) (mask.rows() * 0.6)
        );
        rightRect = new Rect(
                2 * mask.cols() / 3,
                (int) (0.4 * mask.rows()),
                mask.cols() / 3,
                (int) (mask.rows() * 0.6)
        );

        Imgproc.rectangle(input, leftRect, new Scalar(0, 0, 0));
        Imgproc.rectangle(input, centerRect, new Scalar(0, 0, 0));
        Imgproc.rectangle(input, rightRect, new Scalar(0, 0, 0));

        leftBlobCenter = findLargestBlob(mask, leftRect);
        centerBlobCenter = findLargestBlob(mask, centerRect);
        rightBlobCenter = findLargestBlob(mask, rightRect);
    }

    private Point findLargestBlob(Mat mask, Rect rect) {
        Mat roi = new Mat(mask, rect);
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(roi, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        Point blobCenter = new Point();

        if (!contours.isEmpty()) {
            double maxArea = -1;
            int maxAreaIdx = -1;
            for (int i = 0; i < contours.size(); i++) {
                double area = Imgproc.contourArea(contours.get(i));
                if (area > maxArea) {
                    maxArea = area;
                    maxAreaIdx = i;
                }
            }

            if (maxAreaIdx != -1) {
                Moments moments = Imgproc.moments(contours.get(maxAreaIdx));
                blobCenter.x = (int) (moments.get_m10() / moments.get_m00()) + rect.x;
                blobCenter.y = (int) (moments.get_m01() / moments.get_m00()) + rect.y;
            }
        }

        return blobCenter;
    }

    public static double rectSize = 100;
    public static double colorPercentage = -1.0;

    public static TapeSide currentTapeSide = TapeSide.Left;

    private void drawRectanglesAroundBlobs(Mat input, Point leftBlobCenter, Point centerBlobCenter, Point rightBlobCenter) {
        drawRectangle(input, centerBlobCenter, rectSize, new Scalar(0, 0, 255), TapeSide.Middle, centerRect);
        drawRectangle(input, rightBlobCenter, rectSize, new Scalar(0, 0, 255), TapeSide.Right, rightRect);
        drawRectangle(input, leftBlobCenter, rectSize, new Scalar(0, 0, 255), TapeSide.Left, leftRect);
        colorPercentage = -1.0;
    }

    private void drawRectangle(Mat frame, Point center, double size, Scalar color, TapeSide asshat, Rect kms) {
        if (center.x != 0 && center.y != 0) {
            final Point point1 = new Point(center.x - size / 2, center.y - size / 2);
            final Point point2 = new Point(center.x + size / 2, center.y + size / 2);

            double total = (point2.x  - point1.x) * (point2.y - point1.y);
            Mat thingAsd = mask.submat(kms);
            double percentage = Core.countNonZero(thingAsd);

            if ((percentage / total) > colorPercentage)
            {
                colorPercentage = percentage / total;
                currentTapeSide = asshat;
            }

            Imgproc.rectangle(frame, point1, point2, color, 2);
        }
    }

    public TapeSide getTapeSide() {
        return currentTapeSide;
    }

    @Override
    public void getFrameBitmap(Continuation<? extends Consumer<Bitmap>> continuation) {
        continuation.dispatch(bitmapConsumer -> bitmapConsumer.accept(lastFrame.get()));
    }
}

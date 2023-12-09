package org.riverdell.robotics.xdk.opmodes.pipeline.detection.prop;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    private final Mat hsvMat = new Mat();
    private final Mat mask = new Mat();

    private Point leftBlobCenter = new Point();
    private Point centerBlobCenter = new Point();
    private final Point rightBlobCenter = new Point();

    public static double RECTANGLE_SIZE = 100;

    public static int LR_B = 150;
    public static int LR_G = 100;
    public static int LR_R = 100;

    public static int UR_B = 255;
    public static int UR_G = 255;
    public static int UR_R = 255;

    public static double Y_OFFSET_TOP = 0.4;
    public static double Y_OFFSET_BOTTOM = 0.6;

    public static double PERCENTAGE_REQUIRED_RED = 0.30;
    public static double PERCENTAGE_REQUIRED_BLUE = 0.50;

    private final Scalar lowerBlueBound = new Scalar(90, 100, 100);
    private final Scalar upperBlueBound = new Scalar(130, 255, 255);

    private Rect regionLeft;
    private Rect regionCenter;
    private Rect regionRight;

    private final AtomicReference<Bitmap> lastFrame = new AtomicReference<>(
            Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
    );

    private double percentageColorMatch = -1.0;

    @NotNull
    public TapeSide currentTapeSide = TapeSide.Right;

    @Nullable
    private TapeSide iterationTapeSide = null;

    public PropPipelineV2(final TeamColor teamColor) {
        this.teamColor = teamColor;
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        lastFrame.set(Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565));
    }

    @Override
    public Object processFrame(Mat input, long captureTimeNanos) {
        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV);

        if (teamColor == TeamColor.Red) {
            Core.inRange(hsvMat, new Scalar(LR_B, LR_G, LR_R),  new Scalar(UR_B, UR_G, UR_R), mask);
        } else {
            Core.inRange(hsvMat, lowerBlueBound, upperBlueBound, mask);
        }

        detectBlobs(mask);
        drawRectanglesAroundBlobs(
                input, leftBlobCenter, centerBlobCenter, rightBlobCenter
        );

        // draw regions on the mask
        Imgproc.rectangle(input, regionLeft, new Scalar(0, 0, 0));
        Imgproc.rectangle(input, regionCenter, new Scalar(0, 0, 0));

        if (regionRight != null)
        {
            Imgproc.rectangle(input, regionRight, new Scalar(0, 0, 0));
        }

        final Bitmap bitmap = Bitmap.createBitmap(
                input.width(), input.height(), Bitmap.Config.RGB_565
        );

        Utils.matToBitmap(/*mask*/input, bitmap);
        this.lastFrame.set(bitmap);
        return input;
    }

    private void detectBlobs(final @NotNull Mat mask) {
        final int bottomOffset = (int) (((1.0 - Y_OFFSET_TOP) * mask.rows()) * Y_OFFSET_BOTTOM);
        final int topOffset = (int) (Y_OFFSET_TOP * mask.rows());
        this.regionLeft = new Rect(
                0,
                topOffset,
                mask.cols() / 3,
                bottomOffset
        );

        this.regionCenter = new Rect(
                mask.cols() / 3,
                topOffset,
                2 * mask.cols() / 3,
                bottomOffset
        );

        /*this.regionRight = new Rect(
                2 * mask.cols() / 3,
                topOffset,
                mask.cols() / 3,
                bottomOffset
        );*/

        this.leftBlobCenter = findLargestBlob(mask, regionLeft);
        this.centerBlobCenter = findLargestBlob(mask, regionCenter);
//        this.rightBlobCenter = findLargestBlob(mask, regionRight);
    }

    @NotNull
    @Contract("_, _ -> new")
    private Point findLargestBlob(
            final @NotNull Mat mask,
            final @NotNull Rect rect
    ) {
        final Mat roi = new Mat(mask, rect);
        final List<MatOfPoint> contours = new ArrayList<>();
        final Mat hierarchy = new Mat();

        Imgproc.findContours(
                roi, contours, hierarchy,
                Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE
        );

        final Point blobCenter = new Point();

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
               final  Moments moments = Imgproc.moments(contours.get(maxAreaIdx));
                blobCenter.x = (int) (moments.get_m10() / moments.get_m00()) + rect.x;
                blobCenter.y = (int) (moments.get_m01() / moments.get_m00()) + rect.y;
            }
        }

        return blobCenter;
    }

    private void drawRectanglesAroundBlobs(
            final @NotNull Mat input,
            final @NotNull Point leftBlobCenter,
            final @NotNull Point centerBlobCenter,
            final @NotNull Point rightBlobCenter
    ) {
        this.iterationTapeSide = null;

        drawRectangle(
                input, centerBlobCenter,
                TapeSide.Middle, regionCenter
        );
        /*drawRectangle(
                input, rightBlobCenter,
                TapeSide.Right, regionRight
        );*/
        drawRectangle(
                input, leftBlobCenter,
                TapeSide.Left, regionLeft
        );

        // reset the color match percentage
        this.percentageColorMatch = -1.0;

        if (this.iterationTapeSide != null) {
            this.currentTapeSide = iterationTapeSide;
        } else {
            this.currentTapeSide = TapeSide.Right;
        }
    }

    private void drawRectangle(
            Mat frame, Point center, TapeSide tapeSide, Rect regionBlob
    ) {
        if (center.x != 0 && center.y != 0) {
            final Point min = new Point(
                    center.x - RECTANGLE_SIZE / 2,
                    center.y - RECTANGLE_SIZE / 2);
            final Point max = new Point(
                    center.x + RECTANGLE_SIZE / 2,
                    center.y + RECTANGLE_SIZE / 2
            );

            double rectangleArea = (max.x  - min.x) * (max.y - min.y);
            final Mat matColorOverlap = mask.submat(regionBlob);
            double percentage = Core.countNonZero(matColorOverlap);

            if ((percentage / rectangleArea) > (teamColor == TeamColor.Blue ? PERCENTAGE_REQUIRED_BLUE : PERCENTAGE_REQUIRED_RED))
            {
                if ((percentage / rectangleArea) > percentageColorMatch)
                {
                    // set the %age if we have a greater ratio
                    percentageColorMatch = percentage / rectangleArea;
                    iterationTapeSide = tapeSide;

                    Imgproc.rectangle(
                            frame, min, max,
                            new Scalar(0, 255, 0), 2
                    );
                }
            }
        }
    }

    public double getPercentageColorMatch() {
        return percentageColorMatch;
    }

    @NotNull
    public TapeSide getTapeSide() {
        return currentTapeSide;
    }

    @Override
    public void getFrameBitmap(Continuation<? extends Consumer<Bitmap>> continuation) {
        continuation.dispatch(bitmapConsumer -> bitmapConsumer.accept(lastFrame.get()));
    }

    @Override
    public void onDrawFrame(
            Canvas canvas, int onscreenWidth, int onscreenHeight,
            float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext
    ) {

    }
}

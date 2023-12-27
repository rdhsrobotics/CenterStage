package org.riverdell.robotics.xdk.opmodes.autonomous.detection.prop;

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
import org.riverdell.robotics.xdk.opmodes.autonomous.detection.TapeSide;
import org.riverdell.robotics.xdk.opmodes.autonomous.detection.TeamColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Config
public class PropPipelineV2 implements CameraStreamSource, VisionProcessor {

    private final TeamColor teamColor;

    private final Mat hsvMat = new Mat();
    private final Mat mask = new Mat();

    public static double RECTANGLE_SIZE = 100;

    public static double Y_OFFSET_TOP = 0.4;
    public static double Y_OFFSET_BOTTOM = 0.6;

    public static double PERCENTAGE_REQUIRED_RED = 0.20;
    public static double PERCENTAGE_REQUIRED_BLUE = 0.50;

    public static final TapeSide FALLBACK_DETECTION = TapeSide.Right;
    public static final TapeSide[] DETECTION_ZONES = new TapeSide[]{
            TapeSide.Left,
            TapeSide.Middle
    };

    private final Scalar lowerRedBound = new Scalar(150, 100, 100);
    private final Scalar upperRedBound = new Scalar(255, 255, 255);

    private final Scalar lowerBlueBound = new Scalar(90, 100, 100);
    private final Scalar upperBlueBound = new Scalar(130, 255, 255);

    private final Map<TapeSide, Rect> detectionRegions = new HashMap<>();
    private final Map<TapeSide, Point> detectionCenters = new HashMap<>();

    private final AtomicReference<Bitmap> lastFrame = new AtomicReference<>(
            Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
    );

    private double percentageColorMatch = -1.0;

    @NotNull
    public TapeSide currentTapeSide = TapeSide.Right;

    @Nullable
    private TapeSide prominentTapeSide = null;

    public PropPipelineV2(final TeamColor teamColor) {
        this.teamColor = teamColor;
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        lastFrame.set(
                Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        );
    }

    @Override
    public Object processFrame(Mat input, long captureTimeNanos) {
        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV);

        Core.inRange(
                hsvMat,
                teamColor == TeamColor.Red ? lowerRedBound : lowerBlueBound,
                teamColor == TeamColor.Red ? upperRedBound : upperBlueBound,
                mask
        );

        drawTapeRegionBlobs(mask);
        drawRectanglesAroundBlobsAndDetect(input);

        detectionRegions.forEach((side, region) -> {
            Imgproc.rectangle(input, region, new Scalar(0, 0, 0));
        });

        final Bitmap bitmap = Bitmap.createBitmap(
                input.width(), input.height(), Bitmap.Config.RGB_565
        );

        Utils.matToBitmap(mask, bitmap);
        this.lastFrame.set(bitmap);

        return input;
    }

    private void buildRegionBounds(
            @NotNull Mat mask,
            int bottomOffset,
            int topOffset
    ) {
        detectionRegions.put(
                TapeSide.Left,
                new Rect(
                        0,
                        topOffset,
                        mask.cols() / 3,
                        bottomOffset
                )
        );

        detectionRegions.put(
                TapeSide.Middle,
                new Rect(
                        mask.cols() / 3,
                        topOffset,
                        2 * mask.cols() / 3,
                        bottomOffset
                )
        );

        /*detectionRegions.put(
                TapeSide.Right,
                new Rect(
                        2 * mask.cols() / 3,
                        topOffset,
                        mask.cols() / 3,
                        bottomOffset
                )
        );*/
    }

    private void drawTapeRegionBlobs(final @NotNull Mat mask) {
        final int bottomOffset = (int) (((1.0 - Y_OFFSET_TOP) * mask.rows()) * Y_OFFSET_BOTTOM);
        final int topOffset = (int) (Y_OFFSET_TOP * mask.rows());

        buildRegionBounds(mask, bottomOffset, topOffset);

        for (final TapeSide detectionZone : DETECTION_ZONES) {
            detectionCenters.put(
                    detectionZone,
                    findLargestBlob(
                            mask,
                            Objects.requireNonNull(
                                    detectionRegions.get(detectionZone)
                            )
                    )
            );
        }
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
                final Moments moments = Imgproc.moments(contours.get(maxAreaIdx));
                blobCenter.x = (int) (moments.get_m10() / moments.get_m00()) + rect.x;
                blobCenter.y = (int) (moments.get_m01() / moments.get_m00()) + rect.y;
            }
        }

        return blobCenter;
    }

    private void drawRectanglesAroundBlobsAndDetect(
            final @NotNull Mat input
    ) {
        this.prominentTapeSide = null;

        for (final TapeSide detectionZone : DETECTION_ZONES) {
            drawRectangleAndDetermineProminentZone(
                    input,
                    Objects.requireNonNull(
                            detectionCenters.get(detectionZone)
                    ),
                    detectionZone,
                    Objects.requireNonNull(
                            detectionRegions.get(detectionZone)
                    )
            );
        }

        // reset the color match percentage
        this.percentageColorMatch = -1.0;

        if (this.prominentTapeSide != null) {
            this.currentTapeSide = prominentTapeSide;
        } else {
            this.currentTapeSide = FALLBACK_DETECTION;
        }
    }

    private void drawRectangleAndDetermineProminentZone(
            @NotNull Mat frame,
            @NotNull Point center,
            @NotNull TapeSide tapeSide,
            @NotNull Rect regionBlob
    ) {
        if (center.x != 0 && center.y != 0) {
            final Point min = new Point(
                    center.x - RECTANGLE_SIZE / 2,
                    center.y - RECTANGLE_SIZE / 2);
            final Point max = new Point(
                    center.x + RECTANGLE_SIZE / 2,
                    center.y + RECTANGLE_SIZE / 2
            );

            double rectangleArea = (max.x - min.x) * (max.y - min.y);
            final Mat matColorOverlap = mask.submat(regionBlob);
            double percentage = Core.countNonZero(matColorOverlap);

            if ((percentage / rectangleArea) > (teamColor == TeamColor.Blue ? PERCENTAGE_REQUIRED_BLUE : PERCENTAGE_REQUIRED_RED)) {
                if ((percentage / rectangleArea) > percentageColorMatch) {
                    // set the %age if we have a greater ratio
                    percentageColorMatch = percentage / rectangleArea;
                    prominentTapeSide = tapeSide;

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

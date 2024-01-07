//[TeamCode](../../../index.md)/[org.robotics.robotics.xdk.teamcode.autonomous.detection.elements](../index.md)/[GameElementDetection](index.md)

# GameElementDetection

[androidJvm]\
open class [GameElementDetection](index.md) : CameraStreamSource, VisionProcessor

## Constructors

| | |
|---|---|
| [GameElementDetection](-game-element-detection.md) | [androidJvm]<br>constructor(@[NonNull](https://developer.android.com/reference/kotlin/androidx/annotation/NonNull.html)teamColor: [TeamColor](../../org.robotics.robotics.xdk.teamcode.autonomous.detection/-team-color/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [CAM_VIEW](-c-a-m_-v-i-e-w.md) | [androidJvm]<br>open var [CAM_VIEW](-c-a-m_-v-i-e-w.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [DETECTION_ZONES](-d-e-t-e-c-t-i-o-n_-z-o-n-e-s.md) | [androidJvm]<br>val [DETECTION_ZONES](-d-e-t-e-c-t-i-o-n_-z-o-n-e-s.md): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[TapeSide](../../org.robotics.robotics.xdk.teamcode.autonomous.detection/-tape-side/index.md)&gt; |
| [FALLBACK_DETECTION](-f-a-l-l-b-a-c-k_-d-e-t-e-c-t-i-o-n.md) | [androidJvm]<br>val [FALLBACK_DETECTION](-f-a-l-l-b-a-c-k_-d-e-t-e-c-t-i-o-n.md): [TapeSide](../../org.robotics.robotics.xdk.teamcode.autonomous.detection/-tape-side/index.md) |
| [LR_B](-l-r_-b.md) | [androidJvm]<br>open var [LR_B](-l-r_-b.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [LR_G](-l-r_-g.md) | [androidJvm]<br>open var [LR_G](-l-r_-g.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [LR_R](-l-r_-r.md) | [androidJvm]<br>open var [LR_R](-l-r_-r.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [PERCENTAGE_REQUIRED_BLUE](-p-e-r-c-e-n-t-a-g-e_-r-e-q-u-i-r-e-d_-b-l-u-e.md) | [androidJvm]<br>open var [PERCENTAGE_REQUIRED_BLUE](-p-e-r-c-e-n-t-a-g-e_-r-e-q-u-i-r-e-d_-b-l-u-e.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [PERCENTAGE_REQUIRED_RED](-p-e-r-c-e-n-t-a-g-e_-r-e-q-u-i-r-e-d_-r-e-d.md) | [androidJvm]<br>open var [PERCENTAGE_REQUIRED_RED](-p-e-r-c-e-n-t-a-g-e_-r-e-q-u-i-r-e-d_-r-e-d.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [percentageColorMatch](percentage-color-match.md) | [androidJvm]<br>open val [percentageColorMatch](percentage-color-match.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [RECTANGLE_SIZE](-r-e-c-t-a-n-g-l-e_-s-i-z-e.md) | [androidJvm]<br>open var [RECTANGLE_SIZE](-r-e-c-t-a-n-g-l-e_-s-i-z-e.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [UR_B](-u-r_-b.md) | [androidJvm]<br>open var [UR_B](-u-r_-b.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [UR_G](-u-r_-g.md) | [androidJvm]<br>open var [UR_G](-u-r_-g.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [UR_R](-u-r_-r.md) | [androidJvm]<br>open var [UR_R](-u-r_-r.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [Y_OFFSET_BOTTOM](-y_-o-f-f-s-e-t_-b-o-t-t-o-m.md) | [androidJvm]<br>open var [Y_OFFSET_BOTTOM](-y_-o-f-f-s-e-t_-b-o-t-t-o-m.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [Y_OFFSET_TOP](-y_-o-f-f-s-e-t_-t-o-p.md) | [androidJvm]<br>open var [Y_OFFSET_TOP](-y_-o-f-f-s-e-t_-t-o-p.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |

## Functions

| Name | Summary |
|---|---|
| [getFrameBitmap](get-frame-bitmap.md) | [androidJvm]<br>open fun [getFrameBitmap](get-frame-bitmap.md)(continuation: Continuation&lt;out Consumer&lt;[Bitmap](https://developer.android.com/reference/kotlin/android/graphics/Bitmap.html)&gt;&gt;) |
| [getTapeSide](get-tape-side.md) | [androidJvm]<br>@NotNull<br>open fun [getTapeSide](get-tape-side.md)(): [TapeSide](../../org.robotics.robotics.xdk.teamcode.autonomous.detection/-tape-side/index.md) |
| [init](init.md) | [androidJvm]<br>open fun [init](init.md)(width: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), height: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), calibration: CameraCalibration) |
| [onDrawFrame](on-draw-frame.md) | [androidJvm]<br>open fun [onDrawFrame](on-draw-frame.md)(canvas: [Canvas](https://developer.android.com/reference/kotlin/android/graphics/Canvas.html), onscreenWidth: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), onscreenHeight: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), scaleBmpPxToCanvasPx: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html), scaleCanvasDensity: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html), userContext: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)) |
| [processFrame](process-frame.md) | [androidJvm]<br>open fun [processFrame](process-frame.md)(input: Mat, captureTimeNanos: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |

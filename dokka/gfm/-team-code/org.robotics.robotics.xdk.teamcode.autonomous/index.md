//[TeamCode](../../index.md)/[org.robotics.robotics.xdk.teamcode.autonomous](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [AbstractAutoPipeline](-abstract-auto-pipeline/index.md) | [androidJvm]<br>abstract class [AbstractAutoPipeline](-abstract-auto-pipeline/index.md) : LinearOpMode, System |

## Functions

| Name | Summary |
|---|---|
| [hardware](hardware.md) | [androidJvm]<br>inline fun &lt;[T](hardware.md) : HardwareDevice&gt; LinearOpMode.[hardware](hardware.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [T](hardware.md)<br>A little extension function to get a non-null HardwareDevice. |
| [normalizedYaw](normalized-yaw.md) | [androidJvm]<br>fun IMU.[normalizedYaw](normalized-yaw.md)(): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [runRepeating](run-repeating.md) | [androidJvm]<br>fun [runRepeating](run-repeating.md)(everyMillis: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), block: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [ScheduledFuture](https://developer.android.com/reference/kotlin/java/util/concurrent/ScheduledFuture.html)&lt;*&gt; |
| [scheduleAsyncExecution](schedule-async-execution.md) | [androidJvm]<br>fun [scheduleAsyncExecution](schedule-async-execution.md)(inMillis: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), block: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [ScheduledFuture](https://developer.android.com/reference/kotlin/java/util/concurrent/ScheduledFuture.html)&lt;*&gt; |

//[TeamCode](../../../index.md)/[org.robotics.robotics.xdk.teamcode.subsystem](../index.md)/[Elevator](index.md)

# Elevator

[androidJvm]\
class [Elevator](index.md)(opMode: LinearOpMode) : AbstractSubsystem

## Constructors

| | |
|---|---|
| [Elevator](-elevator.md) | [androidJvm]<br>constructor(opMode: LinearOpMode) |

## Properties

| Name | Summary |
|---|---|
| [backingHangMotor](backing-hang-motor.md) | [androidJvm]<br>val [backingHangMotor](backing-hang-motor.md): DcMotorEx |
| [backingMotor](backing-motor.md) | [androidJvm]<br>val [backingMotor](backing-motor.md): DcMotorEx |

## Functions

| Name | Summary |
|---|---|
| [bind](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#1293232412%2FFunctions%2F863896225) | [androidJvm]<br>@NotNull<br>open override fun &lt;[T](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#1293232412%2FFunctions%2F863896225) : [AutoCloseable](https://developer.android.com/reference/kotlin/java/lang/AutoCloseable.html)&gt; [bind](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#1293232412%2FFunctions%2F863896225)(@NotNullterminable: [T](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#1293232412%2FFunctions%2F863896225) &amp; [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [T](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#1293232412%2FFunctions%2F863896225) &amp; [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [bindModule](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#-1130991702%2FFunctions%2F863896225) | [androidJvm]<br>@NotNull<br>open fun &lt;[T](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#-1130991702%2FFunctions%2F863896225) : TerminableModule&gt; [bindModule](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#-1130991702%2FFunctions%2F863896225)(@NotNullmodule: [T](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#-1130991702%2FFunctions%2F863896225) &amp; [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [T](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#-1130991702%2FFunctions%2F863896225) &amp; [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [bindWith](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#1475146202%2FFunctions%2F863896225) | [androidJvm]<br>open fun [bindWith](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#1475146202%2FFunctions%2F863896225)(@NotNullconsumer: TerminableConsumer) |
| [cleanup](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#-1922477242%2FFunctions%2F863896225) | [androidJvm]<br>open override fun [cleanup](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#-1922477242%2FFunctions%2F863896225)() |
| [close](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#1617525170%2FFunctions%2F863896225) | [androidJvm]<br>open override fun [close](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#1617525170%2FFunctions%2F863896225)() |
| [closeAndReportException](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#-1532312530%2FFunctions%2F863896225) | [androidJvm]<br>open override fun [closeAndReportException](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#-1532312530%2FFunctions%2F863896225)() |
| [closeSilently](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#-1052119504%2FFunctions%2F863896225) | [androidJvm]<br>@Nullable<br>open override fun [closeSilently](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#-1052119504%2FFunctions%2F863896225)(): CompositeClosingException? |
| [composeStageContext](compose-stage-context.md) | [androidJvm]<br>open override fun [composeStageContext](compose-stage-context.md)(): StageContext |
| [configureElevator](configure-elevator.md) | [androidJvm]<br>fun [configureElevator](configure-elevator.md)(stick: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)) |
| [configureElevatorManually](configure-elevator-manually.md) | [androidJvm]<br>fun [configureElevatorManually](configure-elevator-manually.md)(position: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)) |
| [configureElevatorManuallyRaw](configure-elevator-manually-raw.md) | [androidJvm]<br>fun [configureElevatorManuallyRaw](configure-elevator-manually-raw.md)(position: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [dispose](dispose.md) | [androidJvm]<br>open override fun [dispose](dispose.md)() |
| [doInitialize](do-initialize.md) | [androidJvm]<br>open override fun [doInitialize](do-initialize.md)() |
| [initialize](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#-467062385%2FFunctions%2F863896225) | [androidJvm]<br>open override fun [initialize](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#-467062385%2FFunctions%2F863896225)() |
| [isClosed](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#1856354860%2FFunctions%2F863896225) | [androidJvm]<br>open fun [isClosed](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#1856354860%2FFunctions%2F863896225)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCompleted](is-completed.md) | [androidJvm]<br>open override fun [isCompleted](is-completed.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [requiresAtLeast](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#-1878088254%2FFunctions%2F863896225) | [androidJvm]<br>open fun [requiresAtLeast](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#-1878088254%2FFunctions%2F863896225)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? |
| [timesOutAfter](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#-667246506%2FFunctions%2F863896225) | [androidJvm]<br>open fun [timesOutAfter](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#-667246506%2FFunctions%2F863896225)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? |
| [toggleHangLift](toggle-hang-lift.md) | [androidJvm]<br>fun [toggleHangLift](toggle-hang-lift.md)(power: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)) |
| [with](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#743359081%2FFunctions%2F863896225) | [androidJvm]<br>open override fun [with](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#743359081%2FFunctions%2F863896225)(autoCloseable: [AutoCloseable](https://developer.android.com/reference/kotlin/java/lang/AutoCloseable.html)): CompositeTerminable |
| [withAll](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#1210161398%2FFunctions%2F863896225) | [androidJvm]<br>open fun [withAll](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#1210161398%2FFunctions%2F863896225)(vararg autoCloseables: [AutoCloseable](https://developer.android.com/reference/kotlin/java/lang/AutoCloseable.html)): CompositeTerminable<br>open fun [withAll](../../org.robotics.robotics.xdk.teamcode.subsystem.claw/-extendable-claw/index.md#-901464004%2FFunctions%2F863896225)(autoCloseables: [MutableIterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterable/index.html)&lt;[AutoCloseable](https://developer.android.com/reference/kotlin/java/lang/AutoCloseable.html)&gt;): CompositeTerminable |

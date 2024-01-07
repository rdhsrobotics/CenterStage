//[TeamCode](../../../index.md)/[org.robotics.robotics.xdk.teamcode.subsystem.claw](../index.md)/[ExtendableClaw](index.md)

# ExtendableClaw

[androidJvm]\
class [ExtendableClaw](index.md)(opMode: LinearOpMode) : AbstractSubsystem

## Constructors

| | |
|---|---|
| [ExtendableClaw](-extendable-claw.md) | [androidJvm]<br>constructor(opMode: LinearOpMode) |

## Types

| Name | Summary |
|---|---|
| [ClawState](-claw-state/index.md) | [androidJvm]<br>enum [ClawState](-claw-state/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[ExtendableClaw.ClawState](-claw-state/index.md)&gt; |
| [ClawStateUpdate](-claw-state-update/index.md) | [androidJvm]<br>enum [ClawStateUpdate](-claw-state-update/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[ExtendableClaw.ClawStateUpdate](-claw-state-update/index.md)&gt; |
| [ClawType](-claw-type/index.md) | [androidJvm]<br>enum [ClawType](-claw-type/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[ExtendableClaw.ClawType](-claw-type/index.md)&gt; |
| [Companion](-companion/index.md) | [androidJvm]<br>object [Companion](-companion/index.md) |
| [ExtenderState](-extender-state/index.md) | [androidJvm]<br>enum [ExtenderState](-extender-state/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[ExtendableClaw.ExtenderState](-extender-state/index.md)&gt; |

## Properties

| Name | Summary |
|---|---|
| [backingClawOpenerLeft](backing-claw-opener-left.md) | [androidJvm]<br>val [backingClawOpenerLeft](backing-claw-opener-left.md): [MotionProfiledServo](../../org.robotics.robotics.xdk.teamcode.subsystem.motionprofile.wrappers/-motion-profiled-servo/index.md) |
| [backingClawOpenerRight](backing-claw-opener-right.md) | [androidJvm]<br>val [backingClawOpenerRight](backing-claw-opener-right.md): [MotionProfiledServo](../../org.robotics.robotics.xdk.teamcode.subsystem.motionprofile.wrappers/-motion-profiled-servo/index.md) |
| [backingExtender](backing-extender.md) | [androidJvm]<br>val [backingExtender](backing-extender.md): [MotionProfiledServo](../../org.robotics.robotics.xdk.teamcode.subsystem.motionprofile.wrappers/-motion-profiled-servo/index.md) |
| [extenderState](extender-state.md) | [androidJvm]<br>var [extenderState](extender-state.md): [ExtendableClaw.ExtenderState](-extender-state/index.md) |

## Functions

| Name | Summary |
|---|---|
| [bind](index.md#1293232412%2FFunctions%2F863896225) | [androidJvm]<br>@NotNull<br>open override fun &lt;[T](index.md#1293232412%2FFunctions%2F863896225) : [AutoCloseable](https://developer.android.com/reference/kotlin/java/lang/AutoCloseable.html)&gt; [bind](index.md#1293232412%2FFunctions%2F863896225)(@NotNullterminable: [T](index.md#1293232412%2FFunctions%2F863896225) &amp; [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [T](index.md#1293232412%2FFunctions%2F863896225) &amp; [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [bindModule](index.md#-1130991702%2FFunctions%2F863896225) | [androidJvm]<br>@NotNull<br>open fun &lt;[T](index.md#-1130991702%2FFunctions%2F863896225) : TerminableModule&gt; [bindModule](index.md#-1130991702%2FFunctions%2F863896225)(@NotNullmodule: [T](index.md#-1130991702%2FFunctions%2F863896225) &amp; [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [T](index.md#-1130991702%2FFunctions%2F863896225) &amp; [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [bindWith](index.md#1475146202%2FFunctions%2F863896225) | [androidJvm]<br>open fun [bindWith](index.md#1475146202%2FFunctions%2F863896225)(@NotNullconsumer: TerminableConsumer) |
| [cleanup](index.md#-1922477242%2FFunctions%2F863896225) | [androidJvm]<br>open override fun [cleanup](index.md#-1922477242%2FFunctions%2F863896225)() |
| [close](index.md#1617525170%2FFunctions%2F863896225) | [androidJvm]<br>open override fun [close](index.md#1617525170%2FFunctions%2F863896225)() |
| [closeAndReportException](index.md#-1532312530%2FFunctions%2F863896225) | [androidJvm]<br>open override fun [closeAndReportException](index.md#-1532312530%2FFunctions%2F863896225)() |
| [closeSilently](index.md#-1052119504%2FFunctions%2F863896225) | [androidJvm]<br>@Nullable<br>open override fun [closeSilently](index.md#-1052119504%2FFunctions%2F863896225)(): CompositeClosingException? |
| [composeStageContext](compose-stage-context.md) | [androidJvm]<br>open override fun [composeStageContext](compose-stage-context.md)(): StageContext |
| [decrementAddition](decrement-addition.md) | [androidJvm]<br>fun [decrementAddition](decrement-addition.md)() |
| [decrementClawAddition](decrement-claw-addition.md) | [androidJvm]<br>fun [decrementClawAddition](decrement-claw-addition.md)() |
| [dispose](index.md#-1679028501%2FFunctions%2F863896225) | [androidJvm]<br>open fun [dispose](index.md#-1679028501%2FFunctions%2F863896225)() |
| [doInitialize](do-initialize.md) | [androidJvm]<br>open override fun [doInitialize](do-initialize.md)() |
| [incrementAddition](increment-addition.md) | [androidJvm]<br>fun [incrementAddition](increment-addition.md)() |
| [incrementClawAddition](increment-claw-addition.md) | [androidJvm]<br>fun [incrementClawAddition](increment-claw-addition.md)() |
| [initialize](index.md#-467062385%2FFunctions%2F863896225) | [androidJvm]<br>open override fun [initialize](index.md#-467062385%2FFunctions%2F863896225)() |
| [isClosed](index.md#1856354860%2FFunctions%2F863896225) | [androidJvm]<br>open fun [isClosed](index.md#1856354860%2FFunctions%2F863896225)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCompleted](is-completed.md) | [androidJvm]<br>open override fun [isCompleted](is-completed.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [periodic](periodic.md) | [androidJvm]<br>fun [periodic](periodic.md)() |
| [requiresAtLeast](index.md#-1878088254%2FFunctions%2F863896225) | [androidJvm]<br>open fun [requiresAtLeast](index.md#-1878088254%2FFunctions%2F863896225)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? |
| [timesOutAfter](index.md#-667246506%2FFunctions%2F863896225) | [androidJvm]<br>open fun [timesOutAfter](index.md#-667246506%2FFunctions%2F863896225)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? |
| [toggleExtender](toggle-extender.md) | [androidJvm]<br>fun [toggleExtender](toggle-extender.md)(state: [ExtendableClaw.ExtenderState](-extender-state/index.md)? = null, force: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false) |
| [updateClawState](update-claw-state.md) | [androidJvm]<br>fun [updateClawState](update-claw-state.md)(effectiveOn: [ExtendableClaw.ClawStateUpdate](-claw-state-update/index.md), state: [ExtendableClaw.ClawState](-claw-state/index.md), force: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true) |
| [with](index.md#743359081%2FFunctions%2F863896225) | [androidJvm]<br>open override fun [with](index.md#743359081%2FFunctions%2F863896225)(autoCloseable: [AutoCloseable](https://developer.android.com/reference/kotlin/java/lang/AutoCloseable.html)): CompositeTerminable |
| [withAll](index.md#1210161398%2FFunctions%2F863896225) | [androidJvm]<br>open fun [withAll](index.md#1210161398%2FFunctions%2F863896225)(vararg autoCloseables: [AutoCloseable](https://developer.android.com/reference/kotlin/java/lang/AutoCloseable.html)): CompositeTerminable<br>open fun [withAll](index.md#-901464004%2FFunctions%2F863896225)(autoCloseables: [MutableIterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterable/index.html)&lt;[AutoCloseable](https://developer.android.com/reference/kotlin/java/lang/AutoCloseable.html)&gt;): CompositeTerminable |

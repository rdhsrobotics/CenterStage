//[TeamCode](../../../index.md)/[org.robotics.robotics.xdk.teamcode.autonomous.contexts](../index.md)/[ClawSubsystemContext](index.md)

# ClawSubsystemContext

abstract class [ClawSubsystemContext](index.md)(motionProfiledServo: [MotionProfiledServo](../../org.robotics.robotics.xdk.teamcode.subsystem.motionprofile.wrappers/-motion-profiled-servo/index.md)) : StageContext

#### Inheritors

| |
|---|
| [RightClawFinger](../-right-claw-finger/index.md) |
| [LeftClawFinger](../-left-claw-finger/index.md) |
| [BothClawFinger](../-both-claw-finger/index.md) |
| [ExtenderContext](../-extender-context/index.md) |

## Constructors

| | |
|---|---|
| [ClawSubsystemContext](-claw-subsystem-context.md) | [androidJvm]<br>constructor(vararg motionProfiledServo: [MotionProfiledServo](../../org.robotics.robotics.xdk.teamcode.subsystem.motionprofile.wrappers/-motion-profiled-servo/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [dispose](dispose.md) | [androidJvm]<br>open override fun [dispose](dispose.md)() |
| [isCompleted](is-completed.md) | [androidJvm]<br>open override fun [isCompleted](is-completed.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [requiresAtLeast](requires-at-least.md) | [androidJvm]<br>open override fun [requiresAtLeast](requires-at-least.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [timesOutAfter](times-out-after.md) | [androidJvm]<br>open override fun [timesOutAfter](times-out-after.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |

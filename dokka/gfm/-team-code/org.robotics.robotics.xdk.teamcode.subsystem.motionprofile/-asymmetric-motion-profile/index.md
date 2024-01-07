//[TeamCode](../../../index.md)/[org.robotics.robotics.xdk.teamcode.subsystem.motionprofile](../index.md)/[AsymmetricMotionProfile](index.md)

# AsymmetricMotionProfile

[androidJvm]\
open class [AsymmetricMotionProfile](index.md)

## Constructors

| | |
|---|---|
| [AsymmetricMotionProfile](-asymmetric-motion-profile.md) | [androidJvm]<br>constructor(initialPosition: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), finalPosition: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), constraints: [ProfileConstraints](../-profile-constraints/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [constraints](constraints.md) | [androidJvm]<br>open var [constraints](constraints.md): [ProfileConstraints](../-profile-constraints/index.md) |
| [distance](distance.md) | [androidJvm]<br>open var [distance](distance.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [finalPosition](final-position.md) | [androidJvm]<br>open var [finalPosition](final-position.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [flipped](flipped.md) | [androidJvm]<br>open var [flipped](flipped.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [initialPosition](initial-position.md) | [androidJvm]<br>open var [initialPosition](initial-position.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [max_velocity](max_velocity.md) | [androidJvm]<br>open var [max_velocity](max_velocity.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [originalPos](original-pos.md) | [androidJvm]<br>open var [originalPos](original-pos.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [state](state.md) | [androidJvm]<br>open var [state](state.md): [ProfileState](../-profile-state/index.md) |
| [t1](t1.md) | [androidJvm]<br>open var [t1](t1.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [t1_stop_position](t1_stop_position.md) | [androidJvm]<br>open var [t1_stop_position](t1_stop_position.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [t2](t2.md) | [androidJvm]<br>open var [t2](t2.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [t2_stop_position](t2_stop_position.md) | [androidJvm]<br>open var [t2_stop_position](t2_stop_position.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [t3](t3.md) | [androidJvm]<br>open var [t3](t3.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [totalTime](total-time.md) | [androidJvm]<br>open var [totalTime](total-time.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |

## Functions

| Name | Summary |
|---|---|
| [calculate](calculate.md) | [androidJvm]<br>open fun [calculate](calculate.md)(time: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)): [ProfileState](../-profile-state/index.md) |
| [toString](to-string.md) | [androidJvm]<br>@[NonNull](https://developer.android.com/reference/kotlin/androidx/annotation/NonNull.html)<br>open fun [toString](to-string.md)(): [String](https://developer.android.com/reference/kotlin/java/lang/String.html) |

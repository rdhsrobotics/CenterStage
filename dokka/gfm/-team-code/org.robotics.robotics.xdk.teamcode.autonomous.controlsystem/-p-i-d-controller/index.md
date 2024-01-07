//[TeamCode](../../../index.md)/[org.robotics.robotics.xdk.teamcode.autonomous.controlsystem](../index.md)/[PIDController](index.md)

# PIDController

[androidJvm]\
class [PIDController](index.md)(kP: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), kI: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), kD: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), val setPoint: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), setPointTolerance: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), minimumVelocity: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), telemetry: Telemetry, val maintainHeading: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, val maintainHeadingValue: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = 0.0)

## Constructors

| | |
|---|---|
| [PIDController](-p-i-d-controller.md) | [androidJvm]<br>constructor(kP: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), kI: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), kD: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), setPoint: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), setPointTolerance: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), minimumVelocity: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), telemetry: Telemetry, maintainHeading: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, maintainHeadingValue: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = 0.0) |

## Properties

| Name | Summary |
|---|---|
| [currentRobotHeading](current-robot-heading.md) | [androidJvm]<br>var [currentRobotHeading](current-robot-heading.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [maintainHeading](maintain-heading.md) | [androidJvm]<br>val [maintainHeading](maintain-heading.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false |
| [maintainHeadingValue](maintain-heading-value.md) | [androidJvm]<br>val [maintainHeadingValue](maintain-heading-value.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = 0.0 |
| [setPoint](set-point.md) | [androidJvm]<br>val [setPoint](set-point.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |

## Functions

| Name | Summary |
|---|---|
| [atSetPoint](at-set-point.md) | [androidJvm]<br>fun [atSetPoint](at-set-point.md)(currentValue: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [calculate](calculate.md) | [androidJvm]<br>fun [calculate](calculate.md)(currentValue: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), heading: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [customErrorCalculator](custom-error-calculator.md) | [androidJvm]<br>fun [customErrorCalculator](custom-error-calculator.md)(block: (current: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)) -&gt; [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)): [PIDController](index.md) |
| [customVelocityCalculator](custom-velocity-calculator.md) | [androidJvm]<br>fun [customVelocityCalculator](custom-velocity-calculator.md)(block: () -&gt; [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)): [PIDController](index.md) |

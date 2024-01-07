//[TeamCode](../../../index.md)/[org.robotics.robotics.xdk.teamcode.mono](../index.md)/[MonoTestEGroupAutonomous](index.md)

# MonoTestEGroupAutonomous

[androidJvm]\
@Disabled

@Autonomous(name = &quot;Mono | Test EGroup Auto Terminate&quot;)

class [MonoTestEGroupAutonomous](index.md) : LinearOpMode

## Constructors

| | |
|---|---|
| [MonoTestEGroupAutonomous](-mono-test-e-group-autonomous.md) | [androidJvm]<br>constructor() |

## Properties

| Name | Summary |
|---|---|
| [gamepad1](index.md#1533398127%2FProperties%2F863896225) | [androidJvm]<br>var [gamepad1](index.md#1533398127%2FProperties%2F863896225): Gamepad |
| [gamepad2](index.md#1564417934%2FProperties%2F863896225) | [androidJvm]<br>var [gamepad2](index.md#1564417934%2FProperties%2F863896225): Gamepad |
| [hardwareMap](index.md#-1397214969%2FProperties%2F863896225) | [androidJvm]<br>var [hardwareMap](index.md#-1397214969%2FProperties%2F863896225): HardwareMap |
| [isStarted](index.md#1892877380%2FProperties%2F863896225) | [androidJvm]<br>val [isStarted](index.md#1892877380%2FProperties%2F863896225): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [msStuckDetectInit](index.md#1483349743%2FProperties%2F863896225) | [androidJvm]<br>var [~~msStuckDetectInit~~](index.md#1483349743%2FProperties%2F863896225): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [msStuckDetectInitLoop](index.md#-553001045%2FProperties%2F863896225) | [androidJvm]<br>var [~~msStuckDetectInitLoop~~](index.md#-553001045%2FProperties%2F863896225): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [msStuckDetectLoop](index.md#363588571%2FProperties%2F863896225) | [androidJvm]<br>var [~~msStuckDetectLoop~~](index.md#363588571%2FProperties%2F863896225): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [msStuckDetectStart](index.md#427944857%2FProperties%2F863896225) | [androidJvm]<br>var [~~msStuckDetectStart~~](index.md#427944857%2FProperties%2F863896225): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [msStuckDetectStop](index.md#-409341632%2FProperties%2F863896225) | [androidJvm]<br>var [~~msStuckDetectStop~~](index.md#-409341632%2FProperties%2F863896225): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [stopRequested](index.md#-1991443121%2FProperties%2F863896225) | [androidJvm]<br>val [stopRequested](index.md#-1991443121%2FProperties%2F863896225): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [telemetry](index.md#1097975490%2FProperties%2F863896225) | [androidJvm]<br>var [telemetry](index.md#1097975490%2FProperties%2F863896225): Telemetry |
| [time](index.md#-591827947%2FProperties%2F863896225) | [androidJvm]<br>var [time](index.md#-591827947%2FProperties%2F863896225): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |

## Functions

| Name | Summary |
|---|---|
| [getRuntime](index.md#503260448%2FFunctions%2F863896225) | [androidJvm]<br>open fun [getRuntime](index.md#503260448%2FFunctions%2F863896225)(): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [hardware](../../org.robotics.robotics.xdk.teamcode.autonomous/hardware.md) | [androidJvm]<br>inline fun &lt;[T](../../org.robotics.robotics.xdk.teamcode.autonomous/hardware.md) : HardwareDevice&gt; LinearOpMode.[hardware](../../org.robotics.robotics.xdk.teamcode.autonomous/hardware.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [T](../../org.robotics.robotics.xdk.teamcode.autonomous/hardware.md)<br>A little extension function to get a non-null HardwareDevice. |
| [idle](index.md#-945285709%2FFunctions%2F863896225) | [androidJvm]<br>fun [idle](index.md#-945285709%2FFunctions%2F863896225)() |
| [init](index.md#-1617228809%2FFunctions%2F863896225) | [androidJvm]<br>override fun [init](index.md#-1617228809%2FFunctions%2F863896225)() |
| [init_loop](index.md#-493667936%2FFunctions%2F863896225) | [androidJvm]<br>override fun [init_loop](index.md#-493667936%2FFunctions%2F863896225)() |
| [internalPostInitLoop](index.md#832637297%2FFunctions%2F863896225) | [androidJvm]<br>open fun [~~internalPostInitLoop~~](index.md#832637297%2FFunctions%2F863896225)() |
| [internalPostLoop](index.md#-180688735%2FFunctions%2F863896225) | [androidJvm]<br>open fun [~~internalPostLoop~~](index.md#-180688735%2FFunctions%2F863896225)() |
| [internalPreInit](index.md#958221314%2FFunctions%2F863896225) | [androidJvm]<br>open fun [~~internalPreInit~~](index.md#958221314%2FFunctions%2F863896225)() |
| [internalUpdateTelemetryNow](index.md#949931346%2FFunctions%2F863896225) | [androidJvm]<br>fun [internalUpdateTelemetryNow](index.md#949931346%2FFunctions%2F863896225)(p0: TelemetryMessage) |
| [loop](index.md#1557977315%2FFunctions%2F863896225) | [androidJvm]<br>override fun [loop](index.md#1557977315%2FFunctions%2F863896225)() |
| [opModeInInit](index.md#1015161614%2FFunctions%2F863896225) | [androidJvm]<br>fun [opModeInInit](index.md#1015161614%2FFunctions%2F863896225)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [opModeIsActive](index.md#-911330061%2FFunctions%2F863896225) | [androidJvm]<br>fun [opModeIsActive](index.md#-911330061%2FFunctions%2F863896225)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [requestOpModeStop](index.md#-765560122%2FFunctions%2F863896225) | [androidJvm]<br>fun [requestOpModeStop](index.md#-765560122%2FFunctions%2F863896225)() |
| [resetRuntime](index.md#1757147609%2FFunctions%2F863896225) | [androidJvm]<br>open fun [resetRuntime](index.md#1757147609%2FFunctions%2F863896225)() |
| [runOpMode](run-op-mode.md) | [androidJvm]<br>open override fun [runOpMode](run-op-mode.md)() |
| [sleep](index.md#-1955259651%2FFunctions%2F863896225) | [androidJvm]<br>fun [sleep](index.md#-1955259651%2FFunctions%2F863896225)(p0: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)) |
| [start](index.md#-1200709743%2FFunctions%2F863896225) | [androidJvm]<br>override fun [start](index.md#-1200709743%2FFunctions%2F863896225)() |
| [stop](index.md#841039173%2FFunctions%2F863896225) | [androidJvm]<br>override fun [stop](index.md#841039173%2FFunctions%2F863896225)() |
| [terminateOpModeNow](index.md#-167858447%2FFunctions%2F863896225) | [androidJvm]<br>fun [terminateOpModeNow](index.md#-167858447%2FFunctions%2F863896225)() |
| [updateTelemetry](index.md#1080474565%2FFunctions%2F863896225) | [androidJvm]<br>open fun [updateTelemetry](index.md#1080474565%2FFunctions%2F863896225)(p0: Telemetry) |
| [waitForStart](index.md#246695705%2FFunctions%2F863896225) | [androidJvm]<br>open fun [waitForStart](index.md#246695705%2FFunctions%2F863896225)() |

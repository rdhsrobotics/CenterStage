//[TeamCode](../../../index.md)/[org.robotics.robotics.xdk.teamcode.mono](../index.md)/[MonoTestDualSelectOverride](index.md)

# MonoTestDualSelectOverride

[androidJvm]\
@Disabled

@TeleOp(name = &quot;Mono | Test Select Override&quot;)

class [MonoTestDualSelectOverride](index.md) : LinearOpMode

## Constructors

| | |
|---|---|
| [MonoTestDualSelectOverride](-mono-test-dual-select-override.md) | [androidJvm]<br>constructor() |

## Properties

| Name | Summary |
|---|---|
| [gamepad1](../-mono-test-e-group-autonomous/index.md#1533398127%2FProperties%2F863896225) | [androidJvm]<br>var [gamepad1](../-mono-test-e-group-autonomous/index.md#1533398127%2FProperties%2F863896225): Gamepad |
| [gamepad2](../-mono-test-e-group-autonomous/index.md#1564417934%2FProperties%2F863896225) | [androidJvm]<br>var [gamepad2](../-mono-test-e-group-autonomous/index.md#1564417934%2FProperties%2F863896225): Gamepad |
| [hardwareMap](../-mono-test-e-group-autonomous/index.md#-1397214969%2FProperties%2F863896225) | [androidJvm]<br>var [hardwareMap](../-mono-test-e-group-autonomous/index.md#-1397214969%2FProperties%2F863896225): HardwareMap |
| [isStarted](../-mono-test-e-group-autonomous/index.md#1892877380%2FProperties%2F863896225) | [androidJvm]<br>val [isStarted](../-mono-test-e-group-autonomous/index.md#1892877380%2FProperties%2F863896225): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [msStuckDetectInit](../-mono-test-e-group-autonomous/index.md#1483349743%2FProperties%2F863896225) | [androidJvm]<br>var [~~msStuckDetectInit~~](../-mono-test-e-group-autonomous/index.md#1483349743%2FProperties%2F863896225): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [msStuckDetectInitLoop](../-mono-test-e-group-autonomous/index.md#-553001045%2FProperties%2F863896225) | [androidJvm]<br>var [~~msStuckDetectInitLoop~~](../-mono-test-e-group-autonomous/index.md#-553001045%2FProperties%2F863896225): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [msStuckDetectLoop](../-mono-test-e-group-autonomous/index.md#363588571%2FProperties%2F863896225) | [androidJvm]<br>var [~~msStuckDetectLoop~~](../-mono-test-e-group-autonomous/index.md#363588571%2FProperties%2F863896225): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [msStuckDetectStart](../-mono-test-e-group-autonomous/index.md#427944857%2FProperties%2F863896225) | [androidJvm]<br>var [~~msStuckDetectStart~~](../-mono-test-e-group-autonomous/index.md#427944857%2FProperties%2F863896225): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [msStuckDetectStop](../-mono-test-e-group-autonomous/index.md#-409341632%2FProperties%2F863896225) | [androidJvm]<br>var [~~msStuckDetectStop~~](../-mono-test-e-group-autonomous/index.md#-409341632%2FProperties%2F863896225): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [stopRequested](../-mono-test-e-group-autonomous/index.md#-1991443121%2FProperties%2F863896225) | [androidJvm]<br>val [stopRequested](../-mono-test-e-group-autonomous/index.md#-1991443121%2FProperties%2F863896225): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [telemetry](../-mono-test-e-group-autonomous/index.md#1097975490%2FProperties%2F863896225) | [androidJvm]<br>var [telemetry](../-mono-test-e-group-autonomous/index.md#1097975490%2FProperties%2F863896225): Telemetry |
| [time](../-mono-test-e-group-autonomous/index.md#-591827947%2FProperties%2F863896225) | [androidJvm]<br>var [time](../-mono-test-e-group-autonomous/index.md#-591827947%2FProperties%2F863896225): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |

## Functions

| Name | Summary |
|---|---|
| [getRuntime](../-mono-test-e-group-autonomous/index.md#503260448%2FFunctions%2F863896225) | [androidJvm]<br>open fun [getRuntime](../-mono-test-e-group-autonomous/index.md#503260448%2FFunctions%2F863896225)(): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [hardware](../../org.robotics.robotics.xdk.teamcode.autonomous/hardware.md) | [androidJvm]<br>inline fun &lt;[T](../../org.robotics.robotics.xdk.teamcode.autonomous/hardware.md) : HardwareDevice&gt; LinearOpMode.[hardware](../../org.robotics.robotics.xdk.teamcode.autonomous/hardware.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [T](../../org.robotics.robotics.xdk.teamcode.autonomous/hardware.md)<br>A little extension function to get a non-null HardwareDevice. |
| [idle](../-mono-test-e-group-autonomous/index.md#-945285709%2FFunctions%2F863896225) | [androidJvm]<br>fun [idle](../-mono-test-e-group-autonomous/index.md#-945285709%2FFunctions%2F863896225)() |
| [init](../-mono-test-e-group-autonomous/index.md#-1617228809%2FFunctions%2F863896225) | [androidJvm]<br>override fun [init](../-mono-test-e-group-autonomous/index.md#-1617228809%2FFunctions%2F863896225)() |
| [init_loop](../-mono-test-e-group-autonomous/index.md#-493667936%2FFunctions%2F863896225) | [androidJvm]<br>override fun [init_loop](../-mono-test-e-group-autonomous/index.md#-493667936%2FFunctions%2F863896225)() |
| [internalPostInitLoop](../-mono-test-e-group-autonomous/index.md#832637297%2FFunctions%2F863896225) | [androidJvm]<br>open fun [~~internalPostInitLoop~~](../-mono-test-e-group-autonomous/index.md#832637297%2FFunctions%2F863896225)() |
| [internalPostLoop](../-mono-test-e-group-autonomous/index.md#-180688735%2FFunctions%2F863896225) | [androidJvm]<br>open fun [~~internalPostLoop~~](../-mono-test-e-group-autonomous/index.md#-180688735%2FFunctions%2F863896225)() |
| [internalPreInit](../-mono-test-e-group-autonomous/index.md#958221314%2FFunctions%2F863896225) | [androidJvm]<br>open fun [~~internalPreInit~~](../-mono-test-e-group-autonomous/index.md#958221314%2FFunctions%2F863896225)() |
| [internalUpdateTelemetryNow](../-mono-test-e-group-autonomous/index.md#949931346%2FFunctions%2F863896225) | [androidJvm]<br>fun [internalUpdateTelemetryNow](../-mono-test-e-group-autonomous/index.md#949931346%2FFunctions%2F863896225)(p0: TelemetryMessage) |
| [loop](../-mono-test-e-group-autonomous/index.md#1557977315%2FFunctions%2F863896225) | [androidJvm]<br>override fun [loop](../-mono-test-e-group-autonomous/index.md#1557977315%2FFunctions%2F863896225)() |
| [opModeInInit](../-mono-test-e-group-autonomous/index.md#1015161614%2FFunctions%2F863896225) | [androidJvm]<br>fun [opModeInInit](../-mono-test-e-group-autonomous/index.md#1015161614%2FFunctions%2F863896225)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [opModeIsActive](../-mono-test-e-group-autonomous/index.md#-911330061%2FFunctions%2F863896225) | [androidJvm]<br>fun [opModeIsActive](../-mono-test-e-group-autonomous/index.md#-911330061%2FFunctions%2F863896225)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [requestOpModeStop](../-mono-test-e-group-autonomous/index.md#-765560122%2FFunctions%2F863896225) | [androidJvm]<br>fun [requestOpModeStop](../-mono-test-e-group-autonomous/index.md#-765560122%2FFunctions%2F863896225)() |
| [resetRuntime](../-mono-test-e-group-autonomous/index.md#1757147609%2FFunctions%2F863896225) | [androidJvm]<br>open fun [resetRuntime](../-mono-test-e-group-autonomous/index.md#1757147609%2FFunctions%2F863896225)() |
| [runOpMode](run-op-mode.md) | [androidJvm]<br>open override fun [runOpMode](run-op-mode.md)() |
| [sleep](../-mono-test-e-group-autonomous/index.md#-1955259651%2FFunctions%2F863896225) | [androidJvm]<br>fun [sleep](../-mono-test-e-group-autonomous/index.md#-1955259651%2FFunctions%2F863896225)(p0: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)) |
| [start](../-mono-test-e-group-autonomous/index.md#-1200709743%2FFunctions%2F863896225) | [androidJvm]<br>override fun [start](../-mono-test-e-group-autonomous/index.md#-1200709743%2FFunctions%2F863896225)() |
| [stop](../-mono-test-e-group-autonomous/index.md#841039173%2FFunctions%2F863896225) | [androidJvm]<br>override fun [stop](../-mono-test-e-group-autonomous/index.md#841039173%2FFunctions%2F863896225)() |
| [terminateOpModeNow](../-mono-test-e-group-autonomous/index.md#-167858447%2FFunctions%2F863896225) | [androidJvm]<br>fun [terminateOpModeNow](../-mono-test-e-group-autonomous/index.md#-167858447%2FFunctions%2F863896225)() |
| [updateTelemetry](../-mono-test-e-group-autonomous/index.md#1080474565%2FFunctions%2F863896225) | [androidJvm]<br>open fun [updateTelemetry](../-mono-test-e-group-autonomous/index.md#1080474565%2FFunctions%2F863896225)(p0: Telemetry) |
| [waitForStart](../-mono-test-e-group-autonomous/index.md#246695705%2FFunctions%2F863896225) | [androidJvm]<br>open fun [waitForStart](../-mono-test-e-group-autonomous/index.md#246695705%2FFunctions%2F863896225)() |

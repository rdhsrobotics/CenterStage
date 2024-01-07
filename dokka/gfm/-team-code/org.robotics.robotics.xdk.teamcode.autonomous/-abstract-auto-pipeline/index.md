//[TeamCode](../../../index.md)/[org.robotics.robotics.xdk.teamcode.autonomous](../index.md)/[AbstractAutoPipeline](index.md)

# AbstractAutoPipeline

abstract class [AbstractAutoPipeline](index.md) : LinearOpMode, System

#### Inheritors

| |
|---|
| [AutoPipelineBlueLeft](../../org.robotics.robotics.xdk.teamcode.autonomous.blue/-auto-pipeline-blue-left/index.md) |
| [AutoPipelineBlueRight](../../org.robotics.robotics.xdk.teamcode.autonomous.blue/-auto-pipeline-blue-right/index.md) |
| [V2ControlTestAutoMoveBackwards](../../org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.v2/-v2-control-test-auto-move-backwards/index.md) |
| [V2ControlTestAutoMoveDistance](../../org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.v2/-v2-control-test-auto-move-distance/index.md) |
| [V2ControlTestAutoMoveForward](../../org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.v2/-v2-control-test-auto-move-forward/index.md) |
| [V2ControlTestAutoStrafe](../../org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.v2/-v2-control-test-auto-strafe/index.md) |
| [V2ControlTestAutoTurn](../../org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.v2/-v2-control-test-auto-turn/index.md) |
| [AutoPipelineRedLeft](../../org.robotics.robotics.xdk.teamcode.autonomous.red/-auto-pipeline-red-left/index.md) |
| [AutoPipelineRedRight](../../org.robotics.robotics.xdk.teamcode.autonomous.red/-auto-pipeline-red-right/index.md) |
| [IsolatedAutoTesting](../../org.robotics.robotics.xdk.teamcode.autonomous.testing/-isolated-auto-testing/index.md) |

## Constructors

| | |
|---|---|
| [AbstractAutoPipeline](-abstract-auto-pipeline.md) | [androidJvm]<br>constructor() |

## Types

| Name | Summary |
|---|---|
| [V2](-v2/index.md) | [androidJvm]<br>inner class [V2](-v2/index.md) |

## Properties

| Name | Summary |
|---|---|
| [gamepad1](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#1533398127%2FProperties%2F863896225) | [androidJvm]<br>var [gamepad1](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#1533398127%2FProperties%2F863896225): Gamepad |
| [gamepad2](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#1564417934%2FProperties%2F863896225) | [androidJvm]<br>var [gamepad2](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#1564417934%2FProperties%2F863896225): Gamepad |
| [hardwareMap](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-1397214969%2FProperties%2F863896225) | [androidJvm]<br>var [hardwareMap](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-1397214969%2FProperties%2F863896225): HardwareMap |
| [isStarted](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#1892877380%2FProperties%2F863896225) | [androidJvm]<br>val [isStarted](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#1892877380%2FProperties%2F863896225): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [msStuckDetectInit](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#1483349743%2FProperties%2F863896225) | [androidJvm]<br>var [~~msStuckDetectInit~~](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#1483349743%2FProperties%2F863896225): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [msStuckDetectInitLoop](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-553001045%2FProperties%2F863896225) | [androidJvm]<br>var [~~msStuckDetectInitLoop~~](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-553001045%2FProperties%2F863896225): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [msStuckDetectLoop](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#363588571%2FProperties%2F863896225) | [androidJvm]<br>var [~~msStuckDetectLoop~~](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#363588571%2FProperties%2F863896225): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [msStuckDetectStart](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#427944857%2FProperties%2F863896225) | [androidJvm]<br>var [~~msStuckDetectStart~~](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#427944857%2FProperties%2F863896225): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [msStuckDetectStop](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-409341632%2FProperties%2F863896225) | [androidJvm]<br>var [~~msStuckDetectStop~~](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-409341632%2FProperties%2F863896225): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [stopRequested](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-1991443121%2FProperties%2F863896225) | [androidJvm]<br>val [stopRequested](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-1991443121%2FProperties%2F863896225): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [subsystems](subsystems.md) | [androidJvm]<br>open override val [subsystems](subsystems.md): [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;Subsystem&gt; |
| [telemetry](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#1097975490%2FProperties%2F863896225) | [androidJvm]<br>var [telemetry](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#1097975490%2FProperties%2F863896225): Telemetry |
| [time](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-591827947%2FProperties%2F863896225) | [androidJvm]<br>var [time](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-591827947%2FProperties%2F863896225): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |

## Functions

| Name | Summary |
|---|---|
| [bindModuleWith](../../org.robotics.robotics.xdk.teamcode.autonomous.testing/-isolated-auto-testing/index.md#1091412264%2FFunctions%2F863896225) | [androidJvm]<br>open fun [bindModuleWith](../../org.robotics.robotics.xdk.teamcode.autonomous.testing/-isolated-auto-testing/index.md#1091412264%2FFunctions%2F863896225)(@NotNullconsumer: TerminableConsumer) |
| [buildExecutionGroup](build-execution-group.md) | [androidJvm]<br>abstract fun [buildExecutionGroup](build-execution-group.md)(tapeSide: [TapeSide](../../org.robotics.robotics.xdk.teamcode.autonomous.detection/-tape-side/index.md)): RootExecutionGroup |
| [disposeOfAll](../../org.robotics.robotics.xdk.teamcode.autonomous.testing/-isolated-auto-testing/index.md#-1091733938%2FFunctions%2F863896225) | [androidJvm]<br>open fun [disposeOfAll](../../org.robotics.robotics.xdk.teamcode.autonomous.testing/-isolated-auto-testing/index.md#-1091733938%2FFunctions%2F863896225)(): [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;Subsystem&gt; |
| [getRuntime](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#503260448%2FFunctions%2F863896225) | [androidJvm]<br>open fun [getRuntime](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#503260448%2FFunctions%2F863896225)(): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [getTeamColor](get-team-color.md) | [androidJvm]<br>abstract fun [getTeamColor](get-team-color.md)(): [TeamColor](../../org.robotics.robotics.xdk.teamcode.autonomous.detection/-team-color/index.md) |
| [hardware](../hardware.md) | [androidJvm]<br>inline fun &lt;[T](../hardware.md) : HardwareDevice&gt; LinearOpMode.[hardware](../hardware.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [T](../hardware.md)<br>A little extension function to get a non-null HardwareDevice. |
| [idle](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-945285709%2FFunctions%2F863896225) | [androidJvm]<br>fun [idle](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-945285709%2FFunctions%2F863896225)() |
| [init](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-1617228809%2FFunctions%2F863896225) | [androidJvm]<br>override fun [init](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-1617228809%2FFunctions%2F863896225)() |
| [init_loop](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-493667936%2FFunctions%2F863896225) | [androidJvm]<br>override fun [init_loop](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-493667936%2FFunctions%2F863896225)() |
| [initializeAll](../../org.robotics.robotics.xdk.teamcode.autonomous.testing/-isolated-auto-testing/index.md#-1012677936%2FFunctions%2F863896225) | [androidJvm]<br>open fun [initializeAll](../../org.robotics.robotics.xdk.teamcode.autonomous.testing/-isolated-auto-testing/index.md#-1012677936%2FFunctions%2F863896225)(): [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;Subsystem&gt; |
| [internalPostInitLoop](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#832637297%2FFunctions%2F863896225) | [androidJvm]<br>open fun [~~internalPostInitLoop~~](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#832637297%2FFunctions%2F863896225)() |
| [internalPostLoop](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-180688735%2FFunctions%2F863896225) | [androidJvm]<br>open fun [~~internalPostLoop~~](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-180688735%2FFunctions%2F863896225)() |
| [internalPreInit](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#958221314%2FFunctions%2F863896225) | [androidJvm]<br>open fun [~~internalPreInit~~](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#958221314%2FFunctions%2F863896225)() |
| [internalUpdateTelemetryNow](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#949931346%2FFunctions%2F863896225) | [androidJvm]<br>fun [internalUpdateTelemetryNow](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#949931346%2FFunctions%2F863896225)(p0: TelemetryMessage) |
| [loop](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#1557977315%2FFunctions%2F863896225) | [androidJvm]<br>override fun [loop](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#1557977315%2FFunctions%2F863896225)() |
| [move](move.md) | [androidJvm]<br>fun [move](move.md)(ticks: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)) |
| [opModeInInit](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#1015161614%2FFunctions%2F863896225) | [androidJvm]<br>fun [opModeInInit](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#1015161614%2FFunctions%2F863896225)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [opModeIsActive](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-911330061%2FFunctions%2F863896225) | [androidJvm]<br>fun [opModeIsActive](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-911330061%2FFunctions%2F863896225)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [register](../../org.robotics.robotics.xdk.teamcode.autonomous.testing/-isolated-auto-testing/index.md#-1973601324%2FFunctions%2F863896225) | [androidJvm]<br>open fun [register](../../org.robotics.robotics.xdk.teamcode.autonomous.testing/-isolated-auto-testing/index.md#-1973601324%2FFunctions%2F863896225)(vararg subsystem: Subsystem) |
| [requestOpModeStop](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-765560122%2FFunctions%2F863896225) | [androidJvm]<br>fun [requestOpModeStop](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-765560122%2FFunctions%2F863896225)() |
| [resetRuntime](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#1757147609%2FFunctions%2F863896225) | [androidJvm]<br>open fun [resetRuntime](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#1757147609%2FFunctions%2F863896225)() |
| [runOpMode](run-op-mode.md) | [androidJvm]<br>open override fun [runOpMode](run-op-mode.md)() |
| [setup](../../org.robotics.robotics.xdk.teamcode.autonomous.testing/-isolated-auto-testing/index.md#-1456822251%2FFunctions%2F863896225) | [androidJvm]<br>open override fun [setup](../../org.robotics.robotics.xdk.teamcode.autonomous.testing/-isolated-auto-testing/index.md#-1456822251%2FFunctions%2F863896225)(consumer: TerminableConsumer) |
| [sleep](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-1955259651%2FFunctions%2F863896225) | [androidJvm]<br>fun [sleep](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-1955259651%2FFunctions%2F863896225)(p0: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)) |
| [start](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-1200709743%2FFunctions%2F863896225) | [androidJvm]<br>override fun [start](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-1200709743%2FFunctions%2F863896225)() |
| [stop](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#841039173%2FFunctions%2F863896225) | [androidJvm]<br>override fun [stop](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#841039173%2FFunctions%2F863896225)() |
| [strafe](strafe.md) | [androidJvm]<br>fun [strafe](strafe.md)(ticks: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)) |
| [terminateOpModeNow](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-167858447%2FFunctions%2F863896225) | [androidJvm]<br>fun [terminateOpModeNow](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#-167858447%2FFunctions%2F863896225)() |
| [turn](turn.md) | [androidJvm]<br>fun [turn](turn.md)(ticks: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)) |
| [updateTelemetry](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#1080474565%2FFunctions%2F863896225) | [androidJvm]<br>open fun [updateTelemetry](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#1080474565%2FFunctions%2F863896225)(p0: Telemetry) |
| [waitForStart](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#246695705%2FFunctions%2F863896225) | [androidJvm]<br>open fun [waitForStart](../../org.robotics.robotics.xdk.teamcode.mono/-mono-test-e-group-autonomous/index.md#246695705%2FFunctions%2F863896225)() |

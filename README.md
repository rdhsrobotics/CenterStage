## 2024 Robot Code
**Project Structure:**
- [Subsystems](https://github.com/rdhsrobotics/ftc-rc-2024/tree/master/TeamCode/src/main/java/org/riverdell/robotics/xdk/opmodes/subsystem)
- [TeleOp](https://github.com/rdhsrobotics/ftc-rc-2024/blob/master/TeamCode/src/main/java/org/riverdell/robotics/xdk/opmodes/AbstractTeleOp.kt)
- **Configs:**
  - Instead of hardcoding numbers into our program, we put them in a `@Config` object. This allows us to edit these values live in a dashboard rather than rebuilding the entire program.
  - [Autonomous PID Config](https://github.com/rdhsrobotics/ftc-rc-2024/blob/master/TeamCode/src/main/java/org/robotics/robotics/xdk/asd/autonomous/utilities/AutoPipelineUtilities.kt)
  - [Claw/Extender/Airplane Launcher Config](https://github.com/rdhsrobotics/ftc-rc-2024/blob/master/TeamCode/src/main/java/org/robotics/robotics/xdk/asd/subsystem/claw/ClawExpansionConstants.kt)
- **Autonomous**:
  - [PID Implementations](https://github.com/rdhsrobotics/ftc-rc-2024/blob/master/TeamCode/src/main/java/org/riverdell/robotics/xdk/opmodes/autonomous/AbstractAutoPipeline.kt#L188)
  - [Element Detection](https://github.com/rdhsrobotics/ftc-rc-2024/blob/master/TeamCode/src/main/java/org/riverdell/robotics/xdk/opmodes/autonomous/detection/elements/GameElementDetection.java)
  - [PID Base](https://github.com/rdhsrobotics/ftc-rc-2024/blob/master/TeamCode/src/main/java/org/riverdell/robotics/xdk/opmodes/autonomous/controlsystem/PIDController.kt)
  - **Implementations:**
    - Each implementation contains two classes inside. The file starts with a configuration class with the name of the implementation. This is followed by the LinearOpMode with the state machine implementation.
    - [Blue](https://github.com/rdhsrobotics/ftc-rc-2024/tree/master/TeamCode/src/main/java/org/riverdell/robotics/xdk/opmodes/autonomous/blue)
    - [Red](https://github.com/rdhsrobotics/ftc-rc-2024/tree/master/TeamCode/src/main/java/org/riverdell/robotics/xdk/opmodes/autonomous/red)

## Tips:
- `lazy` functions are used throughout the entire robot code module for several use cases. Lazy values have the sole purpose of not instantaneously initializing the value when the class is instantiated. A lazy value will be created when it is first referenced in the program, and then the value will be cached for future use.
  - This is particularly useful in FTC programs because op mode classes are initialized on startup, not when you run the program. This means that we have to make sure our variables are initialized on the `runOpMode` method, not on class startup. Lazy's behavior allows us to achieve this.

## Mono
Mono is an internal library that handles all of our control systems.
- Autonomous structuring/state machines
- Driver control gamepad button mapping
- Robot hardware modularization with Subsystems

### State machines:
State machines are used in our autonomous implementations to plan out chains of tasks our robot takes *(execution groups)*. You need to know three key terms to understand our autonomous programs:
- `single`: A single task that is executed
- `simultaneous`: A group of tasks executed **simultaneously**.
- `consecutive`: A group of tasks executed **consecutively**.

You can start writing your execution group tasks in the body of a `Mono.buildExecutionGroup { /* here */ }` call. Here is a simple example:
```kotlin
Mono.buildExecutionGroup {
    single("Forward to tape") {
        move(-BlueLeft.MoveForwardToTape)
    }
}
```

And we can make it more complex...
```kotlin
Mono.buildExecutionGroup {
    simultaneous("move into tape") {
        single("Forward to tape") {
            move(-BlueLeft.MoveForwardToTape)
        }

        single("set extender to intake") {
            clawSubsystem.toggleExtender(
                ExtendableClaw.ExtenderState.Intake,
                force = true
            )
        }
    }
}
```

And even more complex...
```kotlin
    simultaneous("strafe into drop position") {
        consecutive("strafe") {
            single("strafe into position") {
                strafe(-BlueLeft.StrafeIntoPosition)
            }
            single("sync into heading") {
                turn(BlueLeft.TurnTowardsBackboard)
            }
        }
        single("heighten elevator") {
            elevatorSubsystem.configureElevatorManually(BlueLeft.ZElevatorDropExpectedHeight)
        }
    }
```

### Gamepad Mappings:
Gamepad mappings are used in our TeleOp code to make writing command executions triggered by the driver 1/2 gamepads easier. These gamepad mappings are created with a builder-style system. A simple example:
```kotlin
val gamepad = Mono.commands(gamepad1) // declare your GamepadCommands instance

// launch the airplane when you click the square button, and
// set it back to armed when you release the square button.
gp1Commands
    .where(ButtonType.PlayStationSquare)
    .triggers {
        paperPlaneLauncher.launch()
    }
    .andIsHeldUntilReleasedWhere {
        paperPlaneLauncher.reset()
    }
```

This builder system makes it very easy for us to build deposit presets in TeleOp:
```kotlin
//depositPresetReleaseOnElevatorHeight is hidden
gp2Commands
    .where(ButtonType.DPadLeft)
    .depositPresetReleaseOnElevatorHeight(-630)

gp2Commands
    .where(ButtonType.DPadUp)
    .depositPresetReleaseOnElevatorHeight(-850)

gp2Commands
    .where(ButtonType.DPadRight)
    .depositPresetReleaseOnElevatorHeight(-1130)
```

### Subsystems:
Our robot code is split up into multiple classes through Subsystems. Subsystems are independent components of the robot. An example of a subsystem is: `AirplaneLauncher`.

#### Lifecycle:
- Subsystem is registered in the subsystem registry (essentially a list of available subsystems we keep control over).
- When you press init on the opmode, subsystems are **initialized**.
  - Calls the `doInitialize()` function within the Subsystem implementation.
- When the opmode is stopped, the subsystems are **disposed**.
  - Calls the `dispose()` function within the Subsystem implementation.

Here is the real implementation of the `AirplaneLauncher`:
```kotlin
class AirplaneLauncher(private val opMode: LinearOpMode) : AbstractSubsystem()
{
    private val backingServo by lazy {
        opMode.hardware<Servo>("launcher")
    }

    /**
     * Puts the airplane launcher servo to the LAUNCHED position.
     */
    fun launch()
    {
        backingServo.position = ClawExpansionConstants.MAX_PLANE_POSITION
    }

    /**
     * Puts the airplane launcher servo to the ARMED position.
     */
    fun arm()
    {
        backingServo.position = ClawExpansionConstants.DEFAULT_PLANE_POSITION
    }

    /**
     * Initialize the servo when the subsystem is initalized, and arm the airplane launcher.
     */
    override fun doInitialize()
    {
        arm()
    }

    // do nothing since servos don't need to be reset on end
    override fun dispose()
    {

    }
}
```



package org.riverdell.robotics.xdk.opmodes.autonomous.utilities

import com.acmerobotics.dashboard.config.Config

@Config
object AutoPipelineUtilities
{
    @JvmField var PID_MOVEMENT_KP = 0.03
    @JvmField var PID_MOVEMENT_KI = 0.00001
    @JvmField var PID_MOVEMENT_KD = 0.03
    @JvmField var PID_MOVEMENT_TOLERANCE = 10
    @JvmField var PID_MOVEMENT_MIN_VELOCITY = 15.0

    @JvmField var PID_DISTANCE_KP = 0.03
    @JvmField var PID_DISTANCE_KI = 0.00001
    @JvmField var PID_DISTANCE_KD = 0.05
    @JvmField var PID_DISTANCE_TOLERANCE = 10
    @JvmField var PID_DISTANCE_MIN_VELOCITY = 15.0

    @JvmField var PID_ROTATION_KP = 0.02
    @JvmField var PID_ROTATION_KI = 0.0
    @JvmField var PID_ROTATION_KD = 0.005
    @JvmField var PID_ROTATION_TOLERANCE = 2
    @JvmField var PID_ROTATION_MIN_VELOCITY = 25.0

    @JvmField var RAMP_UP_SPEED = 2000.0

    @JvmField
    var MOVEMENT_MAX_ERROR = 15.0

    @Deprecated("v2")
    @JvmField
    var ROTATION_END_VELOCITY = 0.15

    @Deprecated("v2")
    @JvmField
    var ROTATION_END_YAW = 1.0
}
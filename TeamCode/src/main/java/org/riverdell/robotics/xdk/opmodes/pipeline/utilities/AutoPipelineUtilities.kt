package org.riverdell.robotics.xdk.opmodes.pipeline.utilities

import com.acmerobotics.dashboard.config.Config

@Config
object AutoPipelineUtilities
{
    @JvmField var PID_MOVEMENT_KP = 0.03
    @JvmField var PID_MOVEMENT_KI = 0.00001
    @JvmField var PID_MOVEMENT_KD = 0.03
    @JvmField var PID_MOVEMENT_TOLERANCE = 10
    @JvmField var PID_MOVEMENT_MAX_ERROR = 99

    @JvmField var PID_DISTANCE_KP = 0.03
    @JvmField var PID_DISTANCE_KI = 0.00001
    @JvmField var PID_DISTANCE_KD = 0.05
    @JvmField var PID_DISTANCE_TOLERANCE = 10
    @JvmField var PID_DISTANCE_MAX_ERROR = 99

    @JvmField var PID_ROTATION_KP = 0.03
    @JvmField var PID_ROTATION_KI = 0.00001
    @JvmField var PID_ROTATION_KD = 0.05
    @JvmField var PID_ROTATION_TOLERANCE = 0.5
    @JvmField var PID_ROTATION_MAX_ERROR = 99

    @JvmField var RAMP_UP_SPEED = 2000.0

    @Deprecated("v2")
    @JvmField
    var MOVEMENT_MAX_ERROR = 15.0

    @Deprecated("v2")
    @JvmField
    var MOVEMENT_MAX_VELOCITY = 10.0

    @Deprecated("v2")
    @JvmField
    var ROTATION_END_VELOCITY = 0.15

    @Deprecated("v2")
    @JvmField
    var ROTATION_END_YAW = 1.0
}
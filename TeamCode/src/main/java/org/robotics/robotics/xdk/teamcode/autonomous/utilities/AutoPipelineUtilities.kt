package org.robotics.robotics.xdk.teamcode.autonomous.utilities

import com.acmerobotics.dashboard.config.Config

@Config
object AutoPipelineUtilities
{
    @JvmField var PID_MOVEMENT_KP = 0.002
    @JvmField var PID_MOVEMENT_KI = 0.0
    @JvmField var PID_MOVEMENT_KD = 0.005
    @JvmField var PID_MOVEMENT_TOLERANCE = 10.0
    @JvmField var PID_MOVEMENT_MIN_VELOCITY = 10.0

    @JvmField var PID_DISTANCE_KP = 0.03
    @JvmField var PID_DISTANCE_KI = 0.00001
    @JvmField var PID_DISTANCE_KD = 0.03
    @JvmField var PID_DISTANCE_TOLERANCE = 10.0
    @JvmField var PID_DISTANCE_MIN_VELOCITY = 15.0

    @JvmField var PID_ROTATION_KP = 0.02
    @JvmField var PID_ROTATION_KI = 0.0
    @JvmField var PID_ROTATION_KD = 0.075
    @JvmField var PID_ROTATION_TOLERANCE = 0.5
    @JvmField var PID_ROTATION_MIN_VELOCITY = 0.5

    @JvmField var RAMP_UP_SPEED = 700.0

    @JvmField var APRIL_TAG_TURN_FACTOR = 0.0
    @JvmField var APRIL_TAG_FORWARD_FACTOR = 0.02
    @JvmField var APRIL_TAG_STRAFE_FACTOR = 0.00

    @JvmField var APRIL_TAG_LOCALIZATION_THRESHOLD = 5.0

    /*@JvmField var MOTION_PROFILE_ACCEL = 500.0
    @JvmField var MOTION_PROFILE_DECEL = 500.0
    @JvmField var MOTION_PROFILE_VELOCITY = 1000.0*/
}
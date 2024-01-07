package org.robotics.robotics.xdk.asd.subsystem.claw

import com.acmerobotics.dashboard.config.Config

@Config
object ClawExpansionConstants
{
    @JvmField var PRELOAD_EXTENDER_POSITION = 1.0
    @JvmField var MIN_EXTENDER_POSITION = 0.69
    @JvmField var INTERMEDIATE_EXTENDER_POSITION = 0.52
    @JvmField var MAX_EXTENDER_POSITION = 0.48

    @JvmField var CLAW_MOTION_PROFILE_VELOCITY = 5.0
    @JvmField var CLAW_MOTION_PROFILE_ACCEL = 5.0
    @JvmField var CLAW_MOTION_PROFILE_DECEL = 2.0

    @JvmField var CLAW_FINGER_PROFILE_VELOCITY = 5.0
    @JvmField var CLAW_FINGER_PROFILE_ACCEL = 33.0
    @JvmField var CLAW_FINGER_PROFILE_DECEL = 50.0

    @JvmField var CLOSED_LEFT_CLAW = 0.5
    @JvmField var OPEN_LEFT_CLAW = 0.7

    @JvmField var CLOSED_RIGHT_CLAW = 0.9
    @JvmField var OPEN_RIGHT_CLAW = 0.7

    @JvmField var DEFAULT_PLANE_POSITION = 1.0
    @JvmField var MAX_PLANE_POSITION = 0.8
}
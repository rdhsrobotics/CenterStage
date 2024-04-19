package org.robotics.robotics.xdk.teamcode.subsystem.claw

import com.acmerobotics.dashboard.config.Config

@Config
object ClawExpansionConstants
{
    @JvmField var Test = 0.9

    @JvmField var CLAW_MOTION_PROFILE_VELOCITY = 2.0
    @JvmField var CLAW_MOTION_PROFILE_ACCEL = 8.0
    @JvmField var CLAW_MOTION_PROFILE_DECEL = 8.0

    @JvmField var CLAW_FINGER_PROFILE_VELOCITY = 20.0
    @JvmField var CLAW_FINGER_PROFILE_ACCEL = 33.0
    @JvmField var CLAW_FINGER_PROFILE_DECEL = 50.0

    @JvmField var CLOSED_LEFT_CLAW = 0.14
    @JvmField var OPEN_LEFT_CLAW = 0.28
    @JvmField var OPEN_LEFT_CLAW_INTAKE = 0.33

    @JvmField var CLOSED_RIGHT_CLAW = 0.96
    @JvmField var OPEN_RIGHT_CLAW = 0.80
    @JvmField var OPEN_RIGHT_CLAW_INTAKE = 0.78
}
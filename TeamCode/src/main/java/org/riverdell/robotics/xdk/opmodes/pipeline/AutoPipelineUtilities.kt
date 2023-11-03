package org.riverdell.robotics.xdk.opmodes.pipeline

import com.acmerobotics.dashboard.config.Config

@Config
object AutoPipelineUtilities
{
    @JvmField var PID_MOVEMENT_KP = 0.06
    @JvmField var PID_MOVEMENT_KD = 0.02

    @JvmField var PID_ROTATION_KP = 0.05
    @JvmField var PID_ROTATION_KI = -0.013
    @JvmField var PID_ROTATION_KD = 0.002

    @JvmField var MOVEMENT_RAMP_UP_TIME = 300.0
    @JvmField var MOVEMENT_MAX_ERROR = 15.0
    @JvmField var MOVEMENT_MAX_VELOCITY = 15.0

    @JvmField var ROTATION_RAMP_UP_TIME = 300.0
    @JvmField var ROTATION_END_VELOCITY = 0.15
    @JvmField var ROTATION_END_YAW = 1.0
}
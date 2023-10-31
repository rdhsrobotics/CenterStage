package org.riverdell.robotics.xdk.opmodes.pipeline

import com.acmerobotics.dashboard.config.Config

@Config
object AutoPipelineUtilities
{
    @JvmField var UNITS_PER_INCH_STRAFE = 153.5
    @JvmField var UNITS_PER_INCH_FORWARD = 89.127
    @JvmField var UNITS_PER_DEGREE_TURN = 21.111

    @JvmField var PID_KP = -0.002
    @JvmField var PID_KI = 0.00001
    @JvmField var PID_KD = 0.05
}
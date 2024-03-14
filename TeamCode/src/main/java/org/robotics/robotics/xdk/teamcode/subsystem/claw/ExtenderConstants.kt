package org.robotics.robotics.xdk.teamcode.subsystem.claw

import com.acmerobotics.dashboard.config.Config

@Config
object ExtenderConstants
{
    @JvmField var PRELOAD_EXTENDER_POSITION = 1.0
    @JvmField var MIN_EXTENDER_POSITION = 0.875
    @JvmField var INTERMEDIATE_EXTENDER_POSITION = 0.7
    @JvmField var MAX_EXTENDER_POSITION = 0.65
}
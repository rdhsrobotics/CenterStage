package org.robotics.robotics.xdk.teamcode.subsystem.claw

import com.acmerobotics.dashboard.config.Config

@Config
object ExtenderConstants
{
    // .87 PRELOAD -> .77 MIN -> .6 INTERMED -> .37 MAX
    @JvmField var PRELOAD_EXTENDER_POSITION = 0.87
    @JvmField var MIN_EXTENDER_POSITION = 0.68
    @JvmField var FLOAT_EXTENDER_POSITION = 0.4
    @JvmField var INTERMEDIATE_EXTENDER_POSITION = 0.5
    @JvmField var MAX_EXTENDER_POSITION = 0.32
}
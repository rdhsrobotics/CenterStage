package org.riverdell.robotics.xdk.opmodes.subsystem.claw

import com.acmerobotics.dashboard.config.Config

@Config
object ClawExpansionConstants
{
    @JvmField var MIN_EXTENDER_POSITION = 0.0
    @JvmField var INTERMEDIATE_EXTENDER_POSITION = 0.13
    @JvmField var MAX_EXTENDER_POSITION = 0.28

    @JvmField var CLOSED_LEFT_CLAW = 0.26
    @JvmField var OPEN_LEFT_CLAW = 0.45

    @JvmField var CLOSED_RIGHT_CLAW = 0.8
    @JvmField var OPEN_RIGHT_CLAW = 0.6

    @JvmField var DEFAULT_PLANE_POSITION = 0.6
    @JvmField var MAX_PLANE_POSITION = 1.0
}
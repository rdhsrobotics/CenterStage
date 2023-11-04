package org.riverdell.robotics.xdk.opmodes.subsystem.claw

import com.acmerobotics.dashboard.config.Config

@Config
object ClawExpansionConstants
{
    @JvmField var MAX_CLAW_POSITION = 0.5

    @JvmField var MIN_EXTENDER_POSITION = 0.20
    @JvmField var MAX_EXTENDER_POSITION = 0.4060

    @JvmField var DEFAULT_PLANE_POSITION = 0.6
    @JvmField var MAX_PLANE_POSITION = 0.9
}
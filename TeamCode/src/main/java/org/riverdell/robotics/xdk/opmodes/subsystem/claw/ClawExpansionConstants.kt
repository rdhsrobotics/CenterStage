package org.riverdell.robotics.xdk.opmodes.subsystem.claw

import com.acmerobotics.dashboard.config.Config

@Config
object ClawExpansionConstants
{
    @JvmField var MAX_CLAW_POSITION = 0.5
    @JvmField var MIN_EXTENDER_POSITION = 7.2
    @JvmField var MAX_EXTENDER_POSITION = 23.0
}
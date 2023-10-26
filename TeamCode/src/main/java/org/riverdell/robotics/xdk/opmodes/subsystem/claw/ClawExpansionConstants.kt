package org.riverdell.robotics.xdk.opmodes.subsystem.claw

import com.acmerobotics.dashboard.config.Config

@Config
object ClawExpansionConstants
{
    @JvmField var MAX_DEGREE_CLAW = 75.0
    @JvmField var MIN_DEGREE_CLAW = 0.0

    @JvmField var MAX_DEGREE_EXTENDER = 90.0
    @JvmField var MIN_DEGREE_EXTENDER = 20.0
}
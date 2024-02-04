package org.robotics.robotics.xdk.teamcode.subsystem.passivehang

import com.acmerobotics.dashboard.config.Config

@Config
object PassiveHangConstants
{
    @JvmField var LEFT_RETRACTOR_ARMED = 1.0
    @JvmField var LEFT_RETRACTOR_DEPLOYED = 0.8

    @JvmField var RIGHT_RETRACTOR_ARMED = 0.8
    @JvmField var RIGHT_RETRACTOR_DEPLOYED = 1.0
}
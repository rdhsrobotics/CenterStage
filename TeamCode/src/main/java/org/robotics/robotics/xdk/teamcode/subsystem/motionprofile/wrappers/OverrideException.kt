package org.robotics.robotics.xdk.teamcode.subsystem.motionprofile.wrappers

import java.lang.RuntimeException

class OverrideException(val cancelled: Boolean = false) : RuntimeException()
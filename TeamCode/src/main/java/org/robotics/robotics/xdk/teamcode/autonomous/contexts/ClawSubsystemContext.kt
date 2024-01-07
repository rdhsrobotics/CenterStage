package org.robotics.robotics.xdk.teamcode.autonomous.contexts

import io.liftgate.robotics.mono.pipeline.StageContext
import org.robotics.robotics.xdk.teamcode.subsystem.claw.ExtendableClaw
import org.robotics.robotics.xdk.teamcode.subsystem.motionprofile.wrappers.MotionProfiledServo

class RightClawFinger(claw: ExtendableClaw) : ClawSubsystemContext(claw.backingClawOpenerRight)
class LeftClawFinger(claw: ExtendableClaw) : ClawSubsystemContext(claw.backingClawOpenerLeft)
class BothClawFinger(claw: ExtendableClaw) : ClawSubsystemContext(claw.backingClawOpenerLeft, claw.backingClawOpenerRight)

class ExtenderContext(claw: ExtendableClaw) : ClawSubsystemContext(claw.backingExtender)

abstract class ClawSubsystemContext(
    private vararg val motionProfiledServo: MotionProfiledServo
) : StageContext
{
    override fun dispose()
    {

    }

    override fun requiresAtLeast() = 50L
    override fun timesOutAfter() = 1500L

    override fun isCompleted() = motionProfiledServo
        .any { !it.isTargetMotionActive() }
}
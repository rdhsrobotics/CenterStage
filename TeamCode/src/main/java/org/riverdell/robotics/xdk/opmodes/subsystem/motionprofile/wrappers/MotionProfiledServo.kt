package org.riverdell.robotics.xdk.opmodes.subsystem.motionprofile.wrappers

import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.ElapsedTime
import org.riverdell.robotics.xdk.opmodes.subsystem.motionprofile.AsymmetricMotionProfile
import org.riverdell.robotics.xdk.opmodes.subsystem.motionprofile.ProfileConstraints

class MotionProfiledServo(
    private val servo: Servo,
    private val constraints: () -> ProfileConstraints
)
{
    private var motionProfile: AsymmetricMotionProfile? = null
    private var timer: ElapsedTime? = null

    fun unwrapServo() = servo
    fun setMotionProfileTarget(target: Double)
    {
        motionProfile = AsymmetricMotionProfile(
            servo.position,
            target,
            constraints()
        )
    }

    fun runPeriodic()
    {
        if (motionProfile == null)
        {
            timer = null
            return
        }

        if (timer == null)
        {
            timer = ElapsedTime()
        }

        val position = motionProfile!!.calculate(timer!!.time())
        servo.position = position.x

        if (servo.position == motionProfile!!.finalPosition)
        {
            motionProfile = null
        }
    }

    fun cancelMotionProfile()
    {
        motionProfile = null
        timer = ElapsedTime()
    }

    fun isTargetMotionActive() = motionProfile != null

    fun setTarget(targetPosition: Double)
    {
        servo.position = targetPosition
    }
}
package org.riverdell.robotics.xdk.opmodes.subsystem.claw

import com.arcrobotics.ftclib.hardware.SimpleServo
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.Subsystem
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ClawExpansionConstants.MAX_DEGREE_CLAW
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ClawExpansionConstants.MAX_DEGREE_EXTENDER
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ClawExpansionConstants.MIN_DEGREE_CLAW
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ClawExpansionConstants.MIN_DEGREE_EXTENDER

class ExtendableClaw(private val opMode: LinearOpMode) : Subsystem
{
    private val backingExtender by lazy {
        SimpleServo(
            opMode.hardwareMap,
            "extender",
            MIN_DEGREE_EXTENDER, MAX_DEGREE_EXTENDER
        )
    }

    private val backingClawOpener by lazy {
        SimpleServo(
            opMode.hardwareMap,
            "claw",
            MIN_DEGREE_CLAW, MAX_DEGREE_CLAW
        )
    }

    override fun composeStageContext() = object : StageContext
    {
        override fun isCompleted() = true
    }

    override fun initialize()
    {
        backingExtender.turnToAngle(20.0)
        backingClawOpener.turnToAngle(0.0)
    }

    /**
     * Expand claw by the trigger amount. Accepts an expansion
     * range of [0.0, 1.0].
     */
    fun expandClaw(expandsTo: Double)
    {
        backingClawOpener.turnToAngle(MAX_DEGREE_CLAW * expandsTo)
    }

    override fun isCompleted() = true
}
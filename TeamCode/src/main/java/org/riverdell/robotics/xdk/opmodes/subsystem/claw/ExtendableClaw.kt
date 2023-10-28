package org.riverdell.robotics.xdk.opmodes.subsystem.claw

import com.arcrobotics.ftclib.hardware.SimpleServo
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.pipeline.StageContext
import io.liftgate.robotics.mono.subsystem.Subsystem
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ClawExpansionConstants.MAX_DEGREE_CLAW
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ClawExpansionConstants.MIN_DEGREE_CLAW

class ExtendableClaw(private val opMode: LinearOpMode) : Subsystem
{
    private val backingExtender by lazy {
        SimpleServo(
            opMode.hardwareMap,
            "extender",
            20.0, 45.0
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
        backingExtender.turnToAngle(0.0)
        backingClawOpener.turnToAngle(0.0)
    }

    fun turnExtenderToAngle(expandsTo: Double)
    {
        backingExtender.turnToAngle(30 * expandsTo)
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
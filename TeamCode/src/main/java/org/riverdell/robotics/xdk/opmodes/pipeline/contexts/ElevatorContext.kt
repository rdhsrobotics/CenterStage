package org.riverdell.robotics.xdk.opmodes.pipeline.contexts

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import io.liftgate.robotics.mono.pipeline.StageContext

class ElevatorContext(private val motor: DcMotor, private val opMode: LinearOpMode) : StageContext
{
    override fun dispose()
    {
        motor.power = 0.0
    }

    // TODO: NOT WORK 
    override fun isCompleted() = !motor.isBusy && !opMode.isStopRequested
}
package org.riverdell.robotics.xdk.opmodes.pipeline.contexts

import com.qualcomm.robotcore.hardware.DcMotor
import io.liftgate.robotics.mono.pipeline.StageContext

class ElevatorContext(private val motor: DcMotor) : StageContext
{
    override fun dispose()
    {
        motor.power = 0.0
    }

    override fun isCompleted() = !motor.isBusy
}
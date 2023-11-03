package org.riverdell.robotics.xdk.opmodes.pipeline.contexts

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import io.liftgate.robotics.mono.pipeline.StageContext

class DrivebaseContext(private val motors: List<DcMotor>, private val opMode: LinearOpMode) : StageContext
{
    override fun dispose()
    {
        motors.onEach {
            it.power = 0.0
            it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        }
    }

    override fun isCompleted() = motors.none { it.isBusy } && !opMode.isStopRequested
}
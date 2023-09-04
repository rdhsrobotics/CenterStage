package org.riverdell.robotics.opmode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import io.liftgate.ftc.scripting.KotlinScript.ImpliedVariable
import io.liftgate.ftc.scripting.opmode.DevLinearOpMode

@TeleOp(
    name = "kts - Dev Test",
    group = "scripted"
)
class DevTeleOp : DevLinearOpMode()
{
    override fun getImpliedVariables() = emptyList<ImpliedVariable>()
    override fun getScriptName() = "Dev.kts"
}

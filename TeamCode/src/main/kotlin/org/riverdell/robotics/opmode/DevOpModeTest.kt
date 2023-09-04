package org.riverdell.robotics.opmode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import io.liftgate.ftc.scripting.KotlinScript.ImpliedVariable
import io.liftgate.ftc.scripting.opmode.DevLinearOpMode

@TeleOp(
    name = "DevTest-Scripted",
    group = "Scripted"
)
class DevOpModeTest : DevLinearOpMode()
{
    override fun getImpliedVariables() = mutableListOf<ImpliedVariable>()
    override fun getScriptName() = "Dev.kts"
}
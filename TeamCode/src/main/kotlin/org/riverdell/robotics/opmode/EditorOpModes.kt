package org.riverdell.robotics.opmode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import io.liftgate.ftc.scripting.opmode.editor.StartEditorOpMode
import io.liftgate.ftc.scripting.opmode.editor.StopEditorOpMode

/**
 * @author GrowlyX
 * @since 9/4/2023
 */
@TeleOp(name = "Start Scripting Editor", group = "script:management")
class StartEditor : StartEditorOpMode()

@TeleOp(name = "Stop Scripting Editor", group = "script:management")
class StopEditor : StopEditorOpMode()

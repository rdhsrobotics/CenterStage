package org.riverdell.robotics.xdk.opmodes.autonomous.blue

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.pipeline.consecutive
import io.liftgate.robotics.mono.pipeline.simultaneous
import io.liftgate.robotics.mono.pipeline.single
import org.riverdell.robotics.xdk.opmodes.Global
import org.riverdell.robotics.xdk.opmodes.autonomous.AbstractAutoPipeline
import org.riverdell.robotics.xdk.opmodes.autonomous.detection.TapeSide
import org.riverdell.robotics.xdk.opmodes.autonomous.detection.TeamColor
import org.riverdell.robotics.xdk.opmodes.autonomous.contexts.ExtenderContext
import org.riverdell.robotics.xdk.opmodes.autonomous.contexts.LeftClawFinger
import org.riverdell.robotics.xdk.opmodes.autonomous.contexts.RightClawFinger
import org.riverdell.robotics.xdk.opmodes.autonomous.red.RedRight
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ExtendableClaw

/**
 * @author Subham
 * @since 10/23/2023
 */
@Config
object BlueLeft
{
    @JvmField
    var MoveForwardToTape = 925.0
    @JvmField
    var MoveBackFromTape = -725.0
    @JvmField
    var TurnTowardsBackboard = -90.0
    @JvmField
    var MoveTowardsBackboard = 3 * 1025.0
    @JvmField
    var StrafeIntoPosition = -1000.0
    @JvmField
    var MoveSlightlyIntoBackboard = 100.0
    @JvmField
    var GoToParkingZone = 250.0

    @JvmField var StrafeIntoParkingZone = 1125.0

    @JvmField
    var ZElevatorDropExpectedHeight = 0.5
    @JvmField
    var ZTapeLeftTurnAmount = -55.0
    @JvmField
    var ZTapeRightTurnAmount = 65.0
}

@Autonomous(
    name = "Blue | Player 1",
    group = "Blue",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineBlueLeft : AbstractAutoPipeline()
{
    fun getTapeSideTurnPosition(tapeSide: TapeSide) = when (tapeSide)
    {
        TapeSide.Middle -> 0.0
        TapeSide.Left -> BlueLeft.ZTapeLeftTurnAmount
        TapeSide.Right -> BlueLeft.ZTapeRightTurnAmount
    }

    override fun getTeamColor() = TeamColor.Red
    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono
        .buildExecutionGroup {
            simultaneous("move into tape") {
                single("Forward to tape") {
                    move(-BlueLeft.MoveForwardToTape)
                }

                single("intermed") {
                    clawSubsystem.toggleExtender(
                        ExtendableClaw.ExtenderState.Intake,
                        force = true
                    )
                    Thread.sleep(125L)
                }
            }

            single("turn if required") {
                val turnPosition = getTapeSideTurnPosition(tapeSide)
                if (tapeSide == TapeSide.Left)
                {
                    move(125.0)
                }

                if (turnPosition == 0.0)
                {
                    move(100.0)
                    return@single
                }

                turn(turnPosition)
            }

            single("drop shit") {
                clawSubsystem.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Right,
                    ExtendableClaw.ClawState.Open
                )

                Thread.sleep(400)

                clawSubsystem.toggleExtender(
                    ExtendableClaw.ExtenderState.Deposit,
                    force = true
                )
            }

            simultaneous("turn back if required and do something else") {
                single("close lol") {
                    clawSubsystem.updateClawState(
                        ExtendableClaw.ClawStateUpdate.Right,
                        ExtendableClaw.ClawState.Closed
                    )
                }

                single("turn back if required") {
                    val turnPosition = getTapeSideTurnPosition(tapeSide)
                    if (turnPosition == 0.0)
                    {
                        return@single
                    }

                    turn(0.0)
                }
            }

            simultaneous("move back from tape") {
                single("move back") {
                    move(-BlueLeft.MoveBackFromTape)
                }

                single("af") {
                    clawSubsystem.toggleExtender(
                        ExtendableClaw.ExtenderState.Deposit,
                        force = true
                    )
                }
            }

            single("turn lol") {
                turn(BlueLeft.TurnTowardsBackboard)
            }

            single("move towards backboard") {
                move(BlueLeft.MoveTowardsBackboard)
            }

            simultaneous("strafe into drop position") {
                consecutive("strafe") {
                    single("strafe into position") {
                        strafe(-BlueLeft.StrafeIntoPosition)
                    }

                    single("sync into heading") {
                        turn(BlueLeft.TurnTowardsBackboard)
                    }
                }

                single("heighten elevator") {
                    elevatorSubsystem.configureElevatorManually(BlueLeft.ZElevatorDropExpectedHeight)
                }
            }

            single("move slightly into backboard") {
                move(-BlueLeft.MoveSlightlyIntoBackboard)
            }

            single("drop pixel lol") {
                clawSubsystem.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Left,
                    ExtendableClaw.ClawState.Open
                )

                Thread.sleep(500L)
            }

            single("move back from into backboard") {
                move(BlueLeft.MoveSlightlyIntoBackboard)
            }

            simultaneous("update elevator and return") {
                single("elevator retraction") {
                    elevatorSubsystem.configureElevatorManually(0.0)
                }

                single("right claw reset") {
                    clawSubsystem.updateClawState(
                        ExtendableClaw.ClawStateUpdate.Left,
                        ExtendableClaw.ClawState.Closed
                    )
                }

                single("strafe back to before") {
                    strafe(BlueLeft.StrafeIntoParkingZone)
                }
            }


            single("correct heading again") {
                turn(BlueLeft.TurnTowardsBackboard)
            }

            single("move forward into parking zone") {
                move(BlueLeft.GoToParkingZone)
            }
        }
}
package org.riverdell.robotics.xdk.opmodes.autonomous.red

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
import org.riverdell.robotics.xdk.opmodes.autonomous.red.RedLeft.ZTapeLeftTurnAmount
import org.riverdell.robotics.xdk.opmodes.autonomous.red.RedLeft.ZTapeRightTurnAmount
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ExtendableClaw

/**
 * @author Subham
 * @since 10/23/2023
 */
@Config
object RedLeft
{
    @JvmField
    var MoveForwardToTape = 925.0
    @JvmField
    var MoveBackFromTape = -725.0
    @JvmField
    var TurnTowardsBackboard = -90.0
    @JvmField
    var MoveTowardsBackboard = -3 * 1025.0
    @JvmField
    var StrafeIntoPosition = -1000.0
    @JvmField
    var MoveSlightlyIntoBackboard = 200.0
    @JvmField
    var GoToParkingZone = -250.0
    @JvmField
    var StrafeIntoParkingZone = -1125.0

    @JvmField
    var ZElevatorDropExpectedHeight = 0.5
    @JvmField
    var ZTapeLeftTurnAmount = 55.0
    @JvmField
    var ZTapeRightTurnAmount = -65.0
}

@Autonomous(
    name = "Red | Player 2",
    group = "Red",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineRedLeft : AbstractAutoPipeline()
{
    private fun getTapeSideTurnPosition(tapeSide: TapeSide) = when (tapeSide)
    {
        TapeSide.Middle -> 0.0
        TapeSide.Left -> ZTapeLeftTurnAmount
        TapeSide.Right -> ZTapeRightTurnAmount
    }

    override fun getTeamColor() = TeamColor.Red
    override fun buildExecutionGroup(tapeSide: TapeSide) = Mono
        .buildExecutionGroup {
            simultaneous("move into tape") {
                single("Forward to tape") {
                    move(-RedLeft.MoveForwardToTape)
                }

                single("intermed") {
                    clawSubsystem.toggleExtender(
                        ExtendableClaw.ExtenderState.Intake,
                        force = true
                    )
                }
            }

            single("turn if required") {
                val turnPosition = getTapeSideTurnPosition(tapeSide)
                if (tapeSide == TapeSide.Right)
                {
                    move(125.0)
                }

                if (turnPosition == 0.0)
                {
                    return@single
                }

                turn(turnPosition)
            }

            single("drop shit") {
                clawSubsystem.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Right,
                    ExtendableClaw.ClawState.Open
                )

                Thread.sleep(500)

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
                    if (tapeSide == TapeSide.Right)
                    {
                        move(125.0)
                    }

                    if (turnPosition == 0.0)
                    {
                        return@single
                    }

                    turn(0.0)
                }
            }

            simultaneous("move back from tape") {
                single("move back") {
                    move(-RedLeft.MoveBackFromTape + 150)
                }

                single("af") {
                    clawSubsystem.toggleExtender(
                        ExtendableClaw.ExtenderState.Deposit,
                        force = true
                    )
                }
            }

            single("turn lol") {
                turn(RedLeft.TurnTowardsBackboard)
            }

            single("move towards backboard") {
                move(RedLeft.MoveTowardsBackboard)
            }

            simultaneous("strafe into drop position") {
                consecutive("strafe") {
                    single("strafe into position") {
                        strafe(-RedLeft.StrafeIntoPosition)
                    }

                    single("sync into heading") {
                        turn(RedLeft.TurnTowardsBackboard)
                    }
                }

                single("heighten elevator") {
                    elevatorSubsystem.configureElevatorManually(RedLeft.ZElevatorDropExpectedHeight)
                }
            }

            single("move slightly into backboard") {
                move(-RedLeft.MoveSlightlyIntoBackboard)
            }

            single("drop pixel lol") {
                clawSubsystem.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Left,
                    ExtendableClaw.ClawState.Open
                )

                Thread.sleep(500L)
            }

            simultaneous("reset elevator stuff") {
                single("move back from into backboard") {
                    move(RedLeft.MoveSlightlyIntoBackboard)
                }

                single("right claw reset") {
                    clawSubsystem.updateClawState(
                        ExtendableClaw.ClawStateUpdate.Left,
                        ExtendableClaw.ClawState.Closed
                    )
                }

                single("elevator retraction") {
                    elevatorSubsystem.configureElevatorManually(0.0)
                }
            }

            single("strafe back to before") {
                strafe(RedLeft.StrafeIntoParkingZone)
            }

            single("correct heading again") {
                turn(RedLeft.TurnTowardsBackboard)
            }

            single("move forward into parking zone") {
                move(RedLeft.GoToParkingZone)
            }
        }
}
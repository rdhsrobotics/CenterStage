package org.robotics.robotics.xdk.teamcode.autonomous.position

import io.liftgate.robotics.mono.pipeline.RootExecutionGroup
import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Pose
import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.PurePursuitPath
import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.FieldWaypoint
import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.WaypointLike

val Double.degrees: Double
    get() = Math.toRadians(this)

val Int.degrees: Double
    get() = Math.toRadians(this.toDouble())

fun RootExecutionGroup.navigateTo(pose: Pose, optBlock: PositionCommand.() -> Unit = {}) =
    PositionCommand(pose, this).apply(optBlock).execute()

fun RootExecutionGroup.purePursuitNavigateTo(vararg waypoints: WaypointLike, optBlock: PositionCommand.() -> Unit = {}) =
    PurePursuitCommand(this, PurePursuitPath(*waypoints)).apply(optBlock).execute()
package org.robotics.robotics.xdk.teamcode.autonomous.position

import io.liftgate.robotics.mono.pipeline.RootExecutionGroup
import org.robotics.robotics.xdk.teamcode.autonomous.geometry.Pose
import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.PurePursuitPath
import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.Waypoint

val Double.degrees: Double
    get() = Math.toRadians(this)

val Int.degrees: Double
    get() = Math.toRadians(this.toDouble())

fun RootExecutionGroup.navigateTo(pose: Pose) =
    PositionCommand(pose, this).execute()

fun RootExecutionGroup.purePursuitNavigateTo(vararg waypoints: Waypoint) =
    PurePursuitCommand(this, PurePursuitPath(*waypoints)).execute()
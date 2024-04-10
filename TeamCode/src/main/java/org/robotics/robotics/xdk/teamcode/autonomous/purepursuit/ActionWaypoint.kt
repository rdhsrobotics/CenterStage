package org.robotics.robotics.xdk.teamcode.autonomous.purepursuit

data class ActionWaypoint(val action: () -> Unit) : WaypointLike
{
    var afterIndex = ""
    var hasExecuted = false
}
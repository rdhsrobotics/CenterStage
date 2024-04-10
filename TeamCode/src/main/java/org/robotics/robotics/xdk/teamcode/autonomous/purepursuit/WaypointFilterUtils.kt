package org.robotics.robotics.xdk.teamcode.autonomous.purepursuit

fun List<WaypointLike>.populateAndExtractActions() =
    toList().onEachIndexed { index, waypointLike ->
        if (waypointLike is ActionWaypoint)
        {
            val prev = this@populateAndExtractActions.getOrNull(index - 1)
            if (prev is FieldWaypoint)
            {
                waypointLike.afterIndex = prev.id
            }
        }
    }.filterIsInstance<ActionWaypoint>()

fun List<ActionWaypoint>.findWithIndexIncomplete(id: String) = firstOrNull { !it.hasExecuted && it.afterIndex == id }
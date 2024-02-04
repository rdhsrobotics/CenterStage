package org.robotics.robotics.xdk.teamcode.autonomous.shared

import com.acmerobotics.dashboard.config.Config

@Config
object GlobalConstants
{
    @JvmField
    var MoveForwardToSpikeMark = 975.0

    @JvmField
    var FarMoveTowardsBackboard = 3 * 1025.0
    @JvmField
    var CloseMoveTowardsBackboard = 1025.0
    @JvmField
    var TurnToSpikeMark = 55.0

    // Scalars
    @JvmField
    var ScalarMoveSlightlyIntoBackboard = 600.0

    @JvmField
    var ScalarMoveIntoParkingZone = 700.0
    @JvmField
    var ScalarExpectedElevatorDropHeight = 0.3
}
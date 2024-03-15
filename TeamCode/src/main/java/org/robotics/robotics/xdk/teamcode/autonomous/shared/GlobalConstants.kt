package org.robotics.robotics.xdk.teamcode.autonomous.shared

import com.acmerobotics.dashboard.config.Config

@Config
object GlobalConstants
{
    @JvmField
    var MoveForwardToSpikeMark = 620.0

    @JvmField
    var FarMoveTowardsBackboard = (3 * 615.0) - 20.0
    @JvmField
    var CloseMoveTowardsBackboard = 615.0
    @JvmField
    var TurnToSpikeMark = 65.0

    // Scalars
    @JvmField
    var ScalarMoveSlightlyIntoBackboard = 500.0

    @JvmField
    var ScalarMoveIntoParkingZone = 560.0
    @JvmField
    var ScalarExpectedElevatorDropHeight = 0.17
}
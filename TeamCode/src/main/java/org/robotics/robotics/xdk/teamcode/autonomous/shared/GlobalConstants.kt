package org.robotics.robotics.xdk.teamcode.autonomous.shared

import com.acmerobotics.dashboard.config.Config

@Config
object GlobalConstants
{
    @JvmField var MoveForwardToSpikeMark = 925.0
    @JvmField var MoveBackFromSpikeMark = -725.0

    @JvmField var FarMoveTowardsBackboard = 3 * 1025.0
    @JvmField var CloseMoveTowardsBackboard = 3 * 1025.0
    @JvmField var TurnToSpikeMark = 55.0

    // Scalars
    @JvmField var ScalarMoveSlightlyIntoBackboard = 100.0
    @JvmField var ScalarStrafeIntoPosition = 1000.0
    @JvmField var ScalarStrafeIntoParkingPosition = 1125.0
    @JvmField var ScalarMoveIntoParkingZone = 250.0
    @JvmField var ScalarExpectedElevatorDropHeight = 0.56
}
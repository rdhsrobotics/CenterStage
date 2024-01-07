package org.robotics.robotics.xdk.teamcode.autonomous.detection

enum class Direction(val heading: Double)
{
    Left(-90.0), Right(90.0);

    fun oppositeOf() = if (this == Left) Right else Left

    fun matches(tapeSide: TapeSide) =
        tapeSide == TapeSide.Left && this == Left ||
            tapeSide == TapeSide.Right && this == Right
}


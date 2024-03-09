package org.robotics.robotics.xdk.teamcode.autonomous.detection

/**
 *                 .addTag(1, "BlueAllianceLeft",
 *                         2, new VectorF(60.25f, 41.41f, 4f), DistanceUnit.INCH,
 *                         new Quaternion(0.3536f, -0.6124f, 0.6124f, -0.3536f, 0))
 *                 .addTag(2, "BlueAllianceCenter",
 *                         2, new VectorF(60.25f, 35.41f, 4f), DistanceUnit.INCH,
 *                         new Quaternion(0.3536f, -0.6124f, 0.6124f, -0.3536f, 0))
 *                 .addTag(3, "BlueAllianceRight",
 *                         2, new VectorF(60.25f, 29.41f, 4f), DistanceUnit.INCH,
 *                         new Quaternion(0.3536f, -0.6124f, 0.6124f, -0.3536f, 0))
 *                 .addTag(4, "RedAllianceLeft",
 *                         2, new VectorF(60.25f, -29.41f, 4f), DistanceUnit.INCH,
 *                         new Quaternion(0.3536f, -0.6124f, 0.6124f, -0.3536f, 0))
 *                 .addTag(5, "RedAllianceCenter",
 *                         2, new VectorF(60.25f, -35.41f, 4f), DistanceUnit.INCH,
 *                         new Quaternion(0.3536f, -0.6124f, 0.6124f, -0.3536f, 0))
 *                 .addTag(6, "RedAllianceRight",
 *                         2, new VectorF(60.25f, -41.41f, 4f), DistanceUnit.INCH,
 *                         new Quaternion(0.3536f, -0.6124f, 0.6124f, -0.3536f, 0))
 */

val targetAprilTagIDs = mapOf(
    // Red values
    Direction.Left to mapOf(
        TapeSide.Left to 4,
        TapeSide.Middle to 5,
        TapeSide.Right to 6
    ),
    // Blue values                                                                                                                                 nbbbbbbbbbbbbbbbbbc
    Direction.Right to mapOf(
        TapeSide.Left to 1,
        TapeSide.Middle to 2,
        TapeSide.Right to 3
    )
)
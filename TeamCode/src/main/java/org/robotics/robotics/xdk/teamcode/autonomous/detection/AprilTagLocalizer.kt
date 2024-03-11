package org.robotics.robotics.xdk.teamcode.autonomous.detection

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import org.robotics.robotics.xdk.teamcode.autonomous.AbstractAutoPipeline
import java.util.concurrent.CompletableFuture

class AprilTagLocalizer(private val pipe: LinearOpMode)
{
    val processor =
        AprilTagProcessor.Builder()
            .setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
            .build()

    enum class RelocalizationResult
    {
        Complete, CouldNotFindOrLost
    }

    fun relocalize(targetId: Int, seconds: Int = 2, localizeBlock: (AprilTagDetection) -> Boolean) = CompletableFuture
        .supplyAsync {
            var startTime = System.currentTimeMillis()
            var hasFound = true

            while (!pipe.isStopRequested)
            {
                val detection = processor.detections.firstOrNull { it.id == targetId }

                if (detection == null)
                {
                    if (hasFound)
                    {
                        startTime = System.currentTimeMillis()
                        hasFound = false
                    }

                    if (System.currentTimeMillis() - startTime > seconds * 1000L)
                    {
                        return@supplyAsync RelocalizationResult.CouldNotFindOrLost
                    }

                    Thread.sleep(20L)
                    continue
                }

                hasFound = true

                val result = localizeBlock(detection)
                if (result)
                {
                    break
                }

                Thread.sleep(20L)
            }
        }
        .exceptionally {
            it.printStackTrace()
            return@exceptionally null
        }
}
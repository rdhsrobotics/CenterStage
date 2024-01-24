package org.robotics.robotics.xdk.teamcode.autonomous.controlsystem.safety

import com.google.common.collect.EvictingQueue

/**
 * Takes an evicting N-second sample of IMU values
 * continuously run during a match.
 *
 * @author GrowlyX
 * @since 1/20/2024
 */
data class EvictingSample(
    val size: Int,
    val requiredSampleTime: Long,
    val start: Long = System.currentTimeMillis(),
    val samples: EvictingQueue<Double> = EvictingQueue.create(size)
)
{
    enum class AnalyzeResult
    {
        NotNeeded, RequiresExit, CanContinue
    }

    fun analyze(): AnalyzeResult
    {
        if (System.currentTimeMillis() - start < requiredSampleTime)
        {
            return AnalyzeResult.NotNeeded
        }

        if (samples.asSequence().allEqual())
        {
            return AnalyzeResult.RequiresExit
        }

        return AnalyzeResult.CanContinue
    }

    /**
     * Evicts the eldest entry in the [samples] queue and adds a new entry to the queue.
     */
    fun submit(sample: Double)
    {
        samples.add(sample)
    }
}

fun <T> Sequence<T>.allEqual(): Boolean = all { it == first() }
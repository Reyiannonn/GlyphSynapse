package com.glyphsynapse.app.domain.engine

import kotlinx.coroutines.delay

/**
 * Drives a ~30fps render loop, compensating for frame overruns.
 * The speed multiplier scales elapsed time passed to animations — higher = faster.
 *
 * @param timeSource injectable for testing; defaults to wall-clock milliseconds
 */
class FrameScheduler(
    private val targetFps: Int = 30,
    private val timeSource: () -> Long = { System.currentTimeMillis() }
) {
    private val frameIntervalMs = 1000L / targetFps
    private var startWallMs = 0L
    private var accumulatedScaledMs = 0L
    private var lastWallMs = 0L

    fun reset() {
        val now = timeSource()
        startWallMs = now
        lastWallMs = now
        accumulatedScaledMs = 0L
    }

    /**
     * Call at the start of each frame. Returns the scaled elapsed time (ms) to pass to tick().
     * Suspends for the remainder of the frame budget.
     */
    suspend fun awaitNextFrame(speedMultiplier: Float): Long {
        val now = timeSource()
        val wallDelta = now - lastWallMs
        lastWallMs = now

        accumulatedScaledMs += (wallDelta * speedMultiplier).toLong()

        // Sleep for remaining frame time (guard against drift)
        val elapsed = now - startWallMs
        val expectedFrames = elapsed / frameIntervalMs
        val sleepMs = ((expectedFrames + 1) * frameIntervalMs - elapsed).coerceAtLeast(4L)
        delay(sleepMs)

        return accumulatedScaledMs
    }
}

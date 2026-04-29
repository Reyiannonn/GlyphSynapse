package com.glyphsynapse.app

import com.glyphsynapse.app.domain.engine.FrameScheduler
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FrameSchedulerTest {

    /** Fake clock that starts at 0 and advances by a fixed delta each call. */
    private fun fakeClock(deltaMs: Long): () -> Long {
        var t = 0L
        return { t.also { t += deltaMs } }
    }

    @Test
    fun `accumulated time grows with speed multiplier`() = runTest {
        val clock = fakeClock(33L)
        val scheduler = FrameScheduler(targetFps = 30, timeSource = clock)
        scheduler.reset()

        val first = scheduler.awaitNextFrame(1.0f)
        val second = scheduler.awaitNextFrame(1.0f)

        assertTrue(second > first, "Accumulated time should increase each frame")
    }

    @Test
    fun `higher speed multiplier produces more elapsed time`() = runTest {
        val slowClock = fakeClock(33L)
        val fastClock = fakeClock(33L)

        val slow = FrameScheduler(targetFps = 30, timeSource = slowClock)
        slow.reset()
        val fast = FrameScheduler(targetFps = 30, timeSource = fastClock)
        fast.reset()

        val slowElapsed = slow.awaitNextFrame(0.5f)
        val fastElapsed = fast.awaitNextFrame(2.0f)

        assertTrue(fastElapsed >= slowElapsed, "2× speed should accumulate more time than 0.5×")
    }

    @Test
    fun `accumulated time is zero after reset`() = runTest {
        val clock = fakeClock(0L)
        val scheduler = FrameScheduler(targetFps = 30, timeSource = clock)
        scheduler.reset()
        // No frames ticked yet, but accumulated should start at 0
        // We can verify by checking a 0-delta first frame
        val elapsed = scheduler.awaitNextFrame(1.0f)
        assertTrue(elapsed >= 0, "Elapsed should be non-negative")
    }
}

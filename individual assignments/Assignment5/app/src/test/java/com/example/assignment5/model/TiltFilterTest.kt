package com.example.assignment5.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TiltFilterTest {
    @Test
    fun sensorCoordinatesAreConvertedToScreenDirections() {
        val tilt = TiltFilter().update(2f, 3f, 1L, false, 0)
        assertEquals(Tilt(-2f, 3f), tilt)
    }

    @Test
    fun allDisplayRotationsUseTheCorrectScreenAxes() {
        val expected = listOf(Tilt(-2f, 3f), Tilt(3f, 2f), Tilt(2f, -3f), Tilt(-3f, -2f))
        for (rotation in 0..3) {
            val tilt = TiltFilter().update(2f, 3f, 1L, false, rotation)
            assertEquals(expected[rotation], tilt)
        }
    }

    @Test
    fun accelerometerFallbackSmoothsSuddenChanges() {
        val filter = TiltFilter()
        filter.update(0f, 0f, 1_000_000_000L, true, 0)
        val tilt = filter.update(10f, 0f, 1_020_000_000L, true, 0)
        assertTrue(tilt.x < 0f && tilt.x > -10f)
    }

    @Test
    fun resettingTheFilterDiscardsOldSensorReadings() {
        val filter = TiltFilter()
        filter.update(10f, 10f, 1_000_000_000L, true, 0)
        filter.reset()
        val tilt = filter.update(0f, 0f, 2_000_000_000L, true, 0)
        assertEquals(0f, tilt.x, 0f)
        assertEquals(0f, tilt.y, 0f)
    }
}

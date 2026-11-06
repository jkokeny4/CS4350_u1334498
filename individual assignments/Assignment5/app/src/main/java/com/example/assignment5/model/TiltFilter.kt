package com.example.assignment5.model

import kotlin.math.exp

data class Tilt(val x: Float = 0f, val y: Float = 0f)

class TiltFilter {
    private var filteredX = 0f
    private var filteredY = 0f
    private var lastTimestamp = 0L

    fun reset() {
        filteredX = 0f
        filteredY = 0f
        lastTimestamp = 0L
    }

    fun update(
        x: Float,
        y: Float,
        timestamp: Long,
        useAccelerometer: Boolean,
        rotation: Int
    ): Tilt {
        if (!x.isFinite() || !y.isFinite()) {
            return Tilt()
        }

        if (useAccelerometer && lastTimestamp != 0L) {
            // A low-pass filter reduces shaking when using the accelerometer fallback.
            val dt = ((timestamp - lastTimestamp) / 1_000_000_000f).coerceIn(0f, 0.2f)
            val alpha = exp(-dt / 0.18f)
            filteredX = alpha * filteredX + (1f - alpha) * x
            filteredY = alpha * filteredY + (1f - alpha) * y
        } else {
            filteredX = x
            filteredY = y
        }
        lastTimestamp = timestamp

        // Rotation values 0..3 match Surface.ROTATION_0 through ROTATION_270.
        val screenGravity = when (rotation) {
            1 -> Tilt(-filteredY, filteredX)
            2 -> Tilt(-filteredX, -filteredY)
            3 -> Tilt(filteredY, -filteredX)
            else -> Tilt(filteredX, filteredY)
        }

        // Rolling follows downhill gravity. Screen Y points down, sensor Y points up.
        return Tilt(-screenGravity.x, screenGravity.y)
    }
}

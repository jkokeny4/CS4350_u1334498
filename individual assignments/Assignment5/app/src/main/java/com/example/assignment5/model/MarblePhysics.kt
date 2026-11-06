package com.example.assignment5.model

import kotlin.math.abs
import kotlin.math.exp

// Position is measured in dp and velocity is measured in dp per second.
data class MarbleState(
    val x: Float = 0f,
    val y: Float = 0f,
    val velocityX: Float = 0f,
    val velocityY: Float = 0f
)

object MarblePhysics {
    const val MARBLE_SIZE = 40f
    private const val GRAVITY_SCALE = 85f
    private const val FRICTION = 1.8f

    fun step(
        marble: MarbleState,
        tilt: Tilt,
        maxX: Float,
        maxY: Float,
        seconds: Float
    ): MarbleState {
        if (!seconds.isFinite() || seconds <= 0f) {
            return marble
        }

        // Avoid a large jump if a frame takes longer than expected.
        val dt = seconds.coerceAtMost(0.05f)
        val friction = exp(-FRICTION * dt)
        val accelerationX = if (abs(tilt.x) < 0.08f) 0f else tilt.x * GRAVITY_SCALE
        val accelerationY = if (abs(tilt.y) < 0.08f) 0f else tilt.y * GRAVITY_SCALE

        var velocityX = (marble.velocityX + accelerationX * dt) * friction
        var velocityY = (marble.velocityY + accelerationY * dt) * friction

        if (accelerationX == 0f && abs(velocityX) < 0.1f) velocityX = 0f
        if (accelerationY == 0f && abs(velocityY) < 0.1f) velocityY = 0f

        val x = (marble.x + velocityX * dt).coerceIn(0f, maxX.coerceAtLeast(0f))
        val y = (marble.y + velocityY * dt).coerceIn(0f, maxY.coerceAtLeast(0f))

        // Stop the velocity pointing into a wall so reversing tilt responds quickly.
        if ((x == 0f && velocityX < 0f) || (x == maxX && velocityX > 0f)) velocityX = 0f
        if ((y == 0f && velocityY < 0f) || (y == maxY && velocityY > 0f)) velocityY = 0f

        return MarbleState(x, y, velocityX, velocityY)
    }
}

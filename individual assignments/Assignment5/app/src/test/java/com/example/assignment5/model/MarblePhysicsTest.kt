package com.example.assignment5.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MarblePhysicsTest {
    @Test
    fun tiltAcceleratesTheMarbleInBothScreenDirections() {
        val start = MarbleState(x = 100f, y = 100f)
        val result = MarblePhysics.step(start, Tilt(2f, -3f), 500f, 500f, 0.016f)

        assertTrue(result.x > start.x)
        assertTrue(result.y < start.y)
    }

    @Test
    fun frictionGraduallySlowsTheMarbleOnALevelSurface() {
        var marble = MarbleState(x = 500f, y = 500f, velocityX = 100f, velocityY = -80f)
        val firstFrame = MarblePhysics.step(marble, Tilt(), 2000f, 2000f, 0.016f)
        assertTrue(firstFrame.velocityX in 0f..99.99f)
        assertTrue(firstFrame.velocityY > -80f && firstFrame.velocityY < 0f)

        repeat(600) {
            marble = MarblePhysics.step(marble, Tilt(), 2000f, 2000f, 0.016f)
        }
        assertEquals(0f, marble.velocityX, 0f)
        assertEquals(0f, marble.velocityY, 0f)
    }

    @Test
    fun everyWallKeepsTheWholeMarbleInsideTheBoard() {
        var marble = MarbleState(x = 50f, y = 50f)
        val directions = listOf(Tilt(10f, 10f), Tilt(-10f, -10f))

        for (tilt in directions) {
            repeat(600) {
                marble = MarblePhysics.step(marble, tilt, 100f, 100f, 0.016f)
                assertTrue(marble.x in 0f..100f)
                assertTrue(marble.y in 0f..100f)
            }
            val edge = if (tilt.x > 0f) 100f else 0f
            assertEquals(edge, marble.x, 0f)
            assertEquals(edge, marble.y, 0f)
            assertEquals(0f, marble.velocityX, 0f)
            assertEquals(0f, marble.velocityY, 0f)
        }
    }

    @Test
    fun theMarbleCanMoveAwayFromAWallWhenTiltReverses() {
        val atWall = MarbleState(x = 100f, y = 50f)
        val result = MarblePhysics.step(atWall, Tilt(-2f, 0f), 100f, 100f, 0.016f)
        assertTrue(result.x < 100f)
    }

    @Test
    fun longFrameDelaysDoNotCauseLargeJumps() {
        val start = MarbleState(x = 500f, y = 500f, velocityX = 100f)
        val delayed = MarblePhysics.step(start, Tilt(2f, 0f), 2000f, 2000f, 10f)
        val capped = MarblePhysics.step(start, Tilt(2f, 0f), 2000f, 2000f, 0.05f)
        assertEquals(capped, delayed)
    }

    @Test
    fun aTinyBoardDoesNotProduceNegativeCoordinates() {
        val result = MarblePhysics.step(MarbleState(), Tilt(8f, -8f), 0f, 0f, 0.016f)
        assertEquals(0f, result.x, 0f)
        assertEquals(0f, result.y, 0f)
    }
}

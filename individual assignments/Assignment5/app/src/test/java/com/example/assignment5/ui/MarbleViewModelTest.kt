package com.example.assignment5.ui

import com.example.assignment5.model.MarbleState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MarbleViewModelTest {
    @Test
    fun resetCentersTheMarbleAndClearsItsVelocity() {
        val viewModel = MarbleViewModel()
        viewModel.setBoardSize(300f, 400f)
        viewModel.onSensorStarted(true, false)
        viewModel.updateTilt(-3f, 2f, 1L, false, 0)
        viewModel.onFrame(1_000_000_000L)
        viewModel.onFrame(1_016_000_000L)
        assertTrue(viewModel.marble.value.velocityX > 0f)

        viewModel.resetMarble()
        assertEquals(MarbleState(x = 130f, y = 180f), viewModel.marble.value)
    }

    @Test
    fun pausingStopsMotionAndResumingDoesNotJump() {
        val viewModel = MarbleViewModel()
        viewModel.setBoardSize(300f, 400f)
        viewModel.onSensorStarted(true, false)
        viewModel.updateTilt(-3f, 2f, 1L, false, 0)
        viewModel.onFrame(1_000_000_000L)
        viewModel.onFrame(1_016_000_000L)
        viewModel.onSensorStopped()

        val paused = viewModel.marble.value
        assertEquals(0f, paused.velocityX, 0f)
        assertEquals(0f, paused.velocityY, 0f)
        viewModel.onFrame(10_000_000_000L)
        assertEquals(paused, viewModel.marble.value)

        viewModel.onSensorStarted(true, false)
        viewModel.onFrame(100_000_000_000L)
        assertEquals(paused, viewModel.marble.value)
    }

    @Test
    fun resizingTheBoardClampsTheMarbleToTheNewBounds() {
        val viewModel = MarbleViewModel()
        viewModel.setBoardSize(600f, 600f)
        viewModel.setBoardSize(100f, 120f)

        assertEquals(MarbleState(x = 60f, y = 80f), viewModel.marble.value)
    }

    @Test
    fun noSensorLeavesTheMarbleStillAndShowsAMessage() {
        val viewModel = MarbleViewModel()
        viewModel.setBoardSize(300f, 400f)
        viewModel.onSensorStarted(false, false)
        viewModel.updateTilt(5f, 5f, 1L, false, 0)
        viewModel.onFrame(1_000_000_000L)
        viewModel.onFrame(1_016_000_000L)

        assertEquals(MarbleState(x = 130f, y = 180f), viewModel.marble.value)
        assertEquals("No motion sensor available", viewModel.sensorStatus.value)
    }
}

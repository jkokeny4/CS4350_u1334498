package com.example.assignment5.ui

import androidx.lifecycle.ViewModel
import com.example.assignment5.model.MarblePhysics
import com.example.assignment5.model.MarbleState
import com.example.assignment5.model.Tilt
import com.example.assignment5.model.TiltFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MarbleViewModel : ViewModel() {
    private val _marble = MutableStateFlow(MarbleState())
    val marble = _marble.asStateFlow()

    private val _sensorStatus = MutableStateFlow("Waiting for sensor")
    val sensorStatus = _sensorStatus.asStateFlow()

    private val tiltFilter = TiltFilter()
    private var tilt = Tilt()
    private var maxX = 0f
    private var maxY = 0f
    private var boardReady = false
    private var running = false
    private var lastFrame = 0L

    fun setBoardSize(width: Float, height: Float) {
        maxX = (width - MarblePhysics.MARBLE_SIZE).coerceAtLeast(0f)
        maxY = (height - MarblePhysics.MARBLE_SIZE).coerceAtLeast(0f)

        if (!boardReady) {
            resetMarble()
            boardReady = true
        } else {
            _marble.value = MarbleState(
                x = _marble.value.x.coerceIn(0f, maxX),
                y = _marble.value.y.coerceIn(0f, maxY)
            )
        }
        lastFrame = 0L
    }

    fun resetMarble() {
        _marble.value = MarbleState(x = maxX / 2f, y = maxY / 2f)
    }

    fun onSensorStarted(registered: Boolean, useAccelerometer: Boolean) {
        running = registered
        tiltFilter.reset()
        tilt = Tilt()
        lastFrame = 0L
        _sensorStatus.value = when {
            !registered -> "No motion sensor available"
            useAccelerometer -> "Accelerometer fallback active"
            else -> "Gravity sensor active"
        }
    }

    fun onSensorStopped() {
        running = false
        lastFrame = 0L
        tilt = Tilt()
        _marble.value = _marble.value.copy(velocityX = 0f, velocityY = 0f)
        _sensorStatus.value = "Sensor paused"
    }

    fun updateTilt(x: Float, y: Float, timestamp: Long, useAccelerometer: Boolean, rotation: Int) {
        if (running) {
            tilt = tiltFilter.update(x, y, timestamp, useAccelerometer, rotation)
        }
    }

    fun onFrame(frameTime: Long) {
        if (!running || !boardReady) return

        if (lastFrame != 0L) {
            val seconds = (frameTime - lastFrame) / 1_000_000_000f
            _marble.value = MarblePhysics.step(_marble.value, tilt, maxX, maxY, seconds)
        }
        lastFrame = frameTime
    }
}

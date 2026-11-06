package com.example.assignment5

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.assignment5.model.MarblePhysics
import com.example.assignment5.ui.MarbleViewModel
import kotlinx.coroutines.isActive

class MainActivity : ComponentActivity(), SensorEventListener {
    private val viewModel: MarbleViewModel by viewModels()
    private lateinit var sensorManager: SensorManager
    private var motionSensor: Sensor? = null
    private var useAccelerometer = false
    private var registered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        motionSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
            ?: sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        useAccelerometer = motionSensor?.type == Sensor.TYPE_ACCELEROMETER

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF19665B),
                    background = Color(0xFFF6F8F5)
                )
            ) {
                MarbleGame(viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registered = motionSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        } ?: false
        viewModel.onSensorStarted(registered, useAccelerometer)
    }

    override fun onPause() {
        sensorManager.unregisterListener(this)
        registered = false
        viewModel.onSensorStopped()
        super.onPause()
    }

    @Suppress("DEPRECATION")
    override fun onSensorChanged(event: SensorEvent) {
        if (registered && event.sensor.type == motionSensor?.type) {
            viewModel.updateTilt(
                x = event.values[0],
                y = event.values[1],
                timestamp = event.timestamp,
                useAccelerometer = useAccelerometer,
                rotation = windowManager.defaultDisplay.rotation
            )
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No action is needed when the sensor accuracy changes.
    }
}

@Composable
private fun MarbleGame(viewModel: MarbleViewModel) {
    val marble by viewModel.marble.collectAsStateWithLifecycle()
    val sensorStatus by viewModel.sensorStatus.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Draw at the display's frame rate; stop the loop when the activity is paused.
    LaunchedEffect(lifecycleOwner, viewModel) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            while (isActive) {
                withFrameNanos { viewModel.onFrame(it) }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Marble Game", style = MaterialTheme.typography.headlineMedium)
            Text("Hold your phone flat, then gently tilt it to roll the marble.")
            Text(
                text = sensorStatus,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFE4EEE7))
                    .border(2.dp, Color(0xFF9BB8A7), RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                BoxWithConstraints(Modifier.fillMaxSize()) {
                    LaunchedEffect(maxWidth, maxHeight) {
                        viewModel.setBoardSize(maxWidth.value, maxHeight.value)
                    }

                    Canvas(Modifier.fillMaxSize()) {
                        val lineColor = Color(0xFFC8D9CC)
                        drawLine(
                            lineColor,
                            Offset(size.width / 2f, 0f),
                            Offset(size.width / 2f, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                        drawLine(
                            lineColor,
                            Offset(0f, size.height / 2f),
                            Offset(size.width, size.height / 2f),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    Box(
                        modifier = Modifier
                            .absoluteOffset(x = marble.x.dp, y = marble.y.dp)
                            .size(MarblePhysics.MARBLE_SIZE.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(Color(0xFFBEF7F4), Color(0xFF168A90), Color(0xFF074D59)),
                                    center = Offset(18f, 18f)
                                ),
                                shape = CircleShape
                            )
                            .border(1.dp, Color(0xFF0B5660), CircleShape)
                            .semantics { contentDescription = "Marble" }
                    )
                }
            }

            Button(
                onClick = viewModel::resetMarble,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset marble")
            }
        }
    }
}

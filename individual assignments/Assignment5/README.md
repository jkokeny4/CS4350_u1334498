# Assignment 5 — Marble Game

A single-activity Compose app that rolls a marble downhill when the phone is tilted.

## How it works

- `MainActivity` reads `TYPE_GRAVITY` using `SENSOR_DELAY_GAME`, registers in `onResume()`, and unregisters in `onPause()`.
- When gravity is unavailable, it uses the accelerometer with a low-pass filter. A device with neither sensor shows an unavailable message.
- `TiltFilter` converts the sensor axes to screen directions, including landscape rotations.
- `MarbleViewModel` owns the marble state and advances the physics using frame timestamps. The animation loop runs only while the activity is resumed.
- `MarblePhysics` applies tilt acceleration, time-based friction, and bounds that account for the entire marble's diameter.
- `BoxWithConstraints` measures the play area. The marble's absolute offset is applied before its size and circular background.
- Reset centers the marble and clears its velocity. Resuming the app clears stale motion to avoid jumps.

## Run and test

Open this `Assignment5` folder in Android Studio and run it on a phone. Hold it nearly flat, then gently lower each edge. The marble should roll toward that edge, stay inside the board, and slow down when the phone returns to level.

For an emulator, open **Extended Controls → Virtual Sensors → Device Pose**. The accelerometer fallback is used if no gravity sensor is provided.

```powershell
.\gradlew.bat clean testDebugUnitTest assembleDebug
```

Unit tests cover screen directions, rotations, accelerometer smoothing, friction, walls, reversing tilt, long frame delays, reset, resizing, pause/resume, and the missing-sensor case.

## Device verification

Tested on a Moto G Play (2024). That phone does not expose a gravity sensor, so it uses the accelerometer fallback. The marble moved from the center to the lower boundary without clipping. Android's sensor service confirmed a 20,000-microsecond sampling period, removal of the listener when the app was backgrounded, and re-registration on resume. The gravity sensor remains the first choice on supported devices.

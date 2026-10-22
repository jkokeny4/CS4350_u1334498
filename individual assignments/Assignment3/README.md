# Assignment 3 — Persistent Course Manager

Assignment 3 extends the Compose Course Manager from Assignment 2 with permanent Room storage.

## Architecture

`Compose UI → CourseViewModel → CourseRepository interface → RoomCourseRepository singleton → Room database`

- `CourseDao` exposes a `Flow`, so the list updates as the Room database changes.
- `RoomCourseRepository.getInstance()` returns the one repository instance for the app.
- `CourseViewModelFactory.getInstance()` provides that repository to the ViewModel.
- The Room database is saved on device, so courses remain after closing and reopening the app.

## Build

```powershell
.\gradlew.bat clean
.\gradlew.bat assembleDebug
```

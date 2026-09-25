# Assignment 2 — Course Manager

A single-activity Android app built entirely with Jetpack Compose. It follows MVVM: `CourseViewModel` owns a `StateFlow` of immutable `Course` records, while the Compose UI observes that state and invokes ViewModel actions.

Features:

- Scrollable `LazyColumn` list showing course names only
- Detail view for department, course number, and location
- Add and delete courses
- Edit courses (extra credit)
- Input validation that requires every course field before saving

## Build

Open this `Assignment2` folder in Android Studio, make sure an Android SDK is configured, then run:

```powershell
.\gradlew.bat clean
.\gradlew.bat assembleDebug
```

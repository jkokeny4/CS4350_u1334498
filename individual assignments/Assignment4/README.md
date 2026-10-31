# Assignment 4 — Fun Facts

Android app that fetches random facts from `https://uselessfacts.jsph.pl/` with a singleton Ktor client and saves them locally with Room.

## Architecture

`Compose UI → FunFactViewModel → FunFactRepository → Room + Ktor`

- `FunFactsApplication` owns the Room database, Ktor client, and singleton repository.
- `FunFactViewModelFactory` receives the application's shared repository and constructs the ViewModel.
- `FunFactApi` is a Kotlin singleton object that creates the Ktor client.
- The Room DAO returns a `Flow`; saved facts and search results update immediately.
- Network responses use `ApiFunFact`, while `FunFact` is a Kotlin Room data class with a generated `@PrimaryKey`.
- KSP generates the Room database code for the Kotlin data class.
- A database migration preserves facts saved by the original Java entity.

## Build

```powershell
.\gradlew.bat clean
.\gradlew.bat assembleDebug
```

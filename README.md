# TMDB Movie App

## Screenshots

<img width="3300" height="2340" alt="merge_1755144870" src="https://github.com/user-attachments/assets/ac4833c3-c95e-4d94-bb29-6b5d4a070f2d" />

## Downloads

Get the Android APK from CI/CD pipeline:
[Download Android Artifact](https://github.com/paulcoding810/tmdb-kmm/actions/workflows/android-build.yml)

## Features

### KMM

- Common API requests and repository logic across platforms
- Centralized error message handling and data logging

### Movie List

- **Trending Movies**: Display current trending movies from TMDB
- **Caching**: Automatic cache invalidation every hour
- **Search Functionality**: Search movies by title with real time results

### Movie Details

- **Details**: View complete movie information including cast, crew, and ratings
- **Caching**: Movie details are cached locally for offline access
- **External Links**: Direct links to official movie pages

### Testing

- **Unit Tests**: ViewModel testing with MockK
- **UI Tests**:  Test for MovieItem components

## Development

1. **Configure API Key**
   - Open `Config.kt` in your project
   - Replace `YOUR_API_KEY` with your TMDB API key

2. **Build and Run**
   ```bash
   ./gradlew :androidApp:assembleDebug
   ```

### Running Tests

**Unit Tests:**

```bash
./gradlew :androidApp:testDebugUnitTest
```

**UI Tests:**

```bash
./gradlew :androidApp:connectedDebugAndroidTest
```

## Tech Stack

### Android

- **[Jetpack Compose](https://developer.android.com/jetpack/compose)** - Modern UI toolkit
- **[Koin](https://insert-koin.io/)** - Dependency injection framework
- **[Coil](https://coil-kt.github.io/coil/compose/)** - Image loading and caching
- **[MockK](https://mockk.io/)** - Mocking library for testing

### KMM (Kotlin Multiplatform Mobile)

- **[Coroutines](https://github.com/Kotlin/kotlinx.coroutines#multiplatform)** - Asynchronous
  programming
- **[DateTime](https://github.com/Kotlin/kotlinx-datetime)** - Date and time utilities
- **[Koin](https://insert-koin.io/)** - Cross-platform dependency injection
- **[Kotlinx Serialization](https://ktor.io/docs/kotlin-serialization.html)** - JSON serialization
- **[Ktor](https://ktor.io/)** - HTTP client for networking
- **[SQLDelight](https://github.com/cashapp/sqldelight/)** - Database and local storage
- **[Coroutines Extensions](https://cashapp.github.io/sqldelight/js_sqlite/coroutines/)** -
  Flow-based database queries


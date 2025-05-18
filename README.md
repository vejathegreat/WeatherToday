# Weather Today

A modern Android weather application built with Jetpack Compose that provides real-time weather information based on the user's current location.

## Features

- Real-time weather information
- Location-based weather updates
- Network connectivity handling
- Beautiful Material 3 UI
- Permission handling with user feedback
- Automatic refresh functionality

## Technologies & Libraries

### Android Architecture Components
- **Jetpack Compose** - Modern UI toolkit for building native Android UI
- **ViewModel** - Lifecycle-aware data holder for UI
- **Coroutines** - For asynchronous programming
- **Flow** - For reactive programming
- **Hilt** - For dependency injection

### Networking
- **Retrofit** (2.9.0) - Type-safe HTTP client
- **OkHttp** (4.12.0) - HTTP client
- **Gson** - For JSON serialization/deserialization
- **OpenWeatherMap API** - Weather data provider

### Location Services
- **Google Play Services Location** (21.0.1) - For accessing device location
- **Accompanist Permissions** (0.32.0) - For handling runtime permissions

### Testing
- **JUnit** (4.13.2) - Unit testing framework
- **MockK** (1.13.8) - Mocking library for Kotlin
- **Turbine** (1.0.0) - Testing library for Flow
- **Truth** (1.1.5) - Fluent assertions library
- **Coroutines Test** (1.7.3) - For testing coroutines
- **AndroidX Test** - For Android-specific testing

### UI Components
- **Material 3** - Material Design components
- **Coil** - Image loading library

## Project Structure

The project follows MVVM (Model-View-ViewModel) architecture pattern with clean architecture principles:

```
app/
├── data/
│   ├── api/         # API interfaces and network-related code
│   ├── di/          # Dependency injection modules
│   ├── location/    # Location-related functionality
│   ├── model/       # Data models
│   ├── repository/  # Data repositories
│   └── utils/       # Utility classes
├── domain/
│   └── usecase/     # Business logic use cases
└── presentation/
    ├── viewmodel/   # ViewModels
    └── ui/          # UI components and themes
```

## Setup

1. Clone the repository
2. Add your OpenWeatherMap API key in `local.properties`:
   ```
   WEATHER_API_KEY=your_api_key_here
   ```
3. Build and run the project

## Required Permissions

```xml
<!-- Network Permissions -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- Location Permissions -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
```

## API Reference

The application uses the OpenWeatherMap API to fetch weather data. The main endpoint used is:

```
GET /weather
```

Parameters:
- `lat` - Latitude
- `lon` - Longitude
- `appid` - API Key
- `units` - Measurement units (metric by default)

## Contributing

Feel free to submit issues and enhancement requests.

## License

This project is licensed under the MIT License - see the LICENSE file for details. 
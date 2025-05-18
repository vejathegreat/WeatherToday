# 🌤️ Weather Today

**Weather Today** is a modern Android weather app built with **Jetpack Compose**, offering real-time weather updates based on your current location. Designed with a beautiful **Material 3 UI**, it seamlessly handles location permissions, network connectivity, and weather data updates.

---

## ✨ Features

- 🔄 Real-time weather information  
- 📍 Location-based weather updates  
- 🌐 Network connectivity awareness  
- 🎨 Modern Material 3 UI  
- 🔐 Permission handling with user feedback  
- ♻️ Automatic data refresh

## Designs

![Screenshot 2025-05-16 at 22 24 29](https://github.com/user-attachments/assets/5bc5ced4-4cfa-4b88-86b3-7348b49a9e12)


---

## 🛠️ Technologies & Libraries

### 📱 Android Architecture
- **Jetpack Compose** – Modern declarative UI toolkit  
- **ViewModel** – Lifecycle-aware state management  
- **Kotlin Coroutines** – Asynchronous programming  
- **Flow** – Reactive data streams  
- **Hilt** – Dependency injection  

### 🌐 Networking
- **Retrofit** (2.9.0) – Type-safe HTTP client  
- **OkHttp** (4.12.0) – Low-level HTTP client  
- **Gson** – JSON serialization/deserialization  
- **OpenWeatherMap API** – Weather data provider  

### 📍 Location Services
- **Google Play Services Location** (21.0.1) – Location access  
- **Accompanist Permissions** (0.32.0) – Runtime permission management  

### 🧪 Testing
- **JUnit** (4.13.2) – Unit testing framework  
- **MockK** (1.13.8) – Kotlin mocking library  
- **Turbine** (1.0.0) – Flow testing  
- **Truth** (1.1.5) – Fluent assertions  
- **Coroutines Test** (1.7.3) – Coroutine testing tools  
- **AndroidX Test** – Android testing support  

### 🖼️ UI & Media
- **Material 3** – Modern design system  
- **Coil** – Fast image loading for Compose  

---

## 📁 Project Structure

Follows the **MVVM** architecture with clean architecture principles:

```
app/
├── data/
│   ├── api/         # Retrofit interfaces and network configuration
│   ├── di/          # Dependency injection setup
│   ├── location/    # Location services and permissions
│   ├── model/       # Data models (DTOs)
│   ├── repository/  # Data source implementations
│   └── utils/       # Utility classes and helpers
├── domain/
│   └── usecase/     # Application business logic
└── presentation/
    ├── viewmodel/   # ViewModels for UI state management
    └── ui/          # UI screens, components, themes
```

---

## ⚙️ Setup Instructions

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/weather-today.git
   ```

2. **Add your OpenWeatherMap API key** in `local.properties`:
   ```properties
   WEATHER_API_KEY=your_api_key_here
   ```

3. **Build and run** the project in Android Studio.

---

## ⚠️ Build Note

> 🧩 **Important:**  
> For this project to build successfully, you **must** add your OpenWeatherMap API key to the `local.properties` file at the root of your project:

```properties
WEATHER_API_KEY=your_api_key_here
```

---

## 🔐 Required Permissions

The following permissions are used in `AndroidManifest.xml`:

```xml
<!-- Internet access -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- Location access -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
```

---

## 🌍 API Reference

Weather data is fetched from the [OpenWeatherMap API](https://openweathermap.org/api):

**Endpoint:**
```
GET /weather
```

**Parameters:**
- `lat` – Latitude  
- `lon` – Longitude  
- `appid` – Your API key  
- `units` – Measurement units (`metric`, `imperial`, etc.)

---

## 🤝 Contributing

Contributions are welcome!  
Feel free to [open an issue](https://github.com/your-username/weather-today/issues) or submit a pull request for new features or bug fixes.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

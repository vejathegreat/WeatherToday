package com.velaphi.weathertoday

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.velaphi.weathertoday.data.model.WeatherResponse
import com.velaphi.weathertoday.data.location.LocationManager
import com.velaphi.weathertoday.presentation.viewmodel.WeatherState
import com.velaphi.weathertoday.presentation.viewmodel.WeatherViewModel
import com.velaphi.weathertoday.ui.theme.WeatherTodayTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTodayTheme {
                MainScreen(locationManager = locationManager)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    locationManager: LocationManager,
    weatherViewModel: WeatherViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val hasNetwork by weatherViewModel.hasNetwork.collectAsState(initial = true)
    val weatherState by weatherViewModel.weatherState.collectAsState()
    val hasPermission by remember {
        derivedStateOf { locationManager.hasLocationPermission(context) }
    }

    var shouldRequestPermission by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var errorType by remember { mutableStateOf("") }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.all { it }
        if (granted) {
            fetchWeather(weatherViewModel, locationManager, context, coroutineScope, {
                errorType = it
            }, {
                isLoading = it
            })
        } else {
            isLoading = false
            errorType = "permission"
        }
    }

    LaunchedEffect(Unit) {
        weatherViewModel.checkNetworkConnection()
        if (!weatherViewModel.hasNetwork.value) {
            isLoading = false
            errorType = "network"
        } else if (!hasPermission) {
            shouldRequestPermission = true
        } else {
            fetchWeather(weatherViewModel, locationManager, context, coroutineScope, {
                errorType = it
            }, {
                isLoading = it
            })
        }
    }

    if (shouldRequestPermission) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
        shouldRequestPermission = false
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Weather Today") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator()
                }

                errorType == "network" -> {
                    ErrorWithIconAndRefresh(
                        iconRes = R.drawable.no_wifi,
                        message = "No network connection",
                        onRefresh = {
                            isLoading = true
                            weatherViewModel.checkNetworkConnection()
                            if (!weatherViewModel.hasNetwork.value) {
                                isLoading = false
                                errorType = "network"
                            } else if (!hasPermission) {
                                isLoading = false
                                errorType = "permission"
                            } else {
                                fetchWeather(
                                    weatherViewModel,
                                    locationManager,
                                    context,
                                    coroutineScope,
                                    onError = { errorType = it },
                                    onLoading = { isLoading = it }
                                )
                            }
                        }
                    )
                }

                errorType == "permission" -> {
                    ErrorWithIconAndRefresh(
                        iconRes = R.drawable.location,
                        message = "Location permission is required",
                        onRefresh = {
                            coroutineScope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Location permission is required",
                                    actionLabel = "Request"
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    shouldRequestPermission = true
                                }
                            }
                        }
                    )
                }

                weatherState is WeatherState.Success -> {
                    val weather = (weatherState as WeatherState.Success).weather
                    WeatherContent(weatherResponse = weather) {
                        fetchWeather(
                            weatherViewModel,
                            locationManager,
                            context,
                            coroutineScope,
                            onError = { errorType = it },
                            onLoading = { isLoading = it }
                        )
                    }
                }

                else -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

fun fetchWeather(
    weatherViewModel: WeatherViewModel,
    locationManager: LocationManager,
    context: android.content.Context,
    coroutineScope: CoroutineScope,
    onError: (String) -> Unit,
    onLoading: (Boolean) -> Unit
) {
    onLoading(true)
    coroutineScope.launch {
        val location = locationManager.getCurrentLocation(context)
        if (location != null) {
            weatherViewModel.fetchWeather(location.latitude, location.longitude)
            onError("")
        } else {
            onError("permission")
        }
        onLoading(false)
    }
}

@Composable
fun ErrorWithIconAndRefresh(iconRes: Int, message: String, onRefresh: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )
        Text(text = message)
        Button(onClick = onRefresh) {
            Text("Refresh")
        }
    }
}

@Composable
fun WeatherContent(weatherResponse: WeatherResponse, onRefreshClicked: () -> Unit) {
    val weather = weatherResponse.weather.firstOrNull()
    val iconUrl = weather?.icon?.let { "https://openweathermap.org/img/wn/${it}@2x.png" }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = weatherResponse.name, style = MaterialTheme.typography.headlineLarge)
        iconUrl?.let {
            AsyncImage(
                model = it,
                contentDescription = weather?.description ?: "Weather Icon",
                modifier = Modifier.size(100.dp)
            )
        }
        Text(text = "${weatherResponse.main.temperature.toInt()}°C", style = MaterialTheme.typography.displayMedium)
        Text(text = weather?.description?.replaceFirstChar { it.uppercase() } ?: "", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRefreshClicked) {
            Text("Refresh")
        }
    }
}

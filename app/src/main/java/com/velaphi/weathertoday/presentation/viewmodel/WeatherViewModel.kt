package com.velaphi.weathertoday.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velaphi.weathertoday.data.location.LocationManager
import com.velaphi.weathertoday.data.model.WeatherResponse
import com.velaphi.weathertoday.data.utils.NetworkUtils
import com.velaphi.weathertoday.domain.usecase.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Coordinates(val latitude: Double, val longitude: Double)

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val weather: WeatherResponse) : WeatherState()
    data class Error(val message: String) : WeatherState()
}

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    @Inject
    lateinit var locationManager: LocationManager

    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    private val _hasNetwork = MutableStateFlow(true)
    val hasNetwork: StateFlow<Boolean> = _hasNetwork.asStateFlow()

    private val _hasLocationPermission = MutableStateFlow(false)
    val hasLocationPermission: StateFlow<Boolean> = _hasLocationPermission.asStateFlow()

    private val _coordinates = MutableStateFlow<Coordinates?>(null)
    val coordinates: StateFlow<Coordinates?> = _coordinates.asStateFlow()

    fun setLoading() {
        _weatherState.value = WeatherState.Loading
    }

    fun setError(message: String) {
        _weatherState.value = WeatherState.Error(message)
    }

    fun checkLocationPermissions(context: android.content.Context) {
        _hasLocationPermission.value = locationManager.hasLocationPermission(context)
    }

    fun checkNetworkConnection() {
        viewModelScope.launch {
            _hasNetwork.value = networkUtils.hasNetworkAccess()
        }
    }

    fun updateLocationAndFetchWeather(context: android.content.Context) {
        viewModelScope.launch {
            try {
                val location = locationManager.getCurrentLocation(context)
                location?.let {
                    val lat = it.latitude
                    val lon = it.longitude
                    _coordinates.value = Coordinates(lat, lon)
                    fetchWeather(lat, lon)
                } ?: setError("Unable to get current location")
            } catch (e: SecurityException) {
                setError("Location permission not granted")
            } catch (e: Exception) {
                setError(e.message ?: "Error getting location")
            }
        }
    }

    fun fetchWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            setLoading()
            try {
                val weather = getWeatherUseCase(latitude, longitude)
                _weatherState.value = WeatherState.Success(weather)
            } catch (e: Exception) {
                setError(e.message ?: "Unknown error occurred")
            }
        }
    }
}
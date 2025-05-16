package com.velaphi.weathertoday.data.repository

import com.velaphi.weathertoday.BuildConfig
import com.velaphi.weathertoday.data.api.WeatherApi
import com.velaphi.weathertoday.data.model.WeatherResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi
) {
    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double
    ): WeatherResponse {
        return weatherApi.getCurrentWeather(latitude, longitude, BuildConfig.WEATHER_API_KEY)
    }
}

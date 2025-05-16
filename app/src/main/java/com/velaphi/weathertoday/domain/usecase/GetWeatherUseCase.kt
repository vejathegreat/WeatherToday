package com.velaphi.weathertoday.domain.usecase

import com.velaphi.weathertoday.data.model.WeatherResponse
import com.velaphi.weathertoday.data.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double
    ): WeatherResponse {
        return repository.getCurrentWeather(latitude, longitude)
    }
}

package com.velaphi.weathertoday.presentation.viewmodel

import com.velaphi.weathertoday.data.model.Coordinates
import com.velaphi.weathertoday.data.model.Main
import com.velaphi.weathertoday.data.model.Weather
import com.velaphi.weathertoday.data.model.WeatherResponse
import com.velaphi.weathertoday.data.utils.NetworkUtils
import com.velaphi.weathertoday.domain.usecase.GetWeatherUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var getWeatherUseCase: GetWeatherUseCase
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var networkUtils: NetworkUtils

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getWeatherUseCase = mockk()
        networkUtils = mockk()
        viewModel = WeatherViewModel(getWeatherUseCase, networkUtils)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Loading`() = runTest {
        val initialState = viewModel.weatherState.first()
        assertTrue(initialState is WeatherState.Loading)
    }

    @Test
    fun `fetchWeather success should update state with weather data`() = runTest {
        val mockWeather = createMockWeatherResponse()
        coEvery { getWeatherUseCase(any(), any()) } returns mockWeather

        viewModel.fetchWeather(0.0, 0.0)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.weatherState.first()
        assertTrue(state is WeatherState.Success)
        assertEquals(mockWeather, (state as WeatherState.Success).weather)
    }

    @Test
    fun `fetchWeather error should update state with error`() = runTest {
        val errorMessage = "Network error"
        coEvery { getWeatherUseCase(any(), any()) } throws Exception(errorMessage)

        viewModel.fetchWeather(0.0, 0.0)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.weatherState.first()
        assertTrue(state is WeatherState.Error)
        assertEquals(errorMessage, (state as WeatherState.Error).message)
    }

    @Test
    fun `setLoading should update state to Loading`() = runTest {
        viewModel.setLoading()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.weatherState.first()
        assertTrue(state is WeatherState.Loading)
    }

    @Test
    fun `setError should update state with error message`() = runTest {
        val errorMessage = "Test error"

        viewModel.setError(errorMessage)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.weatherState.first()
        assertTrue(state is WeatherState.Error)
        assertEquals(errorMessage, (state as WeatherState.Error).message)
    }

    private fun createMockWeatherResponse() = WeatherResponse(
        coordinates = Coordinates(0.0, 0.0),
        weather = listOf(Weather(1, "Clear", "clear sky", "01d")),
        base = "stations",
        main = Main(
            temperature = 20.0,
            feelsLike = 19.0,
            tempMin = 18.0,
            tempMax = 22.0,
            pressure = 1013,
            humidity = 60,
            seaLevel = null,
            groundLevel = null
        ),
        visibility = 10000,
        wind = mockk(relaxed = true),
        clouds = mockk(relaxed = true),
        dt = 1234567890L,
        sys = mockk(relaxed = true),
        timezone = 0,
        id = 1,
        name = "Test City",
        cod = 200
    )
}

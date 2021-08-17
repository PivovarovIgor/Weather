package ru.brauer.weather.domain

import ru.brauer.weather.domain.data.Weather
import ru.brauer.weather.domain.repository.ResponseErrors

sealed class AppState {

    data class Success(val weather: Weather) : AppState()

    class Error(val error: Throwable) : AppState()

    class ResponseWithError(val responseError: ResponseErrors) : AppState()

    object Loading : AppState()
}

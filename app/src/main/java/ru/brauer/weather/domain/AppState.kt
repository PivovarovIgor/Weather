package ru.brauer.weather.domain

import ru.brauer.weather.domain.data.Weather

sealed class AppState {
    data class Success(val weathers: List<Weather>) : AppState()
    class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}

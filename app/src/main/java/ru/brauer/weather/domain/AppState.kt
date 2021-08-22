package ru.brauer.weather.domain

import ru.brauer.weather.domain.data.Weather
import ru.brauer.weather.domain.repository.ResponseErrors

sealed class AppState {

    data class Success(
        val weathers: List<Weather>,
        val indexToAdd: Int,
        val operation: DataUpdateOperations
    ) : AppState() {
        constructor(weather: Weather, indexToAdd: Int, operation: DataUpdateOperations) : this(
            listOf(weather), indexToAdd, operation
        )

        constructor(weathers: List<Weather>) : this(weathers, 0, DataUpdateOperations.ADD)

        val weather: Weather
            get() {
                require(weathers.size == 1) { "Data not available or they are ambiguous." }
                return weathers.first()
            }
    }

    class Error(val error: Throwable) : AppState()

    class ResponseWithError(val responseError: ResponseErrors) : AppState()

    object Loading : AppState()
}

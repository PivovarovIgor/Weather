package ru.brauer.weather.domain.repository.dto

data class WeatherDTO(
    val fact: FactDTO?,
    val forecasts: List<ForecastDTO>?
)

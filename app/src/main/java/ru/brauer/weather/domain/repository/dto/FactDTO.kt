package ru.brauer.weather.domain.repository.dto

data class FactDTO(
    val temp: Int?,
    val feels_like: Int?,
    val wind_speed: Double?,
    val pressure_mm: Int?
)

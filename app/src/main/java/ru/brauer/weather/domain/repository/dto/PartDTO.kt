package ru.brauer.weather.domain.repository.dto

data class PartDTO(
    val temp_avg: Int?,
    val feels_like: Int?,
    val wind_speed: Double?,
    val pressure_mm: Int?
)

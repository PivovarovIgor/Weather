package ru.brauer.weather.domain.repository.dto

data class HoursDTO(
    val hour_ts: Long?,
    val temp: Int?,
    val feels_like: Int?,
    val wind_speed: Double?,
    val pressure_mm: Int?
)

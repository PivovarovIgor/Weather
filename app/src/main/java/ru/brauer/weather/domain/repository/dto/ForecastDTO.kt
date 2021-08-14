package ru.brauer.weather.domain.repository.dto

data class ForecastDTO(
    val date_ts: Long?,
    val hours: List<HoursDTO>?,
    val parts: PartsDTO?
)

package ru.brauer.weather.domain.repository.dto

data class PartsDTO(
    val night: PartDTO?,
    val morning: PartDTO?,
    val day: PartDTO?,
    val evening: PartDTO?
)

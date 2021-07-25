package ru.brauer.weather.domain.repository

import ru.brauer.weather.domain.Weather

object YandexWeatherRepository : IWeatherRepository {
    private const val API_KEY = "8c3fb8d9-af51-4a40-b4dc-dc5b972734e0"

    override val weathers: List<Weather>
        get() = TODO("Not yet implemented")
}
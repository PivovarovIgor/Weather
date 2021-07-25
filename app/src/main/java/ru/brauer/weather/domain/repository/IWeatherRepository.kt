package ru.brauer.weather.domain.repository

import ru.brauer.weather.domain.Weather

interface IWeatherRepository {
    val weathers: List<Weather>
}
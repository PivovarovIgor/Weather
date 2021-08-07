package ru.brauer.weather.domain.repository.weather

import ru.brauer.weather.domain.data.Weather

interface IWeatherRepository {
    fun getWeathers(): List<Weather>
}
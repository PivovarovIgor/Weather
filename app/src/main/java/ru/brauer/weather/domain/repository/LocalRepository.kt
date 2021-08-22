package ru.brauer.weather.domain.repository

import ru.brauer.weather.domain.data.Weather

interface IHistoryLocalRepository {
    fun getAllHistory(): List<Weather>
    fun saveEntity(weather: Weather)
}
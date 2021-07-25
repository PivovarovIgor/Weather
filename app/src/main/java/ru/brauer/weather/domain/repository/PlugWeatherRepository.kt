package ru.brauer.weather.domain.repository

import ru.brauer.weather.domain.Weather

object PlugWeatherRepository : IWeatherRepository {
    override val weathers: List<Weather>
        get() = listOf(
            Weather("Москва", 26),
            Weather("Санкт-Петербург", 22),
            Weather("Самара", 30),
            Weather("Владивосток", 21),
            Weather("Калининград", 27),
        )
}
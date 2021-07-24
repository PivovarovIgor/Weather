package ru.brauer.weather.domain.repository

import ru.brauer.weather.domain.Weather

object PlugRepository : IRepository {
    override val data: List<Weather>
        get() = listOf(
            Weather("Москва", 26),
            Weather("Санкт-Петербург", 22),
            Weather("Самара", 30),
            Weather("Владивосток", 21),
            Weather("Калининград", 27),
        )
}
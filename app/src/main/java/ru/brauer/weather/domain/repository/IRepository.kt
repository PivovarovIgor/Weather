package ru.brauer.weather.domain.repository

import ru.brauer.weather.domain.Weather

interface IRepository {
    val data: List<Weather>
}
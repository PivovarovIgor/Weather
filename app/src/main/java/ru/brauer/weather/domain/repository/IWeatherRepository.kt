package ru.brauer.weather.domain.repository

import ru.brauer.weather.domain.data.Geolocation
import ru.brauer.weather.domain.data.Weather

interface IWeatherRepository {
    fun getWeathers(geolocations: List<Geolocation>): List<Weather>
}
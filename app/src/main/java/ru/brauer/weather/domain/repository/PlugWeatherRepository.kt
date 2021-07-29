package ru.brauer.weather.domain.repository

import ru.brauer.weather.domain.data.Geolocation
import ru.brauer.weather.domain.data.Weather
import ru.brauer.weather.domain.data.getDefaultCity
import java.lang.RuntimeException

object PlugWeatherRepository : IWeatherRepository {

    override fun getWeathers(geolocations: List<Geolocation>): List<Weather> {
        (1..3).shuffled()
            .first()
            .let {
                if (it == 3) {
                    throw RuntimeException("Fatal error")
                }
            }
        return listOf(Weather(getDefaultCity(), 18, 19, 1, 745))
    }
}
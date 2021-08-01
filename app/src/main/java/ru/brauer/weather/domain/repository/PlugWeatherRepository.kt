package ru.brauer.weather.domain.repository

import ru.brauer.weather.domain.data.*

object PlugWeatherRepository : IWeatherRepository {

    override fun getWeathers(geolocations: List<Geolocation>): List<Weather> {
        (1..3).shuffled()
            .first()
            .let {
                if (it == 3) {
                    throw RuntimeException("Fatal error")
                }
            }
        return listOf(
            Weather(
                getDefaultCity(), 18, 19, 1, 745
            ),
            newRandomWeather("Санкт-Петербург", 59.939099, 30.315877),
            newRandomWeather("Калининград", 54.710162, 20.510137),
            newRandomWeather("Краснодар", 45.035470, 38.975313),
            newRandomWeather("Архангельск", 64.539911, 40.515762),
            newRandomWeather("Новосибирск", 55.030204, 82.920430),
            newRandomWeather("Хабаровск", 48.480229, 135.071917),
            newRandomWeather("Владивосток", 43.115542, 131.885494),
        )
    }
}
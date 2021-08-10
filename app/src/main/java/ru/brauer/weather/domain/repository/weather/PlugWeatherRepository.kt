package ru.brauer.weather.domain.repository.weather

import ru.brauer.weather.domain.data.*
import ru.brauer.weather.domain.repository.cities.CityRepository

object PlugWeatherRepository : IWeatherRepository {

    override fun getWeathers(): List<Weather> {
        (1..3).shuffled()
            .first()
            .let {
                if (it == 3) {
                    throw RuntimeException("Fatal error")
                }
            }
        return CityRepository.cities.map(::newRandomWeather)
    }
}
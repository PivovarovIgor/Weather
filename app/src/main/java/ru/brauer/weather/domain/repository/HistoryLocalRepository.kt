package ru.brauer.weather.domain.repository

import ru.brauer.weather.domain.data.City
import ru.brauer.weather.domain.data.Weather
import ru.brauer.weather.domain.data.WeatherDetails
import ru.brauer.weather.domain.repository.room.HistoryDao
import ru.brauer.weather.domain.repository.room.HistoryEntity

class HistoryLocalRepository(private val localDataSource: HistoryDao) : IHistoryLocalRepository {
    
    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToEntity(weather))
    }
}

private fun convertHistoryEntityToWeather(entities: List<HistoryEntity>): List<Weather> {
    return entities.map {
        Weather(City(it.city), WeatherDetails(it.temperature), it.date)
    }
}

private fun convertWeatherToEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(0, weather.city.name, weather.date, weather.fact.temperatureInt, "")
}
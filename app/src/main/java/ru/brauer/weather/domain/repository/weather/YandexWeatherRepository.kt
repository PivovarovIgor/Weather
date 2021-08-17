package ru.brauer.weather.domain.repository.weather

import retrofit2.Callback
import ru.brauer.weather.domain.data.*
import ru.brauer.weather.domain.repository.remotedatasource.RemoteDataSource
import ru.brauer.weather.domain.repository.dto.PartDTO
import ru.brauer.weather.domain.repository.dto.PartsDTO
import ru.brauer.weather.domain.repository.dto.WeatherDTO

object YandexWeatherRepository : IWeatherRepository {
    override fun getWeather(city: City, callback: Callback<WeatherDTO>) {
        RemoteDataSource.getWeatherDetails(city.geolocation.lat, city.geolocation.lon, callback)
    }
}

private const val ONE_SECOND_AS_MILLISECONDS = 1000
private const val MORNING_TIME = 21600000L
private const val DAY_TIME = 43200000L
private const val EVENING_TIME = 64800000L

fun WeatherDTO.getWeatherDataFromRawData(city: City): Weather? {

    val weatherDetails = fact?.let {
        WeatherDetails(
            _temperature = it.temp ?: 0,
            _feelsLike = it.feels_like ?: 0,
            windSpeed = it.wind_speed?.toInt() ?: 0,
            pressure = it.pressure_mm ?: 0
        )
    } ?: return null

    val forecastsData = forecasts?.let { it ->
        it.mapNotNull { forecastItemDTO ->
            forecastItemDTO.date_ts?.let { dateDTO ->

                val date = dateDTO * ONE_SECOND_AS_MILLISECONDS

                var forecastTime = forecastItemDTO.hours?.map { hoursDTO ->
                    ForecastTime(
                        time = (hoursDTO.hour_ts ?: 0) * ONE_SECOND_AS_MILLISECONDS,
                        weatherDetails = WeatherDetails(
                            _temperature = hoursDTO.temp ?: 0,
                            _feelsLike = hoursDTO.feels_like ?: 0,
                            windSpeed = hoursDTO.wind_speed?.toInt() ?: 0,
                            pressure = hoursDTO.pressure_mm ?: 0
                        )
                    )
                } ?: listOf()

                if (forecastTime.isEmpty()) {
                    forecastTime = forecastItemDTO.parts?.extractData(date) ?: listOf()
                }

                ForecastDate(date, forecastTime)
            }
        }
    } ?: listOf()

    return Weather(city, weatherDetails, forecastsData)
}

private fun PartsDTO.extractData(date: Long): List<ForecastTime> {

    val forecasts = mutableListOf<ForecastTime>()

    night?.extractData(date)?.let { forecasts += it }
    morning?.extractData(date + MORNING_TIME)?.let { forecasts += it }
    day?.extractData(date + DAY_TIME)?.let { forecasts += it }
    evening?.extractData(date + EVENING_TIME)?.let { forecasts += it }

    return forecasts
}

private fun PartDTO.extractData(time: Long) = ForecastTime(
    time,
    WeatherDetails(
        _temperature = temp_avg ?: 0,
        _feelsLike = feels_like ?: 0,
        windSpeed = wind_speed?.toInt() ?: 0,
        pressure = pressure_mm ?: 0
    )
)
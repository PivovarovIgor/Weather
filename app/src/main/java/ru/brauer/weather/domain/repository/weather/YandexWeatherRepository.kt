package ru.brauer.weather.domain.repository.weather

import com.google.gson.Gson
import ru.brauer.weather.domain.data.*
import ru.brauer.weather.domain.repository.cities.CityRepository
import ru.brauer.weather.domain.repository.dto.PartDTO
import ru.brauer.weather.domain.repository.dto.PartsDTO
import ru.brauer.weather.domain.repository.dto.WeatherDTO
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

private const val API_KEY = "8c3fb8d9-af51-4a40-b4dc-dc5b972734e0"
private const val REQUEST_PROPERTY_KEY_API = "X-Yandex-API-Key"
private const val YANDEX_WEATHER_URL = "https://api.weather.yandex.ru/v2/forecast"

object YandexWeatherRepository : IWeatherRepository {
    override fun getWeathers(): List<Weather> = CityRepository.cities.mapNotNull(::loadWeather)

    private fun loadWeather(city: City): Weather? {

        var urlConnection: HttpsURLConnection? = null
        val weatherRawData: WeatherDTO

        val uri =
            URL(YANDEX_WEATHER_URL + "?lat=${city.geolocation.lat}&lon=${city.geolocation.lon}")
        try {
            urlConnection = (uri.openConnection() as HttpsURLConnection)
                .apply {
                    requestMethod = "GET"
                    addRequestProperty(REQUEST_PROPERTY_KEY_API, API_KEY)
                    readTimeout = 10000
                }
            weatherRawData = InputStreamReader(urlConnection.inputStream)
                .let { reader ->
                    Gson().fromJson(reader, WeatherDTO::class.java)
                }
        } catch (e: Exception) {
            urlConnection?.disconnect()
            throw e
        }
        urlConnection.disconnect()

        return weatherRawData?.getWeatherDataFromRawData(city)
    }
}

private const val ONE_SECOND_AS_MILLISECONDS = 1000
private const val MORNING_TIME = 21600000L
private const val DAY_TIME = 43200000L
private const val EVENING_TIME = 64800000L

private fun WeatherDTO.getWeatherDataFromRawData(city: City): Weather? {

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
package ru.brauer.weather.domain.repository.weather

import com.google.gson.Gson
import ru.brauer.weather.domain.data.*
import ru.brauer.weather.domain.repository.cities.CityRepository
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
        val weatherRawData: Map<String, *>?

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
                    Gson().fromJson<Map<String, *>>(reader, Map::class.java)
                }
        } catch (e: Exception) {
            urlConnection?.disconnect()
            throw e
        }
        urlConnection.disconnect()

        return weatherRawData?.getWeatherDataFromRawData(city)
    }
}

private fun Map<String, *>.getWeatherDataFromRawData(city: City): Weather? {
    val fact = extractWeatherFact(this) ?: return null
    val forecast = extractWeatherForecast(this)
    return Weather(
        city,
        fact,
        forecast
    )
}

private fun extractWeatherFact(weatherRawData: Map<String, *>): WeatherDetails? {
    val fact = weatherRawData["fact"] as? Map<*, *> ?: return null
    val temp = fact["temp"] as? Double ?: return null
    val feelsLike = fact["feels_like"] as? Double ?: return null
    val windSpeed = fact["wind_speed"] as? Double ?: return null
    val pressure = fact["pressure_mm"] as? Double ?: return null
    return WeatherDetails(temp.toInt(), feelsLike.toInt(), windSpeed.toInt(), pressure.toInt())
}

private const val ONE_SECOND_AS_MILLISECONDS = 1000

private fun extractWeatherForecast(weatherRawData: Map<*, *>): List<ForecastDate> {
    val forecast = weatherRawData["forecasts"] as? List<*> ?: return listOf()
    return forecast
        .mapNotNull { it as? Map<*, *> }
        .mapNotNull { forecastItem ->
            (forecastItem["date_ts"] as? Double)?.toLong()
                ?.let { it * ONE_SECOND_AS_MILLISECONDS }
                ?.let { date ->
                    ForecastDate(date, extractTimesForecast(forecastItem, date))
                }
        }
}

fun extractTimesForecast(forecastItem: Map<*, *>, date: Long): List<ForecastTime> {
    val hours = forecastItem["hours"] as? List<*> ?: listOf<List<*>>()
    if (hours.isEmpty()) {
        return extractPartsForecast(forecastItem, date)
    }
    return hours
        .mapNotNull { it as? Map<*, *> }
        .mapNotNull { hoursItem ->
            val hour = (hoursItem["hour_ts"] as? Double)
                ?.let { it.toLong() * ONE_SECOND_AS_MILLISECONDS }
            val temp = (hoursItem["temp"] as? Double)?.toInt() ?: 0
            val fellsLike = (hoursItem["feels_like"] as? Double)?.toInt() ?: 0
            val windSpeed = (hoursItem["wind_speed"] as? Double)?.toInt() ?: 0
            val pressure = (hoursItem["pressure_mm"] as? Double)?.toInt() ?: 0
            if (hour != null) {
                val weatherDetails = WeatherDetails(temp, fellsLike, windSpeed, pressure)
                ForecastTime(hour, weatherDetails)
            } else {
                null
            }
        }
}

private const val MORNING_TIME = 21600000L
private const val DAY_TIME = 43200000L
private const val EVENING_TIME = 64800000L

fun extractPartsForecast(forecastItem: Map<*, *>, date: Long): List<ForecastTime> {
    val parts = forecastItem["parts"] as? Map<*, *> ?: return listOf()
    val result = mutableListOf<ForecastTime?>()
    result += extractPart(parts, "night", date)
    result += extractPart(parts, "morning", date + MORNING_TIME)
    result += extractPart(parts, "day", date + DAY_TIME)
    result += extractPart(parts, "evening", date + EVENING_TIME)
    return result.filterNotNull()
}

fun extractPart(parts: Map<*, *>, partOfDayName: String, time: Long): ForecastTime? {
    val part = parts[partOfDayName] as? Map<*, *> ?: return null
    return WeatherDetails(
        _temperature = (part["temp_avg"] as? Double)?.toInt() ?: 0,
        _feelsLike = (part["feels_like"] as? Double)?.toInt() ?: 0,
        windSpeed = (part["wind_speed"] as? Double)?.toInt() ?: 0,
        pressure = (part["pressure_mm"] as? Double)?.toInt() ?: 0
    ).let { weatherDetails -> ForecastTime(time, weatherDetails) }
}
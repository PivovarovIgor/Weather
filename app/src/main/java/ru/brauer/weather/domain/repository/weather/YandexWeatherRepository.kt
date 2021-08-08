package ru.brauer.weather.domain.repository.weather

import com.google.gson.Gson
import ru.brauer.weather.domain.data.City
import ru.brauer.weather.domain.data.ForecastDate
import ru.brauer.weather.domain.data.Weather
import ru.brauer.weather.domain.data.WeatherDetails
import ru.brauer.weather.domain.repository.cities.CityRepository
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

private const val API_KEY = "8c3fb8d9-af51-4a40-b4dc-dc5b972734e0"

object YandexWeatherRepository : IWeatherRepository {
    override fun getWeathers(): List<Weather> = CityRepository.cities.mapNotNull(::loadWeather)

    private fun loadWeather(city: City): Weather? {

        var urlConnection: HttpsURLConnection? = null
        var weatherRawData: Map<String, *>? = null

        val uri =
            URL("https://api.weather.yandex.ru/v2/forecast?lat=${city.geolocation.lat}&lon=${city.geolocation.lon}")
        try {
            urlConnection = (uri.openConnection() as HttpsURLConnection)
                .apply {
                    requestMethod = "GET"
                    addRequestProperty("X-Yandex-API-Key", API_KEY)
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
    val fact = extractWeatherFact() ?: return null
    val forecast = extractWeatherForecast()
    return Weather(
        city,
        fact,
        forecast
    )
}

private fun Map<String, *>.extractWeatherFact(): WeatherDetails? {
    val fact = this["fact"] as? Map<*, *> ?: return null
    val temp = fact["temp"] as? Double ?: return null
    val feelsLike = fact["feels_like"] as? Double ?: return null
    val windSpeed = fact["wind_speed"] as? Double ?: return null
    val pressure = fact["pressure_mm"] as? Double ?: return null
    return WeatherDetails(temp.toInt(), feelsLike.toInt(), windSpeed.toInt(), pressure.toInt())
}

private fun Map<String, *>.extractWeatherForecast(): List<ForecastDate> {
    return listOf()
}
package ru.brauer.weather.domain.repository.remotedatasource

import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.brauer.weather.domain.repository.dto.WeatherDTO

private const val API_KEY = "8c3fb8d9-af51-4a40-b4dc-dc5b972734e0"

object RemoteDataSource {

    private val weatherAPI = Retrofit.Builder()
        .baseUrl("https://api.weather.yandex.ru/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build()
        .create(WeatherAPI::class.java)

    fun getWeatherDetails(lat: Double, lon: Double, callback: Callback<WeatherDTO>) {
        weatherAPI.getWeather(API_KEY, lat, lon).enqueue(callback)
    }
}
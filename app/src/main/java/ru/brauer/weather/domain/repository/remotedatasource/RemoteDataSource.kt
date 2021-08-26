package ru.brauer.weather.domain.repository.remotedatasource

import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.brauer.weather.domain.repository.dto.WeatherDTO

private const val API_KEY = "c0f0b888-09ed-4a51-94cf-65da85454071"

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
package ru.brauer.weather.domain.repository.remotedatasource

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.brauer.weather.domain.repository.dto.WeatherDTO

interface WeatherAPI {

    @GET("v2/forecast")
    fun getWeather(
        @Header("X-Yandex-API-Key") token: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<WeatherDTO>
}
package ru.brauer.weather.domain.repository.weather

import retrofit2.Callback
import ru.brauer.weather.domain.data.City
import ru.brauer.weather.domain.repository.dto.WeatherDTO


interface IWeatherRepository {
    fun getWeather(city: City, callback: Callback<WeatherDTO>)
}
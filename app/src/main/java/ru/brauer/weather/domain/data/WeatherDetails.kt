package ru.brauer.weather.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherDetails(
    private val _temperature: Int,
    private val _feelsLike: Int,
    val windSpeed: Int,
    val pressure: Int
) : Parcelable {
    val temperature get() = "${if (_temperature > 0) "+" else ""}$_temperature\u2103"
    val feelsLike get() = "${if (_temperature > 0) "+" else ""}$_temperature\u2103"
}

fun newRandomWeatherDetails(): WeatherDetails {
    val temp = (-10..30).shuffled().first()
    return WeatherDetails(
        temp,
        temp + (-5..5).shuffled().first(),
        (0..15).shuffled().first(),
        (741..748).shuffled().first()
    )
}
package ru.brauer.weather.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val city: City,
    private val _temperature: Int,
    val feelsLike: Int,
    val windSpeed: Int,
    val pressure: Int
) : Parcelable {
    val temperature get() = "${if (_temperature > 0 ) "+" else ""}$_temperature\u2103"
}

fun newRandomWeather(name: String, lat: Double, lon: Double):Weather {
    val city = City(name, lat, lon)
    val temp = (-10..30).shuffled().first()
    return Weather(
        city,
        temp,
        temp + (-5..5).shuffled().first(),
        (0..15).shuffled().first(),
        (741..748).shuffled().first()
    )
}


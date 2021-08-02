package ru.brauer.weather.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val city: City,
    val fact: WeatherDetails
) : Parcelable

fun newRandomWeather(name: String, lat: Double, lon: Double): Weather {
    val city = City(name, lat, lon)
    val temp = (-10..30).shuffled().first()
    return Weather(
        city = city,
        fact = WeatherDetails(
            temp,
            temp + (-5..5).shuffled().first(),
            (0..15).shuffled().first(),
            (741..748).shuffled().first()
        )
    )
}


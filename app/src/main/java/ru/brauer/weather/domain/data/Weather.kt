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

fun getDefaultCity() = City("Москва", getDefaultGeolocation())


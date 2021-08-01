package ru.brauer.weather.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val city: City,
    val temperature: Int,
    val feelsLike: Int,
    val windSpeed: Int,
    val pressure: Int
) : Parcelable

fun getDefaultCity() = City("Москва", getDefaultGeolocation())

fun getDefaultGeolocation() = Geolocation(55.755819, 37.617644)

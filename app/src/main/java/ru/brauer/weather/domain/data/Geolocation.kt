package ru.brauer.weather.domain.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Geolocation(val lat: Double, val lon: Double) : Parcelable

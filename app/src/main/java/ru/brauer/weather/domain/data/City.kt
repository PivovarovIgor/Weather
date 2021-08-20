package ru.brauer.weather.domain.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val name: String,
    val geolocation: Geolocation,
    val urlImage: String?
) : Parcelable {
    constructor(name: String, lat: Double, lon: Double, urlImage: String? = null) : this(name, Geolocation(lat, lon), urlImage)
}


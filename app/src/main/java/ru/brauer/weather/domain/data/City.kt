package ru.brauer.weather.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(val name: String, val geolocation: Geolocation) : Parcelable

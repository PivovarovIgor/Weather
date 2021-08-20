package ru.brauer.weather.domain.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForecastTime(
    val time: Long,
    val weatherDetails: WeatherDetails
) : Parcelable
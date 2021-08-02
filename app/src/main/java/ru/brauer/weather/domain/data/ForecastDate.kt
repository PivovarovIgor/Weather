package ru.brauer.weather.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ForecastDate(
    val date: Long,
    val forecastTime: List<ForecastTime>
) : Parcelable
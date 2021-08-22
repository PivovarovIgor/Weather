package ru.brauer.weather.domain.data


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Weather(
    val city: City,
    val fact: WeatherDetails,
    val forecast: List<ForecastDate>,
    val date: Long = Calendar.getInstance().timeInMillis
) : Parcelable {
    constructor(city: City, fact: WeatherDetails, date: Long) : this(city, fact, listOf(), date)
}

package ru.brauer.weather.domain.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherDetails(
    private val _temperature: Int,
    private val _feelsLike: Int,
    val windSpeed: Int,
    val pressure: Int
) : Parcelable {
    constructor(_temperature: Int) : this(_temperature, 0, 0, 0)
    val temperature get() = "${if (_temperature > 0) "+" else ""}$_temperature\u2103"
    val feelsLike get() = "${if (_temperature > 0) "+" else ""}$_temperature\u2103"
    val temperatureInt get() = _temperature
}
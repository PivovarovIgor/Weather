package ru.brauer.weather.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*
import java.util.Calendar.*

@Parcelize
data class Weather(
    val city: City,
    val fact: WeatherDetails,
    val forecast: List<ForecastDate>
) : Parcelable

private const val DAYS_OF_FORECAST = 7

fun newRandomWeather(city: City): Weather {

    val currentDay = Calendar.getInstance()
    val forecast = mutableListOf<ForecastDate>()
    (1..DAYS_OF_FORECAST).forEach {
        currentDay.add(DAY_OF_YEAR, 1)
        forecast += ForecastDate(
            currentDay.timeInMillis,
            newForecastTimeOfDay(Calendar.getInstance().apply {
                set(YEAR, currentDay.get(YEAR))
                set(DAY_OF_YEAR, currentDay.get(DAY_OF_YEAR))
            }
            )
        )
    }
    return Weather(city, newRandomWeatherDetails(), forecast)
}

private const val HOURS_OF_DAY = 24
private const val PARTS_OF_DAY = 4
private const val HOURS_OF_DAYS_PART = HOURS_OF_DAY / PARTS_OF_DAY

fun newForecastTimeOfDay(currentDate: Calendar): List<ForecastTime> {
    val res = mutableListOf<ForecastTime>()
    (1..PARTS_OF_DAY).forEach { index ->
        res += ForecastTime(
            Calendar.getInstance().apply {
                set(YEAR, currentDate.get(YEAR))
                set(DAY_OF_YEAR, currentDate.get(DAY_OF_YEAR))
                set(HOUR_OF_DAY, (index - 1) * HOURS_OF_DAYS_PART)
                set(MINUTE, 0)
                set(SECOND, 0)
            }.timeInMillis,
            newRandomWeatherDetails()
        )
    }
    return res
}

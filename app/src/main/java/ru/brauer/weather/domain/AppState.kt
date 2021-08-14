package ru.brauer.weather.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.brauer.weather.domain.data.Weather

sealed class AppState {

    @Parcelize
    data class Success(val weathers: List<Weather>) : AppState(), Parcelable

    @Parcelize
    class Error(val error: Throwable) : AppState(), Parcelable

    @Parcelize
    object Loading : AppState(), Parcelable
}

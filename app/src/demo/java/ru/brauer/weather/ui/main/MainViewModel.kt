package ru.brauer.weather.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.brauer.weather.domain.AppState
import ru.brauer.weather.domain.DataUpdateOperations
import ru.brauer.weather.domain.data.Weather
import ru.brauer.weather.domain.repository.PlugWeatherRepository
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private const val TIME_SLEEP = 150L

class MainViewModel : ViewModel() {

    val liveDataToObserver: MutableLiveData<AppState> = MutableLiveData()
    private val _dataWeather: MutableList<Weather> = mutableListOf()
    private val dataWeather: List<Weather>
        get() = _dataWeather
    private val exeSrv: ExecutorService = Executors.newSingleThreadExecutor()

    fun getWeathers() {

        _dataWeather.clear()

        PlugWeatherRepository.getWeathers().forEachIndexed { index, weatherObtained ->
            exeSrv.execute {
                _dataWeather += weatherObtained
                liveDataToObserver.postValue(
                    AppState.Success(
                        weatherObtained,
                        index,
                        DataUpdateOperations.ADD
                    )
                )
                Thread.sleep(TIME_SLEEP)
            }
        }
    }

    fun getCachedWeather(): List<Weather>? {
        if (liveDataToObserver.value is AppState.Success) {
            liveDataToObserver.value = null
        }
        if (dataWeather.isEmpty()) {
            getWeathers()
            return null
        }
        return dataWeather
    }
}
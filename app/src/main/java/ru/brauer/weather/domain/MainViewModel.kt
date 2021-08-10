package ru.brauer.weather.domain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.brauer.weather.domain.repository.weather.IWeatherRepository
import ru.brauer.weather.domain.repository.weather.PlugWeatherRepository
import ru.brauer.weather.domain.repository.weather.YandexWeatherRepository
import java.lang.Thread.sleep

const val SIMULATED_DELAY = 2000L

class MainViewModel(private val repository: IWeatherRepository = YandexWeatherRepository) :
    ViewModel() {

    val liveDataToObserver: MutableLiveData<AppState> = MutableLiveData()

    fun getWeathers() {

        liveDataToObserver.value = AppState.Loading
        Thread {
            sleep(SIMULATED_DELAY)
            val appState = try {
                AppState.Success(repository.getWeathers())
            } catch (ex: Throwable) {
                AppState.Error(ex)
            }
            liveDataToObserver.postValue(appState)
        }.start()
    }
}
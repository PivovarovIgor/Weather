package ru.brauer.weather.domain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.brauer.weather.domain.repository.IWeatherRepository
import ru.brauer.weather.domain.repository.PlugWeatherRepository
import java.lang.Thread.sleep

class MainViewModel(private val repository: IWeatherRepository = PlugWeatherRepository) :
    ViewModel() {

    val liveDataToObserver: MutableLiveData<AppState> = MutableLiveData()

    fun getWeathers() {

        liveDataToObserver.value = AppState.Loading
        Thread {
            sleep(2000)
            val appState = try {
                AppState.Success(repository.getWeathers())
            } catch (ex: Throwable) {
                AppState.Error(ex)
            }
            liveDataToObserver.postValue(appState)
        }.start()
    }
}
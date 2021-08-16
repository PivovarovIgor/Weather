package ru.brauer.weather.domain

import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.brauer.weather.domain.repository.weather.IWeatherRepository
import ru.brauer.weather.domain.repository.weather.YandexWeatherRepository


private const val NAME_THREAD = "thread of main view model"

class MainViewModel(private val repository: IWeatherRepository = YandexWeatherRepository) :
    ViewModel() {

    val liveDataToObserver: MutableLiveData<AppState> = MutableLiveData()
    private val handlerThread = HandlerThread(NAME_THREAD)
        .also { it.start() }
    private val handler = Handler(handlerThread.looper)

    fun getWeathers() {

        liveDataToObserver.value = AppState.Loading
        handler.post {
            val appState = try {
                AppState.Success(repository.getWeathers())
            } catch (ex: Throwable) {
                AppState.Error(ex)
            }
            liveDataToObserver.postValue(appState)
        }
    }

    fun getCachedWeather(): AppState? {
        if (liveDataToObserver.value == null
            || liveDataToObserver.value !is AppState.Success
        ) {
            getWeathers()
            return null
        }
        return liveDataToObserver.value
    }
}
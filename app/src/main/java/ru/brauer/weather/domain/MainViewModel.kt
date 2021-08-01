package ru.brauer.weather.domain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.brauer.weather.domain.data.Geolocation
import ru.brauer.weather.domain.data.getDefaultGeolocation
import ru.brauer.weather.domain.repository.IWeatherRepository
import ru.brauer.weather.domain.repository.PlugWeatherRepository
import java.lang.Thread.sleep

class MainViewModel(private val repository: IWeatherRepository = PlugWeatherRepository) :
    ViewModel() {

    val liveDataToObserver: MutableLiveData<AppState> = MutableLiveData()

    fun getWeathers() {

        val geolocations = getGeolocationsFromPreference()

        liveDataToObserver.value = AppState.Loading
        Thread {
            sleep(2000)
            val appState = try {
                AppState.Success(repository.getWeathers(geolocations))
            } catch (ex: Throwable) {
                AppState.Error(ex)
            }
            liveDataToObserver.postValue(appState)
        }.start()
    }

    private fun getGeolocationsFromPreference(): List<Geolocation> {
        //TODO: customization of the list of cities will be implemented
        return listOf(getDefaultGeolocation())
    }

}
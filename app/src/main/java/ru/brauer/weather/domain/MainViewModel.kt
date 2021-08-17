package ru.brauer.weather.domain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.brauer.weather.domain.repository.ResponseErrors
import ru.brauer.weather.domain.repository.cities.CityRepository
import ru.brauer.weather.domain.repository.dto.WeatherDTO
import ru.brauer.weather.domain.repository.weather.IWeatherRepository
import ru.brauer.weather.domain.repository.weather.YandexWeatherRepository
import ru.brauer.weather.domain.repository.weather.getWeatherDataFromRawData

class MainViewModel(private val repository: IWeatherRepository = YandexWeatherRepository) :
    ViewModel() {

    val liveDataToObserver: MutableLiveData<AppState> = MutableLiveData()

    fun getWeathers() {

        liveDataToObserver.value = AppState.Loading

        CityRepository.cities.forEach { city ->
            repository.getWeather(city,
                object : Callback<WeatherDTO> {
                    override fun onResponse(
                        call: Call<WeatherDTO>,
                        response: Response<WeatherDTO>
                    ) {
                        val serverResponse: WeatherDTO? = response.body()
                        liveDataToObserver.value =
                            if (response.isSuccessful && serverResponse != null) {
                                checkResponse(serverResponse)
                            } else {
                                AppState.ResponseWithError(ResponseErrors.SERVER_ERROR)
                            }
                    }

                    override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                        liveDataToObserver.value = AppState.Error(t)
                    }

                    private fun checkResponse(serverResponse: WeatherDTO): AppState {
                        return serverResponse.getWeatherDataFromRawData(city)
                            ?.let { AppState.Success(it) }
                            ?: AppState.ResponseWithError(ResponseErrors.CORRUPTED_DATA)
                    }
                }
            )
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
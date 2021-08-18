package ru.brauer.weather.domain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.brauer.weather.domain.data.Weather
import ru.brauer.weather.domain.repository.ResponseErrors
import ru.brauer.weather.domain.repository.cities.CityRepository
import ru.brauer.weather.domain.repository.dto.WeatherDTO
import ru.brauer.weather.domain.repository.weather.IWeatherRepository
import ru.brauer.weather.domain.repository.weather.YandexWeatherRepository
import ru.brauer.weather.domain.repository.weather.getWeatherDataFromRawData

class MainViewModel(private val repository: IWeatherRepository = YandexWeatherRepository) :
    ViewModel() {

    val liveDataToObserver: MutableLiveData<AppState> = MutableLiveData()
    private val _dataWeather: MutableList<Weather> = mutableListOf()
    val dataWeather: List<Weather>
        get() = _dataWeather

    fun getWeathers() {
        _dataWeather.clear()

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
                                val res = checkResponse(serverResponse)

                                res
                            } else {
                                AppState.ResponseWithError(ResponseErrors.SERVER_ERROR)
                            }
                    }

                    override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                        liveDataToObserver.value = AppState.Error(t)
                    }

                    private fun checkResponse(serverResponse: WeatherDTO): AppState {
                        return serverResponse.getWeatherDataFromRawData(city)
                            ?.let { weatherObtained ->
                                var indexToAdd = _dataWeather.indexOf(weatherObtained)
                                var operation = DataUpdateOperations.ADD
                                if (indexToAdd >= 0) {
                                    _dataWeather[indexToAdd] = weatherObtained
                                    operation = DataUpdateOperations.UPDATE
                                } else {
                                    indexToAdd = _dataWeather.indexOfFirst { weatheEl ->
                                        weatheEl.city.name > weatherObtained.city.name
                                    }
                                    if (indexToAdd == -1) {
                                        indexToAdd = _dataWeather.size
                                    }
                                }
                                _dataWeather.add(indexToAdd, weatherObtained)
                                AppState.Success(weatherObtained, indexToAdd, operation)
                            }
                            ?: AppState.ResponseWithError(ResponseErrors.CORRUPTED_DATA)
                    }
                }
            )
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
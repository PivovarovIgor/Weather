package ru.brauer.weather.ui.background

import android.app.IntentService
import android.content.Intent
import ru.brauer.weather.domain.AppState
import ru.brauer.weather.domain.repository.weather.YandexWeatherRepository

const val LOAD_SERVICE_EXTRA = "LOAD_SERVICE_EXTRA"
const val BROADCAST_RESULT_LOADING = "ru.brauer.weather.RESULT_LOADING"

class LoaderService(name: String = "LoaderService") : IntentService(name) {

    override fun onHandleIntent(intent: Intent?) {

        sendBroadcast(Intent(BROADCAST_RESULT_LOADING).apply {
            putExtra(LOAD_SERVICE_EXTRA, AppState.Loading)
        })

        val res = try {
            AppState.Success(YandexWeatherRepository.getWeathers())
        } catch (ex: Throwable) {
            AppState.Error(ex)
        }

        sendBroadcast(Intent(BROADCAST_RESULT_LOADING).apply {
            putExtra(LOAD_SERVICE_EXTRA, res)
        })
    }
}
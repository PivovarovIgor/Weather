package ru.brauer.weather.ui.main.details

import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.ViewModel
import ru.brauer.weather.App.Companion.getHistoryDao
import ru.brauer.weather.domain.data.Weather
import ru.brauer.weather.domain.repository.HistoryLocalRepository
import ru.brauer.weather.domain.repository.IHistoryLocalRepository

private const val HANDLER_THREAD_NAME = "ru.brauer.weather.ui.main.details.HANDLER_THREAD"

class DetailsViewModel(
    private val localHistoryRepository: IHistoryLocalRepository = HistoryLocalRepository(
        requireNotNull(getHistoryDao())
    )
) : ViewModel() {

    private val handlerThread = HandlerThread(HANDLER_THREAD_NAME)
        .apply { start() }
    private val handler = Handler(handlerThread.looper)

    fun saveCityToDB(weather: Weather) {

        handler.post {
            localHistoryRepository.saveEntity(weather)
        }
    }
}
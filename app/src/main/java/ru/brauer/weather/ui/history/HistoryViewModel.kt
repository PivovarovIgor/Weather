package ru.brauer.weather.ui.history

import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.brauer.weather.App.Companion.getHistoryDao
import ru.brauer.weather.domain.AppState
import ru.brauer.weather.domain.repository.HistoryLocalRepository
import ru.brauer.weather.domain.repository.IHistoryLocalRepository

private const val HANDLER_THREAD_NAME = "ru.brauer.weather.ui.history.HANDLER_THREAD"

class HistoryViewModel(
    val historyLiveData: MutableLiveData<AppState> = MutableLiveData()
) : ViewModel() {
    private val historyRepository: IHistoryLocalRepository = HistoryLocalRepository(
        requireNotNull(getHistoryDao())
    )
    private val handlerThread = HandlerThread(HANDLER_THREAD_NAME)
        .apply { start() }
    private val handler = Handler(handlerThread.looper)

    fun getAllHistory() {
        historyLiveData.value = AppState.Loading
        handler.post {
            historyLiveData.postValue(
                AppState.Success(
                    historyRepository.getAllHistory().sortedByDescending { it.date })
            )
        }
    }
}
package ru.brauer.weather

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.room.Room
import ru.brauer.weather.domain.repository.room.HistoryDao
import ru.brauer.weather.domain.repository.room.HistoryDataBase

class App : Application() {

    companion object {
        private var appInstance: App? = null
        private var db: HistoryDataBase? = null
        private const val DB_NAME = "History.db"

        fun getHistoryDao(): HistoryDao? {
            if (db == null) {
                synchronized(HistoryDataBase::class.java) {
                    if (db == null) {
                        if (appInstance == null) {
                            throw IllegalStateException("Application is null while creating DataBase")
                        }
                        db = appInstance?.applicationContext?.let {
                            Room.databaseBuilder(
                                it,
                                HistoryDataBase::class.java,
                                DB_NAME
                            ).allowMainThreadQueries()
                                .build()
                        }
                    }
                }
            }
            return db?.historyDao()
        }
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            initCallbackConnectivity()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initCallbackConnectivity() {

        val connectivityManager = ContextCompat.getSystemService(
            applicationContext,
            ConnectivityManager::class.java
        ) ?: return

        connectivityManager.registerDefaultNetworkCallback(
            object : ConnectivityManager.NetworkCallback() {
                override fun onLost(network: Network) {
                    super.onLost(network)
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.connection_lost),
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.connected),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}
package ru.brauer.weather

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class App : Application() {

    override fun onCreate() {
        super.onCreate()
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
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Toast.makeText(applicationContext, "connected", Toast.LENGTH_LONG).show()
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    Toast.makeText(applicationContext, "connection lost", Toast.LENGTH_LONG).show()
                }
            })
    }
}
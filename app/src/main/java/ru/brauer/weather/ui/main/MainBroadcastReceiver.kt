package ru.brauer.weather.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.core.net.ConnectivityManagerCompat

class MainBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        //val isFailedConnect = intent.getBooleanExtra(ConnectivityManagerCompat.)
        StringBuilder().apply {
            append("State of connecting.\n")
            append("info: ${intent?.extras?.getString(ConnectivityManager.EXTRA_EXTRA_INFO)}\n")
            append("no connectivity: ${intent?.extras?.getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY)}\n")
            append("reason: ${intent?.extras?.getString(ConnectivityManager.EXTRA_REASON)}\n")
            append("is failover: ${intent?.extras?.getBoolean(ConnectivityManager.EXTRA_IS_FAILOVER)}\n")
            append("Action: ${intent?.action}")
            toString().also {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
   }
}
package ru.brauer.weather.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.ConnectivityManagerCompat

class MainBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        //val isFailedConnect = intent.getBooleanExtra(ConnectivityManagerCompat.)
        StringBuilder().apply {
            append("State of connecting.\n")
            append(intent?.extras?.size())
            append("Action: ${intent?.action}")
            toString().also {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
   }
}
package ru.brauer.weather.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast

class MainBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent == null) {
            return
        }

        val noConnectivity =
            intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
        val info = intent.getStringExtra(ConnectivityManager.EXTRA_EXTRA_INFO)

        StringBuilder().apply {
            append("Old fashion get connectivity info.\n")
            if (noConnectivity) {
                append("No connectivity")
            } else {
                append("Connected to $info")
            }
            toString().also { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
        }
    }
}
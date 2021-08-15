package ru.brauer.weather.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import ru.brauer.weather.R

class MainBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent == null || context == null) {
            return
        }

        val noConnectivity =
            intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
        val info = intent.getStringExtra(ConnectivityManager.EXTRA_EXTRA_INFO)

        StringBuilder().apply {
            if (noConnectivity) {
                append(context.getString(R.string.connection_lost))
            } else {
                append(context.getString(R.string.connected_to, info))
            }
            toString().also { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
        }
    }
}
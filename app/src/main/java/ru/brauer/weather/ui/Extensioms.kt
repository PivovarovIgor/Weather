package ru.brauer.weather.ui

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

fun View.showSnackbar(@StringRes stringRes: Int) =
    Snackbar.make(this, stringRes, Snackbar.LENGTH_SHORT)
        .also { it.show() }

fun Long.toDateFormat() = SimpleDateFormat
    .getDateInstance()
    .format(Date(this)) ?: ""

fun Long.toTimeFormat() = SimpleDateFormat
    .getTimeInstance(SimpleDateFormat.SHORT)
    .format(Date(this)) ?: ""
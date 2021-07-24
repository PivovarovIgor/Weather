package ru.brauer.weather.ui

import ru.brauer.weather.domain.Weather
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.brauer.weather.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val weather = Weather("Moscow", 20)

        val (city, temp) = weather

        val someButton = findViewById<Button>(R.id.some_button)
        someButton.setOnClickListener {
            Toast.makeText(
                this@MainActivity,
                "In $city temperature is $temp $weather",
                Toast.LENGTH_LONG
            )
                .show()
        }
    }
}
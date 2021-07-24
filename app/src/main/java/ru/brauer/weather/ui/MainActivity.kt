package ru.brauer.weather.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.brauer.weather.R
import ru.brauer.weather.domain.Weather
import ru.brauer.weather.domain.repository.PlugRepository

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

            val data = repository.data;

            for (currentWeather in data) {
                println("for (currentWeather in data) ${currentWeather.city} : ${currentWeather.temperature}")
            }

            for (index in 1 until data.size) {
                println("for (index in 1 until data.size) " + data[index])
            }

            for (index in data.size - 1 downTo 0) {
                println("for (index in data.size - 1 downTo 0) " + data[index])
            }

            for (index in data.indices) {
                println("for (index in data.indices) " + data[index])
            }

            for ((index, value) in data.withIndex()) {
                println("for ((index, value) in data.withIndex()) $index : $value")
            }

            data.forEach(::println)
        }
    }

    companion object {
        val repository = PlugRepository;
    }
}
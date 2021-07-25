package ru.brauer.weather.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import ru.brauer.weather.R
import ru.brauer.weather.domain.Weather
import ru.brauer.weather.domain.repository.PlugWeatherRepository

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
                "In $city temperature is $temp ${weather.copy()}",
                Toast.LENGTH_LONG
            )
                .show()

            val weathers = repository.weathers;

            for (currentWeather in weathers) {
                println("for (currentWeather in data) ${currentWeather.city} : ${currentWeather.temperature}")
            }

            for (index in 1 until weathers.size) {
                println("for (index in 1 until data.size) " + weathers[index])
            }

            for (index in weathers.size - 1 downTo 0) {
                println("for (index in data.size - 1 downTo 0) " + weathers[index])
            }

            for (index in weathers.indices) {
                println("for (index in data.indices) " + weathers[index])
            }

            for ((index, value) in weathers.withIndex()) {
                println("for ((index, value) in data.withIndex()) $index : $value")
            }

            weathers.forEach(::println)
        }

        val weathersListView = findViewById<RecyclerView>(R.id.weathers_list)
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(this, R.drawable.ic_launcher_background)
            ?.let(divider::setDrawable)
        weathersListView.addItemDecoration(divider)
        weathersListView.adapter = WeatherListAdapter(repository)
    }

    companion object {
        val repository = PlugWeatherRepository;
    }
}
package ru.brauer.weather.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.brauer.weather.R
import ru.brauer.weather.domain.Weather
import ru.brauer.weather.domain.repository.IWeatherRepository

class WeatherListAdapter(private val repository: IWeatherRepository) :
    RecyclerView.Adapter<WeatherListAdapter.ViewHolder>() {
    private val weathersData: List<Weather>

    init {
        weathersData = repository.weathers
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cityView: TextView
        private val temperatureView: TextView

        init {
            cityView = view.findViewById(R.id.caption_city)
            temperatureView = view.findViewById(R.id.temperature)
        }

        fun bindData(weather: Weather) {
            cityView.text = weather.city
            temperatureView.text = weather.temperature.toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_weather,
            parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherListAdapter.ViewHolder, position: Int) =
        holder.bindData(weathersData[position])

    override fun getItemCount(): Int = weathersData.size
}
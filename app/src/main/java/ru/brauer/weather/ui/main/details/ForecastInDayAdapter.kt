package ru.brauer.weather.ui.main.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.brauer.weather.databinding.RecyclerItemForecastInDayBinding
import ru.brauer.weather.domain.data.ForecastTime
import ru.brauer.weather.ui.toTimeFormat

class ForecastInDayAdapter : RecyclerView.Adapter<ForecastInDayAdapter.ViewHolder>() {

    var data: List<ForecastTime> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecyclerItemForecastInDayBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).let(::ViewHolder)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class ViewHolder(private val binding: RecyclerItemForecastInDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(forecastTime: ForecastTime) {
            binding.apply {
                timeOfForecast.text = forecastTime.time.toTimeFormat()
                temperature.text = forecastTime.weatherDetails.temperature
            }
        }
    }
}
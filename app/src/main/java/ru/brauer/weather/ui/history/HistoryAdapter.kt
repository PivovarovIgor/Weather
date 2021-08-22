package ru.brauer.weather.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.brauer.weather.databinding.RecycleItemHistoryBinding
import ru.brauer.weather.domain.data.Weather
import ru.brauer.weather.ui.toDateFormat
import ru.brauer.weather.ui.toTimeFormat

class HistoryAdapter() : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    var data: List<Weather> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            RecycleItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: RecycleItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weather: Weather) {
            binding.apply {
                cityName.text = weather.city.name
                date.text = weather.date.toDateFormat()
                time.text = weather.date.toTimeFormat()
                temperature.text = weather.fact.temperature
            }
        }
    }
}
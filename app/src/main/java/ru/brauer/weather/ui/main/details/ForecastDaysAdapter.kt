package ru.brauer.weather.ui.main.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.brauer.weather.databinding.RecyclerItemForecastOfDaysBinding
import ru.brauer.weather.domain.data.ForecastDate
import ru.brauer.weather.ui.toDateFormat
import java.util.*

class ForecastDaysAdapter : RecyclerView.Adapter<ForecastDaysAdapter.ViewHolder>() {
    var data: List<ForecastDate> = listOf()
        set(value) {
            field = value
            this@ForecastDaysAdapter.notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecyclerItemForecastOfDaysBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).let(::ViewHolder)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class ViewHolder(private val binding: RecyclerItemForecastOfDaysBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(forecastDay: ForecastDate) {
            binding.apply {
                dateOfForecast.text = forecastDay.date.toDateFormat()
                recyclerItemForecastInDay.adapter = ForecastInDayAdapter().apply {
                    data = forecastDay.forecastTime
                }
            }
        }
    }
}
package ru.brauer.weather.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.brauer.weather.databinding.RecyclerItemForecastOfDaysBinding
import ru.brauer.weather.domain.data.ForecastDate
import java.text.SimpleDateFormat
import java.util.*

class ForecastDaysAdapter : RecyclerView.Adapter<ForecastDaysAdapter.ViewHolder>() {

    var data: List<ForecastDate> = listOf()
        set(value) {
            field = value
            this@ForecastDaysAdapter.notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerItemForecastOfDaysBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(private val binding: RecyclerItemForecastOfDaysBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(forecastDay: ForecastDate) {
            binding.apply {
                dateOfForecast.text = SimpleDateFormat
                    .getDateInstance()
                    .format(Date(forecastDay.date))
                recyclerItemForecastInDay.adapter = ForecastInDayAdapter().apply {
                    data = forecastDay.forecastTime
                }
            }
        }
    }
}
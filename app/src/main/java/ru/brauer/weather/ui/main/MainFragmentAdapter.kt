package ru.brauer.weather.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.brauer.weather.R
import ru.brauer.weather.domain.data.Weather

class MainFragmentAdapter : RecyclerView.Adapter<MainFragmentAdapter.ViewHolder>() {

    var data: List<Weather> = listOf()
        set(value) {
            field = value
            this@MainFragmentAdapter.notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.main_recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(
        private val view: View,
        private val city: TextView? = view.findViewById(R.id.caption_city),
        private val temperature: TextView? = view.findViewById(R.id.temperature)
    ) : RecyclerView.ViewHolder(view) {

        fun bind(weather: Weather) {
            city?.apply { text = weather.city.name }
            temperature?.apply { text = weather.temperature.toString() }
        }
    }
}
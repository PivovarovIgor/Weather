package ru.brauer.weather.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.brauer.weather.databinding.RecyclerItemMainBinding
import ru.brauer.weather.domain.data.Weather

class MainFragmentAdapter : RecyclerView.Adapter<MainFragmentAdapter.ViewHolder>() {

    private val data: MutableList<Weather> = mutableListOf()

    var onClickItemViewListener: MainFragment.OnClickItemViewListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecyclerItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .let(::ViewHolder)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    fun addWeather(weather: Weather) {
        val indexOfEntry = data.indexOf(weather)
        if (indexOfEntry >= 0) {
            data[indexOfEntry] = weather
            notifyItemChanged(indexOfEntry)
        } else {
            data += weather
            data.sortBy { it.city.name }
            notifyDataSetChanged()
        }
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: RecyclerItemMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(weather: Weather) {
            binding.apply {
                captionCity.text = weather.city.name
                temperature.text = weather.fact.temperature
                root.setOnClickListener { onClickItemViewListener?.onClickItemView(weather) }
            }
        }
    }
}
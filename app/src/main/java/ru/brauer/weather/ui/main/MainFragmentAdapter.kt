package ru.brauer.weather.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.brauer.weather.databinding.RecyclerItemMainBinding
import ru.brauer.weather.domain.data.Weather

class MainFragmentAdapter : RecyclerView.Adapter<MainFragmentAdapter.ViewHolder>() {

    var data: List<Weather> = listOf()
        set(value) {
            field = value
            this@MainFragmentAdapter.notifyDataSetChanged()
        }

    var onClickItemViewListener: MainFragment.OnClickItemViewListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RecyclerItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
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
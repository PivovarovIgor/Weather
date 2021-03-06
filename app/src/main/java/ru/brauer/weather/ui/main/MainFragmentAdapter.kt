package ru.brauer.weather.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import ru.brauer.weather.R
import ru.brauer.weather.databinding.RecyclerItemMainBinding
import ru.brauer.weather.domain.AppState
import ru.brauer.weather.domain.DataUpdateOperations
import ru.brauer.weather.domain.data.Weather
import ru.brauer.weather.ui.main.details.DetailsFragment

class MainFragmentAdapter : RecyclerView.Adapter<MainFragmentAdapter.ViewHolder>() {

    private var data: MutableList<AppState.Success> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecyclerItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .let(::ViewHolder)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position].weather)
    }

    override fun getItemCount() = data.size

    fun addWeather(dataToUpdate: AppState.Success) {
        val indexToAdd = data
            .indexOfFirst { dataToUpdate.indexToAdd <= it.indexToAdd }
            .let { if (it == -1) data.size else it }
        if (dataToUpdate.operation == DataUpdateOperations.UPDATE) {
            data[indexToAdd] = dataToUpdate
            notifyItemChanged(indexToAdd)
        } else {
            data.add(indexToAdd, dataToUpdate)
            notifyItemInserted(indexToAdd)
        }
    }

    fun setData(weathers: List<Weather>) {
        data.clear()
        weathers.forEachIndexed { index, weather ->
            data += AppState.Success(weather, index, DataUpdateOperations.ADD)
        }
        notifyDataSetChanged()
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
                root.setOnClickListener {
                    val bundle = bundleOf(DetailsFragment.KEY_WEATHER to weather)
                    Navigation
                        .findNavController(binding.root)
                        .navigate(R.id.action_navigation_home_to_navigation_details, bundle)
                }
            }
        }
    }
}


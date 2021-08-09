package ru.brauer.weather.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.brauer.weather.R
import ru.brauer.weather.databinding.FragmentDetailsBinding
import ru.brauer.weather.domain.data.Weather

class DetailsFragment : Fragment() {

    companion object {
        private const val KEY_WEATHER = "KEY_WEATHER"

        fun newInstance(weather: Weather) = DetailsFragment()
            .apply {
                val bundle = Bundle()
                bundle.putParcelable(KEY_WEATHER, weather)
                arguments = bundle
            }
    }

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather: Weather? = arguments?.getParcelable(KEY_WEATHER)
        weather?.also(::renderData)
    }

    private fun renderData(weather: Weather) {
        binding?.apply {
            captionCity.text = weather.city.name
            temperature.text = weather.fact.temperature
            feelsLike.text = getString(R.string.feel_like, weather.fact.feelsLike)
            pressure.text = getString(R.string.pressure, weather.fact.pressure.toString())
            windSpeed.text = getString(R.string.wind_speed, weather.fact.windSpeed.toString())
            forecastOfDays.adapter = ForecastDaysAdapter()
                .apply { data = weather.forecast }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
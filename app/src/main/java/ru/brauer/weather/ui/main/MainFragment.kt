package ru.brauer.weather.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.brauer.weather.databinding.FragmentMainBinding
import ru.brauer.weather.domain.AppState
import ru.brauer.weather.domain.MainViewModel
import ru.brauer.weather.domain.data.Weather

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val observer = Observer(::renderData)
        viewModel.liveDataToObserver.observe(viewLifecycleOwner, observer)
        viewModel.getWeathers()
        binding?.apply {
            toUpdate.setOnClickListener {
                clearShow()
                viewModel.getWeathers()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(appState: AppState) {
        binding?.apply { progressBar.visibility = View.GONE }
        when (appState) {
            is AppState.Success -> {
                showSuccessResult(appState.weathers)
            }
            is AppState.Loading -> {
                binding?.run { progressBar.visibility = View.VISIBLE }
            }
            is AppState.Error -> {
                Snackbar.make(binding!!.root, appState.error.message ?: "", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun showSuccessResult(weathers: List<Weather>) {
        //TODO: Displaying the list of cities in pages will be implemented.
        val weather = weathers[0]
        binding?.apply {
            captionCity.text = weather.city.name
            temperature.text = weather.temperature.toString()
            feelsLike.text = weather.feelsLike.toString()
            pressure.text = weather.pressure.toString()
            windSpeed.text = weather.windSpeed.toString()
        }
    }

    private fun clearShow() {
        binding?.apply {
            captionCity.text = ""
            temperature.text = ""
            feelsLike.text = ""
            pressure.text = ""
            windSpeed.text = ""
        }
    }
}
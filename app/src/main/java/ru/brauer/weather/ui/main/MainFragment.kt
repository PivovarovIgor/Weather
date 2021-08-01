package ru.brauer.weather.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.brauer.weather.R
import ru.brauer.weather.databinding.FragmentMainBinding
import ru.brauer.weather.domain.AppState
import ru.brauer.weather.domain.MainViewModel
import ru.brauer.weather.domain.data.Weather

class MainFragment : Fragment() {

    private lateinit var adapter: MainFragmentAdapter
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
        adapter = MainFragmentAdapter()
        binding?.apply { listOfWeathers.adapter = adapter }
        viewModel.liveDataToObserver.observe(viewLifecycleOwner, observer)
        viewModel.getWeathers()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(appState: AppState) =
        binding?.run {
            progressBar.visibility = View.GONE
            when (appState) {
                is AppState.Success -> {
                    showSuccessResult(appState.weathers)
                }
                is AppState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is AppState.Error -> {
                    Snackbar.make(
                        root,
                        appState.error.message ?: "",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(
                            R.string.button_refresh
                        ) { viewModel.getWeathers() }
                        .show()
                }
            }
        }

    private fun showSuccessResult(weathers: List<Weather>) {
        adapter.data = weathers
    }
}
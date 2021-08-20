package ru.brauer.weather.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import ru.brauer.weather.R
import ru.brauer.weather.databinding.FragmentMainBinding
import ru.brauer.weather.domain.AppState
import ru.brauer.weather.domain.repository.ResponseErrors

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding

    private val viewModel: MainViewModel by navGraphViewModels(R.id.mobile_navigation)
    private val adapter: MainFragmentAdapter by lazy {
        MainFragmentAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        viewModel.getCachedWeather()?.let(adapter::setData)
        viewModel.liveDataToObserver.observe(viewLifecycleOwner, Observer(::renderData))
        binding?.let {
            it.swipeContainer.setOnRefreshListener {
                reloadWeathers()
            }
        }
    }

    private fun initRecyclerView() {
        binding?.apply {
            listOfWeathers.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(appState: AppState?) = appState?.let { _ ->
        binding?.run {
            Log.d("MainFragment", "renderData $appState")
            progressBar.visibility = View.GONE
            when (appState) {
                is AppState.Success -> {
                    adapter.addWeather(appState)
                }
                is AppState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    swipeContainer.isRefreshing = false
                }
                is AppState.Error -> {
                    Snackbar.make(
                        root,
                        appState.error.message ?: "",
                        Snackbar.LENGTH_LONG
                    ).setAction(R.string.button_refresh) { viewModel.getWeathers() }
                        .show()
                }
                is AppState.ResponseWithError -> {
                    val message =
                        when (appState.responseError) {
                            ResponseErrors.SERVER_ERROR -> getString(R.string.server_error)
                            ResponseErrors.CORRUPTED_DATA -> getString(R.string.corrupted_data)
                        }
                    Snackbar.make(
                        root,
                        message,
                        Snackbar.LENGTH_LONG
                    ).setAction(R.string.button_refresh) { reloadWeathers() }
                        .show()
                }
            }
        }
    }

    private fun reloadWeathers() {
        adapter.clear()
        viewModel.getWeathers()
    }
}
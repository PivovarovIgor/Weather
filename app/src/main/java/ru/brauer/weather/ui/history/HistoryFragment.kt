package ru.brauer.weather.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import ru.brauer.weather.R
import ru.brauer.weather.databinding.FragmentHistoryBinding
import ru.brauer.weather.domain.AppState

class HistoryFragment : Fragment() {

    private var binding: FragmentHistoryBinding? = null
    private val viewModel: HistoryViewModel by navGraphViewModels(R.id.mobile_navigation)
    private val adapter: HistoryAdapter by lazy { HistoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.historyLiveData.observe(viewLifecycleOwner, Observer(::renderData))
        viewModel.getAllHistory()
        binding?.apply {
            historyList.adapter = adapter
        }
    }

    private fun renderData(appState: AppState?) {
        binding?.apply {
            includeProgress.progressBar.visibility = View.GONE
            when (appState) {
                is AppState.Loading -> {
                    includeProgress.progressBar.visibility = View.VISIBLE
                }
                is AppState.Error -> {
                    Snackbar.make(
                        root,
                        appState.error.message ?: "",
                        Snackbar.LENGTH_LONG
                    ).setAction(R.string.button_refresh) { viewModel.getAllHistory() }
                        .show()
                }
                is AppState.Success -> {
                    adapter.data = appState.weathers
                }
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
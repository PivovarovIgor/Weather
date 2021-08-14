package ru.brauer.weather.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*
import ru.brauer.weather.R
import ru.brauer.weather.databinding.FragmentMainBinding
import ru.brauer.weather.domain.AppState
import ru.brauer.weather.domain.data.Weather
import ru.brauer.weather.ui.background.BROADCAST_RESULT_LOADING
import ru.brauer.weather.ui.background.LOAD_SERVICE_EXTRA
import ru.brauer.weather.ui.background.LoaderService
import ru.brauer.weather.ui.details.DetailsFragment

class MainFragment : Fragment() {

    private lateinit var adapter: MainFragmentAdapter

    private val loadingReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                (it.getParcelableExtra<Parcelable>(LOAD_SERVICE_EXTRA) as AppState)
                    .let(::renderData)
            }
        }
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
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
        context?.registerReceiver(loadingReceiver, IntentFilter(BROADCAST_RESULT_LOADING))
        initRecyclerView()
        swipe_container.setOnRefreshListener {
            getWeathers()
        }
        getWeathers()
    }

    private fun getWeathers() {
        context?.startService(Intent(context, LoaderService::class.java))
    }

    private fun initRecyclerView() {
        adapter = MainFragmentAdapter()
        adapter.onClickItemViewListener = object : OnClickItemViewListener {
            override fun onClickItemView(weather: Weather) {
                showDetail(weather)
            }
        }
        binding?.apply { listOfWeathers.adapter = adapter }
    }

    private fun showDetail(weather: Weather) {
        activity?.apply {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, DetailsFragment.newInstance(weather), null)
                .addToBackStack(null)
                .setTransition(TRANSIT_FRAGMENT_FADE)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter.onClickItemViewListener = null
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(loadingReceiver)
    }

    private fun renderData(appState: AppState) =
        binding?.run {
            Log.d("MainFragment", "renderData $appState")
            progressBar.visibility = View.GONE
            when (appState) {
                is AppState.Success -> {
                    showSuccessResult(appState.weathers)
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
                    ).setAction(R.string.button_refresh) { getWeathers() }
                        .show()
                }
            }
        }

    private fun showSuccessResult(weathers: List<Weather>) {
        adapter.data = weathers
    }

    interface OnClickItemViewListener {
        fun onClickItemView(weather: Weather)
    }
}
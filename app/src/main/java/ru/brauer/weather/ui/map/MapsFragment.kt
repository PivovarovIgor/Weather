package ru.brauer.weather.ui.map

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ru.brauer.weather.R
import ru.brauer.weather.databinding.FragmentMapsBinding
import ru.brauer.weather.domain.data.Weather
import java.io.IOException

class MapsFragment : Fragment() {

    private lateinit var map: GoogleMap
    private var currentLatLng: LatLng? = null
    private var currentTitle: String? = null
    private var currentMarker: Marker? = null
    private var currentWeather: Weather? = null

    private val binding get() = _binding!!
    private var _binding: FragmentMapsBinding? = null

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        googleMap.setOnMapLongClickListener { latLng ->
            getWeatherAsync(latLng)
        }
    }

    private fun getWeatherAsync(latLng: LatLng) {
        val geocoder = Geocoder(context)
        val handler = Handler(Looper.getMainLooper())
        Thread {
            try {
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (addresses.isNotEmpty()) {
                    handler.post {
                        setMarker(latLng, addresses.first().let {
                            "${it.getAddressLine(0)}, ${it.locality}, ${it.countryName}"
                        })
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initSearchByAddress()
    }

    private fun initSearchByAddress() {
        binding.buttonSearch.setOnClickListener {
            val geocoder = Geocoder(it.context)
            val searchText = binding.searchAddress.text.toString()
            Thread {
                try {
                    val addresses = geocoder.getFromLocationName(searchText, 1)
                    if (addresses.isNotEmpty()) {
                        goToAddress(addresses, it, searchText)
                    } else {
                        it.post {
                            Toast.makeText(
                                it.context,
                                "Location '$searchText' not found.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    private fun goToAddress(addresses: List<Address>, view: View?, searchText: String) {
        val location = LatLng(addresses.first().latitude, addresses.first().longitude)
        view?.post {
            setMarkerAndMoveCamera(location, searchText)
        }
    }

    private fun setMarkerAndMoveCamera(
        location: LatLng,
        searchText: String
    ) {
        setMarker(location, searchText)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    private fun setMarker(
        location: LatLng? = null,
        titleText: String? = null,
        weather: Weather? = null
    ) {
        currentMarker?.let { marker ->
            currentWeather?.let { _ ->
                marker.remove()
                currentMarker = null
                currentWeather = null
                currentTitle = null
                currentLatLng = null
            }
        }

        if (location != null) {
            currentLatLng
        }
        if (titleText != null) {
            currentTitle = titleText
        }
        if (weather != null) {
            currentWeather = weather
        }

        currentLatLng?.let { latLon ->
            currentWeather?.let { weather ->
                currentTitle?.let { title ->
                    {
                        currentMarker = MarkerOptions()
                            .position(latLon)
                            .title(title)
                            .snippet(weather.fact.temperature)
                            .let {
                                map.addMarker(it)
                            }?.also { marker ->
                                marker.showInfoWindow()
                            }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.brauer.weather.R
import ru.brauer.weather.databinding.FragmentMapsBinding
import ru.brauer.weather.domain.data.City
import ru.brauer.weather.domain.data.Weather
import ru.brauer.weather.domain.repository.dto.WeatherDTO
import ru.brauer.weather.domain.repository.weather.YandexWeatherRepository
import ru.brauer.weather.domain.repository.weather.getWeatherDataFromRawData
import java.io.IOException

private const val ZOOM_ON_MOVE_MAP = 15f

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

        currentWeather = null
        currentTitle = null
        currentLatLng = latLng

        setMarker()

        val geocoder = Geocoder(context)
        val handler = Handler(Looper.getMainLooper())
        Thread {
            try {
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (addresses.isNotEmpty()) {
                    handler.post {
                        addresses.first().let {
                            currentTitle =
                                "${it.getAddressLine(0)}, ${it.locality}, ${it.countryName}"
                        }
                        setMarker()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
        getWeather(latLng)
    }

    private fun getWeather(latLng: LatLng) {
        val city = City("", latLng.latitude, latLng.longitude)
        YandexWeatherRepository.getWeather(
            city,
            object : Callback<WeatherDTO> {
                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    val weatherDTO: WeatherDTO? = response.body()
                    if (response.isSuccessful && weatherDTO != null) {
                        weatherDTO.getWeatherDataFromRawData(city)?.let {
                            currentWeather = it
                            setMarker()
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_LONG).show()
                }
            })
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
                                getString(R.string.warning_not_found_location_on_text_search, searchText),
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
            getWeather(location)
        }
    }

    private fun setMarkerAndMoveCamera(
        location: LatLng,
        searchText: String
    ) {
        currentLatLng = location
        currentTitle = searchText
        setMarker()
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, ZOOM_ON_MOVE_MAP))
    }

    private fun setMarker() {

        currentMarker?.let { it.remove() }

        currentMarker = MarkerOptions().apply {
            currentLatLng?.let { position(it) }
            currentTitle?.let { title(it) }
            currentWeather?.let { snippet(it.fact.temperature) }
        }.let {
            map.addMarker(it)
        }?.also { marker ->
            if (currentTitle != null || currentWeather != null) {
                marker.showInfoWindow()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
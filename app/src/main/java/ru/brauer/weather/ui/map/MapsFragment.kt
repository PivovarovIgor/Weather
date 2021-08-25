package ru.brauer.weather.ui.map

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ru.brauer.weather.R
import ru.brauer.weather.databinding.FragmentMapsBinding
import java.io.IOException

class MapsFragment : Fragment() {

    private lateinit var map: GoogleMap
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val initPlace = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(initPlace).title("Start_marker"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initPlace))
    }

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

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
            setMarker(location, searchText)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }

    private fun setMarker(location: LatLng, searchText: String) {
        MarkerOptions()
            .position(location)
            .title(searchText)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
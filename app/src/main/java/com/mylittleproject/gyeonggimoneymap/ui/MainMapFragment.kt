package com.mylittleproject.gyeonggimoneymap.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mylittleproject.gyeonggimoneymap.R
import com.mylittleproject.gyeonggimoneymap.common.LOCATION_PERMISSION_REQUEST_CODE
import com.mylittleproject.gyeonggimoneymap.databinding.FragmentMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource

class MainMapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val markerList = mutableListOf<Marker>()
    private var symbolMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        configMap()
        setOnSymbolClickListener()
        setOnMapClickListener()
    }

    private fun setOnMapClickListener() {
        naverMap.setOnMapClickListener { pointF, latLng ->
            symbolMarker?.infoWindow?.close()
            symbolMarker?.map = null
            symbolMarker = null
        }
    }

    private fun setOnSymbolClickListener() {
        naverMap.setOnSymbolClickListener { symbol ->
            Log.d("symbol", symbol.toString())
            symbolMarker?.let {
                it.infoWindow?.close()
                it.map = null
            }
            symbolMarker = null
            if (symbol.caption.isNotEmpty()) {
                symbolMarker = Marker()
                symbolMarker?.let {
                    it.position = symbol.position
                    it.map = naverMap
                    val infoWindow = InfoWindow()
                    infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
                        override fun getText(infoWindow: InfoWindow): CharSequence {
                            return symbol.caption
                        }
                    }
                    infoWindow.open(it)
                }
            }
            true
        }
    }

    private fun configMap() {
        naverMap.minZoom = 15.0
        naverMap.maxZoom = 17.0
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.uiSettings.isTiltGesturesEnabled = false
        naverMap.extent = LatLngBounds(LatLng(31.43, 122.37), LatLng(44.35, 132.0))
    }
}
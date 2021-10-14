package com.mylittleproject.gyeonggimoneymap.ui

import android.content.Intent
import android.graphics.PointF
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mylittleproject.gyeonggimoneymap.R
import com.mylittleproject.gyeonggimoneymap.data.InfoWindowData
import com.mylittleproject.gyeonggimoneymap.data.StoreCategory
import com.mylittleproject.gyeonggimoneymap.databinding.FragmentMapBinding
import com.mylittleproject.gyeonggimoneymap.databinding.InfoWindowViewBinding
import com.mylittleproject.gyeonggimoneymap.network.CategorySearchHelper
import com.mylittleproject.gyeonggimoneymap.presenter.MainMapContract
import com.mylittleproject.gyeonggimoneymap.presenter.MainMapPresenter
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource

class MainMapFragment : Fragment(), OnMapReadyCallback, MainMapContract.MainMapView {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mainMapPresenter: MainMapContract.MainMapPresenter
    private lateinit var catetoryRecyclerView: RecyclerView
    private val adapter =
        CategoryListAdapter { code, adapterPosition -> onItemClick(code, adapterPosition) }
    private var isCategoryClickEnabled = true

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainMapPresenter = MainMapPresenter(this, CategorySearchHelper(), lifecycle)
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)
        catetoryRecyclerView = binding.rvCategory
        catetoryRecyclerView.adapter = adapter
        adapter.submitList(StoreCategory.toList())
        lifecycleScope
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
        setOnMapClickListener()
    }

    private fun setOnMapClickListener() {
        naverMap.setOnMapClickListener { pointF, latLng ->
            mainMapPresenter.onMapClick(pointF, latLng)
        }
    }

    override fun deleteMarker(marker: Marker) {
        marker.infoWindow?.close()
        marker.map = null
    }

    override fun displayMarker(marker: Marker) {
        marker.map = naverMap
    }

    override fun attachInfoWindow(
        infoWindow: InfoWindow,
        marker: Marker,
        infoWindowData: InfoWindowData
    ) {
        val binding: InfoWindowViewBinding = InfoWindowViewBinding.inflate(layoutInflater)
        infoWindow.adapter = object : InfoWindow.DefaultViewAdapter(requireContext()) {
            override fun getContentView(infoWindow: InfoWindow): View {
                if (infoWindowData.roadAddress.isNotEmpty()) {
                    binding.tvAddress.text = infoWindowData.roadAddress
                } else {
                    binding.tvAddress.text = infoWindowData.lotNameAddress
                }
                binding.tvTitle.text = infoWindowData.name
                if (infoWindowData.phone.isNotEmpty()) {
                    binding.tvPhone.text = infoWindowData.phone
                } else {
                    binding.tvPhone.isVisible = false
                }
                return binding.root
            }
        }
        infoWindow.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(infoWindowData.url))
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("error", "No kakakoMap")
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(KAKAO_MAP_PLAYSTORE))
                startActivity(intent)
            }
            true
        }
        infoWindow.open(marker)
    }

    override fun showProgressIndicator() {
        binding.piCircular.visibility = View.VISIBLE
    }

    override fun hideProgressIndicator() {
        binding.piCircular.visibility = View.GONE
    }

    override fun dimMap() {
        binding.vDim.visibility = View.VISIBLE
    }

    override fun unDimMap() {
        binding.vDim.visibility = View.GONE
    }

    override fun enableCategoryClick() {
        isCategoryClickEnabled = true
    }

    override fun disableCategoryClick() {
        isCategoryClickEnabled = false
    }

    override fun showSnackBar(listCount: Int) {
        Snackbar.make(binding.root, "${listCount}곳 발견", Snackbar.LENGTH_SHORT).show()
    }

    private fun configMap() {
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.extent = LatLngBounds(LatLng(31.43, 122.37), LatLng(44.35, 132.0))
    }

    private fun onItemClick(code: String, adapterPosition: Int) {
        if (isCategoryClickEnabled) {
            Log.d("code", code)
            val prevPosition = adapter.selectedPosition
            adapter.selectedPosition = adapterPosition
            adapter.notifyItemChanged(prevPosition)
            adapter.notifyItemChanged(adapter.selectedPosition)
            mainMapPresenter.searchByCategory(
                code, naverMap.cameraPosition.target, naverMap.projection.fromScreenLocation(
                    PointF(0F, 0F)
                )
            )
        }
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        const val KAKAO_MAP_PLAYSTORE = "market://details?id=net.daum.android.map"
    }
}
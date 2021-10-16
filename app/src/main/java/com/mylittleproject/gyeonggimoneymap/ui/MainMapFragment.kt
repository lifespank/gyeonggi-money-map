package com.mylittleproject.gyeonggimoneymap.ui

import android.content.Context
import android.content.Intent
import android.graphics.PointF
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.mylittleproject.gyeonggimoneymap.R
import com.mylittleproject.gyeonggimoneymap.data.InfoWindowData
import com.mylittleproject.gyeonggimoneymap.data.SiGun
import com.mylittleproject.gyeonggimoneymap.data.StoreCategory
import com.mylittleproject.gyeonggimoneymap.databinding.FragmentMapBinding
import com.mylittleproject.gyeonggimoneymap.network.CategorySearchHelper
import com.mylittleproject.gyeonggimoneymap.presenter.MainMapContract
import com.mylittleproject.gyeonggimoneymap.presenter.MainMapPresenter
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource

class MainMapFragment : Fragment(), OnMapReadyCallback, MainMapContract.MainMapView,
    AdapterView.OnItemSelectedListener {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mainMapPresenter: MainMapContract.MainMapPresenter
    private lateinit var catetoryRecyclerView: RecyclerView
    private val categoryListAdapter =
        CategoryListAdapter { code, adapterPosition -> onItemClick(code, adapterPosition) }
    private val infoListAdapter = InfoListAdapter { link -> onLinkClick(link) }
    private lateinit var infoViewPager: ViewPager2
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
        catetoryRecyclerView.adapter = categoryListAdapter
        categoryListAdapter.submitList(StoreCategory.toList())
        setViewPager()
        setSpinner()
    }

    private fun setSpinner() {
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            SiGun.toNameList()
        )
        binding.spSiGun.adapter = arrayAdapter
        binding.spSiGun.onItemSelectedListener = this
    }

    private fun setViewPager() {
        infoViewPager = binding.vpInfo
        infoViewPager.adapter = infoListAdapter
        infoViewPager.offscreenPageLimit = 1

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
        }
        infoViewPager.setPageTransformer(pageTransformer)

        val itemDecoration = HorizontalMarginItemDecoration(
            requireContext(),
            R.dimen.viewpager_current_item_horizontal_margin
        )
        infoViewPager.addItemDecoration(itemDecoration)
        infoViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                mainMapPresenter.selectMarker(position)
            }
        })
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

    override fun showLoading() {
        binding.llLoading.isVisible = true
    }

    override fun hideLoading() {
        binding.llLoading.isVisible = false
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

    override fun showNetworkError() {
        Toast.makeText(context, "네트워크 에러", Toast.LENGTH_SHORT).show()
    }

    override fun showInfo(list: List<InfoWindowData>) {
        infoListAdapter.submitList(list)
    }

    override fun moveCamera(latLng: LatLng) {
        if (this::naverMap.isInitialized) {
            val currentCameraLatLng = naverMap.cameraPosition.target
            val cameraUpdate = if (currentCameraLatLng.distanceTo(latLng) < 2000F) {
                CameraUpdate.scrollTo(latLng)
                    .animate(CameraAnimation.Easing)
            } else {
                CameraUpdate.scrollTo(latLng)
                    .animate(CameraAnimation.Fly, 2000)
            }
            naverMap.moveCamera(cameraUpdate)
        }
    }

    override fun selectViewPager(position: Int) {
        infoViewPager.currentItem = position
    }

    private fun configMap() {
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.extent = LatLngBounds(LatLng(31.43, 122.37), LatLng(44.35, 132.0))
    }

    private fun onItemClick(code: String, adapterPosition: Int) {
        if (isCategoryClickEnabled) {
            Log.d("code", code)
            val prevPosition = categoryListAdapter.selectedPosition
            categoryListAdapter.selectedPosition = adapterPosition
            categoryListAdapter.notifyItemChanged(prevPosition)
            categoryListAdapter.notifyItemChanged(categoryListAdapter.selectedPosition)
            mainMapPresenter.searchByCategory(
                code, naverMap.cameraPosition.target, naverMap.projection.fromScreenLocation(
                    PointF(0F, 0F)
                )
            )
        }
    }

    private fun onLinkClick(link: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("error", "No kakakoMap")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(KAKAO_MAP_PLAYSTORE))
            startActivity(intent)
        }
    }

    class HorizontalMarginItemDecoration(context: Context, @DimenRes horizontalMarginInDp: Int) :
        RecyclerView.ItemDecoration() {

        private val horizontalMarginInPx: Int =
            context.resources.getDimension(horizontalMarginInDp).toInt()

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
        ) {
            outRect.right = horizontalMarginInPx
            outRect.left = horizontalMarginInPx
        }
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        const val KAKAO_MAP_PLAYSTORE = "market://details?id=net.daum.android.map"
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            val siGun = parent.getItemAtPosition(position)
            mainMapPresenter.setSiGun(siGun as String)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //nothing
    }
}
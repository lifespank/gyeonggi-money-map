package com.mylittleproject.gyeonggimoneymap.presenter

import android.graphics.PointF
import com.mylittleproject.gyeonggimoneymap.data.InfoWindowData
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker

interface MainMapContract {
    interface MainMapView {
        fun deleteMarker(marker: Marker)
        fun displayMarker(marker: Marker)
        fun showLoading()
        fun hideLoading()
        fun enableCategoryClick()
        fun disableCategoryClick()
        fun showSnackBar(listCount: Int)
        fun showNetworkError()
        fun showInfo(list: List<InfoWindowData>)
        fun moveCamera(latLng: LatLng)
        fun selectViewPager(position: Int)
    }

    interface MainMapPresenter {
        fun onMapClick(pointf: PointF, latLng: LatLng)
        fun searchByCategory(code: String, cameraCoord: LatLng, leftUpperCoord: LatLng)
        fun selectMarker(position: Int)
    }
}
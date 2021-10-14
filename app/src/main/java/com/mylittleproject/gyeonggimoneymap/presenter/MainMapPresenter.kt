package com.mylittleproject.gyeonggimoneymap.presenter

import android.graphics.PointF
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import com.mylittleproject.gyeonggimoneymap.data.InfoWindowData
import com.mylittleproject.gyeonggimoneymap.data.MarkerInfo
import com.mylittleproject.gyeonggimoneymap.network.CategorySearchHelper
import com.mylittleproject.gyeonggimoneymap.network.Document
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.roundToInt

class MainMapPresenter(
    private val mainMapView: MainMapContract.MainMapView,
    private val categorySearchHelper: CategorySearchHelper,
    private val lifecycle: Lifecycle
) :
    MainMapContract.MainMapPresenter {
    private val markerInfoList = mutableListOf<MarkerInfo>()
    private val infoWindow = InfoWindow()

    override fun onMapClick(pointf: PointF, latLng: LatLng) {
        infoWindow.close()
    }

    override fun searchByCategory(code: String, cameraCoord: LatLng, leftUpperCoord: LatLng) {
        val x = cameraCoord.longitude.toString()
        val y = cameraCoord.latitude.toString()
        val radius = min(cameraCoord.distanceTo(leftUpperCoord).roundToInt(), 500)
        var searchList: List<Document>?
        mainMapView.disableCategoryClick()
        mainMapView.dimMap()
        mainMapView.showProgressIndicator()
        lifecycle.coroutineScope.launch {
            searchList = categorySearchHelper.searchByCategory(code, x, y, radius)
            Log.d("searchList", searchList.toString())
            Log.d("count", searchList?.size.toString())
            clearMarkers()
            searchList?.let {
                markerInfoList.addAll(it.map { document ->
                    val marker = Marker(LatLng(document.y.toDouble(), document.x.toDouble()))
                    marker.isIconPerspectiveEnabled = true
                    marker.captionText = document.placeName
                    marker.isHideCollidedSymbols = true
                    val infoWindowData = InfoWindowData(
                        document.placeName,
                        document.phone,
                        document.roadAddressName,
                        document.addressName,
                        KAKAO_SCHEME + document.placeUrl.substringAfterLast("/")
                    )
                    mainMapView.displayMarker(marker)
                    marker.setOnClickListener { overlay ->
                        onMarkerClick(
                            overlay as Marker,
                            infoWindowData
                        )
                        true
                    }
                    MarkerInfo(marker, infoWindowData)
                })
            }
            mainMapView.hideProgressIndicator()
            mainMapView.unDimMap()
            mainMapView.enableCategoryClick()
        }
    }

    private fun onMarkerClick(marker: Marker, infoWindowData: InfoWindowData) {
        markerInfoList.forEach { otherMarkerInfo ->
            otherMarkerInfo.marker.zIndex = 0
        }
        marker.zIndex = 1
        if (marker.infoWindow == null) {
            mainMapView.attachInfoWindow(infoWindow, marker, infoWindowData)
        } else {
            infoWindow.close()
        }
    }

    private fun clearMarkers() {
        markerInfoList.forEach { mainMapView.deleteMarker(it.marker) }
        markerInfoList.clear()
    }

    companion object {
        const val KAKAO_SCHEME = "kakaomap://place?id="
    }
}
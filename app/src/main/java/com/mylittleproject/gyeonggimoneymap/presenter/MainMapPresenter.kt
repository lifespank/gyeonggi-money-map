package com.mylittleproject.gyeonggimoneymap.presenter

import android.graphics.PointF
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import com.mylittleproject.gyeonggimoneymap.network.CategorySearchHelper
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.Symbol
import com.naver.maps.map.overlay.Marker
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainMapPresenter(
    private val mainMapView: MainMapContract.MainMapView,
    private val categorySearchHelper: CategorySearchHelper,
    private val lifecycle: Lifecycle
) :
    MainMapContract.MainMapPresenter {
    private var symbolMarker: Marker? = null
    override fun onMapClick(pointf: PointF, latLng: LatLng) {
        symbolMarker?.let { mainMapView.deleteMarker(it) }
    }

    override fun onSymbolClick(symbol: Symbol) {
        symbolMarker?.let { mainMapView.deleteMarker(it) }
        if (symbol.caption.isNotEmpty()) {
            symbolMarker = Marker()
            symbolMarker?.let {
                it.position = symbol.position
                mainMapView.displayMarker(it)
                mainMapView.attachInfoWindow(it, symbol.caption)
            }
        }
    }

    override fun searchByCategory(code: String, cameraCoord: LatLng, leftUpperCoord: LatLng) {
        val x = cameraCoord.longitude.toString()
        val y = cameraCoord.latitude.toString()
        val radius = cameraCoord.distanceTo(leftUpperCoord).roundToInt()
        lifecycle.coroutineScope.launch {
            val searchList = categorySearchHelper.searchByCategory(code, x, y, radius)
            Log.d("searchList", searchList.toString())
            Log.d("count", searchList?.size.toString())
        }
    }

}
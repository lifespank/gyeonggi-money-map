package com.mylittleproject.gyeonggimoneymap.presenter

import android.graphics.PointF
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.Symbol
import com.naver.maps.map.overlay.Marker

class MainMapPresenter(private val mainMapView: MainMapContract.MainMapView) :
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

}
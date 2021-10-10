package com.mylittleproject.gyeonggimoneymap.presenter

import android.graphics.PointF
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.Symbol
import com.naver.maps.map.overlay.Marker

interface MainMapContract {
    interface MainMapView {
        fun deleteMarker(marker: Marker)
        fun displayMarker(marker: Marker)
        fun attachInfoWindow(marker: Marker, caption: String)
    }
    interface  MainMapPresenter {
        fun onMapClick(pointf: PointF, latLng: LatLng)
        fun onSymbolClick(symbol: Symbol)
        fun searchByCategory(code: String, cameraCoord: LatLng, leftUpperCoord: LatLng)
    }
}
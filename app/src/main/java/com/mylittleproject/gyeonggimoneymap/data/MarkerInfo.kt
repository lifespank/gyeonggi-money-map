package com.mylittleproject.gyeonggimoneymap.data

import com.naver.maps.map.overlay.Marker

class MarkerInfo(val marker: Marker, val infoWindowData: InfoWindowData)

data class InfoWindowData(
    val position: Int,
    val name: String,
    val phone: String,
    val roadAddress: String,
    val lotNameAddress: String,
    val url: String
)
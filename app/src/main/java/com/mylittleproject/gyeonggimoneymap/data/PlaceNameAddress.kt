package com.mylittleproject.gyeonggimoneymap.data

import android.util.Log

class PlaceNameAddress(beforeName: String, roadAddress: String) {
    val name = refineName(beforeName)
    val siGun = roadAddress.substringAfter("경기 ").substringBefore(" ")
    val lastPartOfAddress = getRoadAddressAndBuildingNumber(roadAddress)

    private fun getRoadAddressAndBuildingNumber(roadAddress: String): String {
        return try {
            val roadAddressSplit = roadAddress.split(" ")
            roadAddressSplit[roadAddressSplit.lastIndex - 1] + " " + roadAddressSplit[roadAddressSplit.lastIndex]
        } catch (e: Exception) {
            Log.e("parse error", "${e.message.toString()} $roadAddress")
            ""
        }
    }

    private fun refineName(beforeName: String): String {
        return beforeName.replace("CU ", "씨유")
    }

    override fun toString(): String {
        return "PlaceNameAddress(name=$name, siGun=$siGun, lastPartOfAddress=$lastPartOfAddress)"
    }
}
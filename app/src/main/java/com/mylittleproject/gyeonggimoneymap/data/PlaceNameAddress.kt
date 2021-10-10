package com.mylittleproject.gyeonggimoneymap.data

class PlaceNameAddress(beforeName: String, roadAddress: String) {
    val name = refineName(beforeName)
    val siGun = roadAddress.substringAfter("경기 ").substringBefore(" ")
    val lastPartOfAddress = getRoadAddressAndBuildingNumber(roadAddress)

    private fun getRoadAddressAndBuildingNumber(roadAddress: String): String {
        val roadAddressSplit = roadAddress.split(" ")
        return roadAddressSplit[roadAddressSplit.lastIndex - 1] + " " + roadAddressSplit[roadAddressSplit.lastIndex]
    }

    private fun refineName(beforeName: String): String {
        return beforeName.replace("CU ", "씨유")
    }

    override fun toString(): String {
        return "PlaceNameAddress(name=$name, siGun=$siGun, lastPartOfAddress=$lastPartOfAddress)"
    }
}
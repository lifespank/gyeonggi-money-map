package com.mylittleproject.gyeonggimoneymap.data

import com.mylittleproject.gyeonggimoneymap.R

enum class StoreCategory(
    val code: String,
    val korean: String,
    val drawable: Int,
) {
    MART("MT1", "마트", R.drawable.ic_baseline_local_grocery_store_24),
    CONVENIENCE_STORE("CS2", "편의점", R.drawable.ic_baseline_local_convenience_store_24),
    RESTAURANT("FD6", "음식점", R.drawable.ic_baseline_restaurant_24),
    CAFE("CE7", "카페", R.drawable.ic_baseline_local_cafe_24),
    PARKING_LOT("PK6", "주차장", R.drawable.ic_baseline_local_parking_24),
    GAS_STATION("OL7", "주유소", R.drawable.ic_baseline_local_gas_station_24),
    ACCOMMODATION("AD5", "숙박", R.drawable.ic_baseline_bed_24),
    HOSPITAL("HP8", "병원", R.drawable.ic_baseline_local_hospital_24),
    PHARMACY("PM9", "약국", R.drawable.ic_baseline_local_pharmacy_24),
    TOURIST_ATTRACTIONS("AT4", "관광명소", R.drawable.ic_baseline_tour_24),
    PRESCHOOL("PS3", "어린이집/유치원", R.drawable.ic_baseline_child_care_24),
    CULTURAL_FACILITY("CT1", "문화시설", R.drawable.ic_baseline_theaters_24),
    ACADEMY("AC5", "학원", R.drawable.ic_baseline_school_24),
    REAL_ESTATE("AG2", "중개업소", R.drawable.ic_baseline_house_24),
}
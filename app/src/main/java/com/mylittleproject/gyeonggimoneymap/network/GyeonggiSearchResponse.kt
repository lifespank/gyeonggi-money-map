package com.mylittleproject.gyeonggimoneymap.network

import com.google.gson.annotations.SerializedName

data class GyeonggiSearchResponse(
    @SerializedName("RegionMnyFacltStus")
    val regionMnyFacltStus: List<RegionMnyFacltStu> = listOf()
)

data class RegionMnyFacltStu(
    @SerializedName("head")
    val head: List<Head> = listOf(),
    @SerializedName("row")
    val row: List<Row> = listOf()
)

data class Head(
    @SerializedName("list_total_count")
    val listTotalCount: Int = 0,
    @SerializedName("RESULT")
    val result: RESULT = RESULT(),
    @SerializedName("api_version")
    val apiVersion: String = ""
)

data class Row(
    @SerializedName("CMPNM_NM")
    val companyName: String = "",
    @SerializedName("INDUTYPE_NM")
    val companyType: String = "",
    @SerializedName("DATA_STD_DE")
    val dataDate: String = "",
    @SerializedName("REFINE_LOTNO_ADDR")
    val lotNumberAddress: String = "",
    @SerializedName("REFINE_ROADNM_ADDR")
    val roadNumberAddress: String = "",
    @SerializedName("REFINE_ZIP_CD")
    val zipCode: String? = null,
    @SerializedName("REFINE_WGS84_LOGT")
    val longitude: String? = null,
    @SerializedName("REFINE_WGS84_LAT")
    val latitude: String? = null,
    @SerializedName("SIGUN_CD")
    val siGunCode: String = "",
    @SerializedName("SIGUN_NM")
    val siGunName: String = ""
)

data class RESULT(
    @SerializedName("CODE")
    val code: String = "",
    @SerializedName("MESSAGE")
    val message: String = ""
)
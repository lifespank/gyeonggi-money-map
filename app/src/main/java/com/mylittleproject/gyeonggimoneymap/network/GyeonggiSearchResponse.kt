package com.mylittleproject.gyeonggimoneymap.network

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class GyeonggiSearchResponse(
    @Json(name = "RegionMnyFacltStus")
    val regionMnyFacltStus: List<RegionMnyFacltStu> = listOf()
)

@JsonClass(generateAdapter = true)
data class RegionMnyFacltStu(
    @Json(name = "head")
    val head: List<Head> = listOf(),
    @Json(name = "row")
    val row: List<Row> = listOf()
)

@JsonClass(generateAdapter = true)
data class Head(
    @Json(name = "list_total_count")
    val listTotalCount: Int = 0,
    @Json(name = "RESULT")
    val result: RESULT = RESULT(),
    @Json(name = "api_version")
    val apiVersion: String = ""
)

@JsonClass(generateAdapter = true)
data class Row(
    @Json(name = "CMPNM_NM")
    val companyName: String = "",
    @Json(name = "INDUTYPE_NM")
    val companyType: String = "",
    @Json(name = "DATA_STD_DE")
    val dataDate: String = "",
    @Json(name = "REFINE_LOTNO_ADDR")
    val lotNumberAddress: String = "",
    @Json(name = "REFINE_ROADNM_ADDR")
    val roadNumberAddress: String = "",
    @Json(name = "REFINE_ZIP_CD")
    val zipCode: String? = null,
    @Json(name = "REFINE_WGS84_LOGT")
    val longitude: String? = null,
    @Json(name = "REFINE_WGS84_LAT")
    val latitude: String? = null,
    @Json(name = "SIGUN_CD")
    val siGunCode: String = "",
    @Json(name = "SIGUN_NM")
    val siGunName: String = ""
)

@JsonClass(generateAdapter = true)
data class RESULT(
    @Json(name = "CODE")
    val code: String = "",
    @Json(name = "MESSAGE")
    val message: String = ""
)
package com.mylittleproject.gyeonggimoneymap.network

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class KakaoSearchResponse(
    @Json(name = "documents")
    val documents: List<Document> = listOf(),
    @Json(name = "meta")
    val meta: Meta = Meta()
)

@JsonClass(generateAdapter = true)
data class Document(
    @Json(name = "address_name")
    val addressName: String = "",
    @Json(name = "category_group_code")
    val categoryGroupCode: String = "",
    @Json(name = "category_group_name")
    val categoryGroupName: String = "",
    @Json(name = "category_name")
    val categoryName: String = "",
    @Json(name = "distance")
    val distance: String = "",
    @Json(name = "id")
    val id: String = "",
    @Json(name = "phone")
    val phone: String = "",
    @Json(name = "place_name")
    val placeName: String = "",
    @Json(name = "place_url")
    val placeUrl: String = "",
    @Json(name = "road_address_name")
    val roadAddressName: String = "",
    @Json(name = "x")
    val x: String = "",
    @Json(name = "y")
    val y: String = ""
) {
    override fun toString(): String {
        return listOf(placeName, phone, roadAddressName).joinToString("\n")
    }
}

@JsonClass(generateAdapter = true)
data class Meta(
    @Json(name = "is_end")
    val isEnd: Boolean = false,
    @Json(name = "pageable_count")
    val pageableCount: Int = 0,
    @Json(name = "same_name")
    val sameName: Any? = null,
    @Json(name = "total_count")
    val totalCount: Int = 0
)
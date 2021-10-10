package com.mylittleproject.gyeonggimoneymap.network

import com.google.gson.annotations.SerializedName

data class KakaoSearchResponse(
    @SerializedName("documents")
    val documents: List<Document> = listOf(),
    @SerializedName("meta")
    val meta: Meta = Meta()
)

data class Document(
    @SerializedName("address_name")
    val addressName: String = "",
    @SerializedName("category_group_code")
    val categoryGroupCode: String = "",
    @SerializedName("category_group_name")
    val categoryGroupName: String = "",
    @SerializedName("category_name")
    val categoryName: String = "",
    @SerializedName("distance")
    val distance: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("place_name")
    val placeName: String = "",
    @SerializedName("place_url")
    val placeUrl: String = "",
    @SerializedName("road_address_name")
    val roadAddressName: String = "",
    @SerializedName("x")
    val x: String = "",
    @SerializedName("y")
    val y: String = ""
) {
    override fun toString(): String {
        return listOf(placeName, phone, roadAddressName).joinToString("\n")
    }
}

data class Meta(
    @SerializedName("is_end")
    val isEnd: Boolean = false,
    @SerializedName("pageable_count")
    val pageableCount: Int = 0,
    @SerializedName("same_name")
    val sameName: Any? = null,
    @SerializedName("total_count")
    val totalCount: Int = 0
)
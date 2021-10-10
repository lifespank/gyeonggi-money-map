package com.mylittleproject.gyeonggimoneymap.network

import com.mylittleproject.gyeonggimoneymap.common.KAKAO_REST_KEY
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface APIService {
    @Headers("Authorization: KakaoAK $KAKAO_REST_KEY")
    @GET("/v2/local/search/category.json")
    fun searchByCategory(
        @Query("category_group_code") categoryGroupCode: String,
        x: String,
        y: String,
        radius: Int = 200,
        page: Int,
        sort: String = "distance"
    )
}
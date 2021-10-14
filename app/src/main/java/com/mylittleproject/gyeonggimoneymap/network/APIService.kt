package com.mylittleproject.gyeonggimoneymap.network

import com.mylittleproject.gyeonggimoneymap.common.KAKAO_REST_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface APIService {
    @Headers("Authorization: KakaoAK $KAKAO_REST_KEY")
    @GET("v2/local/search/category.json")
    suspend fun searchByCategory(
        @Query("category_group_code") categoryGroupCode: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("radius") radius: Int,
        @Query("page") page: Int,
        @Query("sort") sort: String
    ): Response<KakaoSearchResponse>

    @GET("RegionMnyFacltStus")
    suspend fun searchGyeonggiMoneyPlace(
        @Query("KEY") key: String,
        @Query("Type") type: String,
        @Query("pIndex") pIndex: String,
        @Query("pSize") pSize: String,
        @Query("CMPNM_NM") companyName: String,
        @Query("SIGUN_NM") siGunName: String,
        @Query("REFINE_LOTNO_ADDR") lastPartOfAddress: String
    ): Response<GyeonggiSearchResponse>
}
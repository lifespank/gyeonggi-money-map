package com.mylittleproject.gyeonggimoneymap.network

import com.mylittleproject.gyeonggimoneymap.common.KAKAO_CATEGORY_SEARCH_URL
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object KakaoSearchHelper {
    private val retrofit = Retrofit
        .Builder()
        .baseUrl(KAKAO_CATEGORY_SEARCH_URL)
        .addConverterFactory(MoshiConverterFactory.create()).build()
    private val apiService = retrofit.create(APIService::class.java)
}
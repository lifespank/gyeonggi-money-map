package com.mylittleproject.gyeonggimoneymap.network

import android.util.Log
import com.mylittleproject.gyeonggimoneymap.common.KAKAO_CATEGORY_SEARCH_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CategorySearchHelper {
    private val retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(KAKAO_CATEGORY_SEARCH_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
    private val apiService by lazy { retrofit.create(APIService::class.java) }

    suspend fun searchByCategory(
        categoryGroupCode: String,
        x: String,
        y: String,
        radius: Int,
        page: Int = 1,
        sort: String = "distance"
    ): List<Document>? {
        var documentList: MutableList<Document>? = null
        val searchResponse =
            apiService.searchByCategory(categoryGroupCode, x, y, radius, page, sort)
        if (searchResponse.isSuccessful) {
            val data = searchResponse.body()
            if (data != null) {
                documentList = data.documents.toMutableList()
                if (!data.meta.isEnd) {
                    val secondResponse =
                        apiService.searchByCategory(categoryGroupCode, x, y, radius, page + 1, sort)
                    val secondData = secondResponse.body()
                    if (secondData != null) {
                        documentList.addAll(secondData.documents)
                    } else {
                        Log.e("kakao error", "Second Response error")
                    }
                }
            } else {
                Log.e("kakao error", "First Response Error")
            }
        } else {
            Log.e("kakao error", "First Response Error")
        }
        return documentList
    }
}
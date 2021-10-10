package com.mylittleproject.gyeonggimoneymap.network

import android.util.Log
import com.mylittleproject.gyeonggimoneymap.common.GYEONGGI_KEY
import com.mylittleproject.gyeonggimoneymap.common.GYEONGGI_RESPONSE_CODE_VALID
import com.mylittleproject.gyeonggimoneymap.common.GYEONGGI_URL
import com.mylittleproject.gyeonggimoneymap.common.KAKAO_CATEGORY_SEARCH_URL
import com.mylittleproject.gyeonggimoneymap.data.PlaceNameAddress
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CategorySearchHelper {
    private val retrofitKakao by lazy {
        Retrofit
            .Builder()
            .baseUrl(KAKAO_CATEGORY_SEARCH_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
    private val apiServiceKakao by lazy { retrofitKakao.create(APIService::class.java) }
    private val retrofitGyeonggi by lazy {
        Retrofit
            .Builder()
            .baseUrl(GYEONGGI_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
    private val apiServiceGyeonggi by lazy { retrofitGyeonggi.create(APIService::class.java) }

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
            apiServiceKakao.searchByCategory(categoryGroupCode, x, y, radius, page, sort)
        if (searchResponse.isSuccessful) {
            val data = searchResponse.body()
            if (data != null) {
                documentList = data.documents.toMutableList()
                if (!data.meta.isEnd) {
                    val secondResponse =
                        apiServiceKakao.searchByCategory(
                            categoryGroupCode,
                            x,
                            y,
                            radius,
                            page + 1,
                            sort
                        )
                    if (secondResponse.isSuccessful) {
                        val secondData = secondResponse.body()
                        if (secondData != null) {
                            documentList.addAll(secondData.documents)
                        } else {
                            Log.e("kakao error", "Second Response error")
                        }
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

        val filteredDocumentList =
            documentList?.filter {
                var isGood = false
                val placeNameAddress = PlaceNameAddress(it.placeName, it.roadAddressName)
                val gyeonggiResponse =
                    apiServiceGyeonggi.searchGyeonggiMoneyPlace(
                        GYEONGGI_KEY,
                        "json",
                        "1",
                        "1",
                        placeNameAddress.name,
                        placeNameAddress.siGun,
                        placeNameAddress.lastPartOfAddress
                    )
                if (gyeonggiResponse.isSuccessful) {
                    val data = gyeonggiResponse.body()
                    if (data != null
                        && data.regionMnyFacltStus.isNotEmpty()
                        && data.regionMnyFacltStus[0].head[1].result.code == GYEONGGI_RESPONSE_CODE_VALID
                    ) {
                        Log.d("gyeonggiResponse", gyeonggiResponse.toString())
                        isGood = true
                    }
                }
                isGood
            }
        return filteredDocumentList
    }
}
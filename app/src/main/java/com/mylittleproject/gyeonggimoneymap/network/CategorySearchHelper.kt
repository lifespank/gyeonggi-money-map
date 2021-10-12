package com.mylittleproject.gyeonggimoneymap.network

import android.util.Log
import com.mylittleproject.gyeonggimoneymap.common.GYEONGGI_KEY
import com.mylittleproject.gyeonggimoneymap.common.GYEONGGI_RESPONSE_CODE_VALID
import com.mylittleproject.gyeonggimoneymap.common.GYEONGGI_URL
import com.mylittleproject.gyeonggimoneymap.common.KAKAO_CATEGORY_SEARCH_URL
import com.mylittleproject.gyeonggimoneymap.data.PlaceNameAddress
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object CategorySearchHelper {
    private val retrofitKakao by lazy {
        Retrofit
            .Builder()
            .baseUrl(KAKAO_CATEGORY_SEARCH_URL)
            .addConverterFactory(MoshiConverterFactory.create()).build()
    }
    private val apiServiceKakao by lazy { retrofitKakao.create(APIService::class.java) }
    private val retrofitGyeonggi by lazy {
        Retrofit
            .Builder()
            .baseUrl(GYEONGGI_URL)
            .addConverterFactory(MoshiConverterFactory.create()).build()
    }
    private val apiServiceGyeonggi by lazy { retrofitGyeonggi.create(APIService::class.java) }

    suspend fun searchByCategory(
        categoryGroupCode: String,
        x: String,
        y: String,
        radius: Int,
        page: Int = 1,
        sort: String = "distance"
    ): List<Document> {
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

        Log.d("documents", documentList.toString())
        if (!documentList.isNullOrEmpty()) {
            val filteredDocumentList = mutableListOf<Document>()
            coroutineScope {
                documentList.map {
                    async {
                        if (it.roadAddressName.isNotEmpty() && it.roadAddressName.substringBefore(" ") == "경기") {
                            val placeNameAddress =
                                PlaceNameAddress(it.placeName, it.roadAddressName)
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
                                    val mutex = Mutex()
                                    mutex.withLock {
                                        filteredDocumentList.add(it)
                                    }
                                    Log.d("gyeonggiResponse", gyeonggiResponse.toString())
                                }
                            }
                        }
                    }
                }.awaitAll()
            }
            return filteredDocumentList
        }
        return emptyList()
    }
}
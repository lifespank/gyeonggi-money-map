package com.mylittleproject.gyeonggimoneymap.network

import android.util.Log
import com.mylittleproject.gyeonggimoneymap.common.GYEONGGI_KEY
import com.mylittleproject.gyeonggimoneymap.data.PlaceNameAddress
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CategorySearchHelper {

    private val apiServiceKakao = getService(KAKAO_CATEGORY_SEARCH_URL)
    private val apiServiceGyeonggi = getService(GYEONGGI_URL)

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
            val mutex = Mutex()
            coroutineScope {
                documentList.map {
                    async {
                        if (it.addressName.isNotEmpty() && it.addressName.substringBefore(" ") == "경기") {
                            val placeNameAddress =
                                PlaceNameAddress(it.placeName, it.addressName)
                            Log.d("place", placeNameAddress.toString())
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

    private fun getService(baseURL: String): APIService {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(baseURL)
            .addConverterFactory(MoshiConverterFactory.create()).build()
        return retrofit.create(APIService::class.java)
    }

    companion object {
        const val KAKAO_CATEGORY_SEARCH_URL = "https://dapi.kakao.com/"
        const val GYEONGGI_RESPONSE_CODE_VALID = "INFO-000"
        const val GYEONGGI_URL = "https://openapi.gg.go.kr/"
    }
}
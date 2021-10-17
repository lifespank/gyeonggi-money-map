package com.mylittleproject.gyeonggimoneymap.network

import android.util.Log
import com.mylittleproject.gyeonggimoneymap.common.GYEONGGI_KEY
import com.mylittleproject.gyeonggimoneymap.data.PlaceNameAddress
import com.mylittleproject.gyeonggimoneymap.util.refineCU
import com.mylittleproject.gyeonggimoneymap.util.refineGS25
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
    ): List<Document>? = coroutineScope {
        try {
            var pageNum = page
            val documentList: MutableList<Document> = emptyList<Document>().toMutableList()
            val searchResponse =
                apiServiceKakao.searchByCategory(categoryGroupCode, x, y, radius, pageNum, sort)
            var data = searchResponse.body()
            while (data != null && !data.meta.isEnd) {
                Log.d("data", data.toString())
                documentList.addAll(data.documents)
                data = apiServiceKakao.searchByCategory(
                    categoryGroupCode,
                    x,
                    y,
                    radius,
                    ++pageNum,
                    sort
                ).body()
            }
            if (data != null) {
                documentList.addAll(data.documents)
            }
            Log.d("documents", documentList.toString())
            if (!documentList.isNullOrEmpty()) {
                return@coroutineScope documentList.map {
                    async {
                        if (it.addressName.isNotEmpty() && it.addressName.substringBefore(" ") == "경기") {
                            val placeNameAddressList =
                                emptyList<PlaceNameAddress>().toMutableList()
                            if (it.placeName.contains("CU") || it.placeName.contains("씨유")) {
                                val refinedCUList = refineCU(it.placeName)
                                placeNameAddressList.addAll(refinedCUList.map { refinedCU ->
                                    PlaceNameAddress(refinedCU, it.addressName)
                                })
                            } else if (it.placeName.contains("GS25")) {
                                val refinedGS25List = refineGS25(it.placeName)
                                placeNameAddressList.addAll(refinedGS25List.map { refinedGS25 ->
                                    PlaceNameAddress(refinedGS25, it.addressName)
                                })
                            } else {
                                placeNameAddressList.add(
                                    PlaceNameAddress(
                                        it.placeName,
                                        it.addressName
                                    )
                                )
                            }
                            placeNameAddressList.forEach { placeNameAddress ->
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
                                    val gyeonggiData = gyeonggiResponse.body()
                                    if (gyeonggiData != null
                                        && gyeonggiData.regionMnyFacltStus.isNotEmpty()
                                        && gyeonggiData.regionMnyFacltStus[0].head[1].result.code == GYEONGGI_RESPONSE_CODE_VALID
                                    ) {
                                        Log.d(
                                            "gyeonggiResponse",
                                            gyeonggiResponse.toString()
                                        )
                                        return@async it
                                    }
                                }
                            }
                            return@async null
                        } else {
                            return@async null
                        }
                    }
                }.awaitAll().filterNotNull()
            }
            return@coroutineScope null
        } catch (e: Exception) {
            return@coroutineScope null
        }
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
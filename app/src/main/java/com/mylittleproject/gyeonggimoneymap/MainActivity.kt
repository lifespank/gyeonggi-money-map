package com.mylittleproject.gyeonggimoneymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mylittleproject.gyeonggimoneymap.common.NAVER_CLIENT_ID
import com.naver.maps.map.NaverMapSdk

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient(NAVER_CLIENT_ID)
        setContentView(R.layout.activity_main)
    }
}
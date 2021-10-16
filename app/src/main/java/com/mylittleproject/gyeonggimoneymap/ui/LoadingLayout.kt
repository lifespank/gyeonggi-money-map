package com.mylittleproject.gyeonggimoneymap.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.mylittleproject.gyeonggimoneymap.databinding.LayoutLoadingBinding

class LoadingLayout(context: Context?, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private var binding: LayoutLoadingBinding =
        LayoutLoadingBinding.inflate(LayoutInflater.from(context), this, true)
}
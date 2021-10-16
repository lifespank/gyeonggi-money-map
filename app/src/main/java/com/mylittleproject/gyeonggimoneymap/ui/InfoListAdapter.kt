package com.mylittleproject.gyeonggimoneymap.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mylittleproject.gyeonggimoneymap.data.InfoWindowData
import com.mylittleproject.gyeonggimoneymap.databinding.InfoWindowViewBinding

class InfoListAdapter :
    ListAdapter<InfoWindowData, InfoListAdapter.InfoWindowViewHolder>(object :
        DiffUtil.ItemCallback<InfoWindowData>() {

        override fun areItemsTheSame(oldItem: InfoWindowData, newItem: InfoWindowData): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: InfoWindowData, newItem: InfoWindowData): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoWindowViewHolder {
        return InfoWindowViewHolder(
            InfoWindowViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: InfoWindowViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class InfoWindowViewHolder(private val binding: InfoWindowViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(infoWindowData: InfoWindowData) {
            if (infoWindowData.roadAddress.isNotEmpty()) {
                binding.tvAddress.text = infoWindowData.roadAddress
            } else {
                binding.tvAddress.text = infoWindowData.lotNameAddress
            }
            binding.tvTitle.text = infoWindowData.name
            binding.tvPhone.text = infoWindowData.phone
        }
    }
}
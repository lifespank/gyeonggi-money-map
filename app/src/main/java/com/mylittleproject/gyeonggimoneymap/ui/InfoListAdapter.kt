package com.mylittleproject.gyeonggimoneymap.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mylittleproject.gyeonggimoneymap.R
import com.mylittleproject.gyeonggimoneymap.data.InfoWindowData
import com.mylittleproject.gyeonggimoneymap.databinding.InfoWindowViewBinding

class InfoListAdapter(private val onLinkClick: (String) -> Unit) :
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
                false,
            ),
            onLinkClick
        )
    }

    override fun onBindViewHolder(holder: InfoWindowViewHolder, position: Int) {
        holder.bind(getItem(position), currentList.size)
    }

    class InfoWindowViewHolder(
        private val binding: InfoWindowViewBinding,
        private val onLinkClick: (String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(infoWindowData: InfoWindowData, listSize: Int) {
            if (infoWindowData.roadAddress.isNotEmpty()) {
                binding.tvAddress.text = infoWindowData.roadAddress
            } else {
                binding.tvAddress.text = infoWindowData.lotNameAddress
            }
            binding.tvTitle.text = infoWindowData.name
            binding.tvPhone.text = infoWindowData.phone
            binding.tvSpecs.setOnClickListener {
                onLinkClick(infoWindowData.url)
            }
            binding.tvPosition.text = itemView.context.getString(
                R.string.view_pager_position,
                infoWindowData.position + 1,
                listSize
            )
        }
    }
}
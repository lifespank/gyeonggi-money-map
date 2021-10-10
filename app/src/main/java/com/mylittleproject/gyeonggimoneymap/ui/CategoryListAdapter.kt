package com.mylittleproject.gyeonggimoneymap.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mylittleproject.gyeonggimoneymap.R
import com.mylittleproject.gyeonggimoneymap.data.StoreCategory
import com.mylittleproject.gyeonggimoneymap.databinding.RecyclerViewItemCategoryBinding

class CategoryListAdapter(private val onItemClick: (String, Int) -> Unit) :
    ListAdapter<StoreCategory, CategoryListAdapter.CategoryViewHolder>(object :
        DiffUtil.ItemCallback<StoreCategory>() {
        override fun areItemsTheSame(oldItem: StoreCategory, newItem: StoreCategory): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: StoreCategory, newItem: StoreCategory): Boolean {
            return oldItem.code == newItem.code && oldItem.korean == newItem.korean
        }
    }) {
    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            RecyclerViewItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position), position == selectedPosition)
    }

    inner class CategoryViewHolder(private val binding: RecyclerViewItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var code: String

        init {
            itemView.setOnClickListener {
                onItemClick(code, adapterPosition)
            }
        }

        fun bind(storeCategory: StoreCategory, isSelected: Boolean) {
            this.code = storeCategory.code
            binding.tvCategoryKorean.text = storeCategory.korean
            binding.ivCategoryIcon.setImageResource(storeCategory.drawable)
            if (isSelected) {
                itemView.setBackgroundResource(R.drawable.light_green_grey_outline)
            } else {
                itemView.setBackgroundResource(R.drawable.white_grey_outline)
            }
        }
    }
}
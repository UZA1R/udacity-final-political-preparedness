package com.example.android.politicalpreparedness.ui_screens.common_view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.ItemHeaderBinding

const val ITEM_HEADER = 0

class HeaderViewHolder(private val binding: ItemHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup) = HeaderViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_header, parent,
                false
            )
        )
    }

    fun bind(title: String) {
        binding.header = title
    }
}
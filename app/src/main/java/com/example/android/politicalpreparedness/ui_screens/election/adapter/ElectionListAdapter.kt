package com.example.android.politicalpreparedness.ui_screens.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.data_source.network.models.Election
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding

class ElectionListAdapter(private val clickListener: OnClickListener) :
    ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    //TODO: Bind ViewHolder
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

//TODO: Create ElectionViewHolder
class ElectionViewHolder(private val binding: FragmentElectionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    //TODO: Add companion object to inflate ViewHolder (from)
    companion object {
        fun from(parent: ViewGroup) = ElectionViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                android.R.layout.simple_list_item_1, parent,
                false
            )
        )
    }

    fun bind(item: Election) {
//        binding.representative = item
//        binding.representativePhoto.setImageResource(R.drawable.ic_profile)

        binding.executePendingBindings()
    }
}

//TODO: Create ElectionDiffCallback
class ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }
}

//TODO: Create ElectionListener
class OnClickListener(val clickListener: (election: Election) -> Unit) {
    fun onClick(election: Election) = clickListener(election)
}

sealed class DataItem {
    data class SleepNightItem(val election: Election) : DataItem() {
        override val id = election.id
    }

    object Header : DataItem() {
        override val id = Int.MIN_VALUE
    }

    abstract val id: Int
}
package com.example.android.politicalpreparedness.ui_screens.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data_source.network.models.Election
import com.example.android.politicalpreparedness.databinding.ItemElectionBinding
import com.example.android.politicalpreparedness.ui_screens.common_view_holder.HeaderViewHolder
import com.example.android.politicalpreparedness.ui_screens.common_view_holder.ITEM_HEADER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val ITEM_ELECTION = 2

class ElectionListAdapter(private val clickListener: OnClickListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(ElectionDiffCallback()) {
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is DataItem.Header -> ITEM_HEADER
        is DataItem.ElectionItem -> ITEM_ELECTION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_HEADER -> HeaderViewHolder.from(parent)
            ITEM_ELECTION -> ElectionViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    //TODO: Bind ViewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ElectionViewHolder -> {
                val electionItem = getItem(position) as DataItem.ElectionItem
                holder.bind(electionItem.election, clickListener)
            }

            is HeaderViewHolder -> {
                val headerTitle = getItem(position) as DataItem.Header
                holder.bind(headerTitle.title)
            }
        }
    }

    fun addHeaderAndSubmitList(headerTitle: String, list: List<Election>) {
        adapterScope.launch {
            val items =
                listOf(DataItem.Header(headerTitle)) + list.map { DataItem.ElectionItem(it) }

            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }
}

//TODO: Create ElectionViewHolder
class ElectionViewHolder(private val binding: ItemElectionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    //TODO: Add companion object to inflate ViewHolder (from)
    companion object {
        fun from(parent: ViewGroup) = ElectionViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_election, parent,
                false
            )
        )
    }

    fun bind(item: Election, clickListener: OnClickListener) {
        binding.election = item
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }
}

//TODO: Create ElectionDiffCallback
class ElectionDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

//TODO: Create ElectionListener
class OnClickListener(val clickListener: (election: Election) -> Unit) {
    fun onClick(election: Election) = clickListener(election)
}

sealed class DataItem {
    data class ElectionItem(val election: Election) : DataItem() {
        override val id = election.id
    }

    data class Header(val title: String) : DataItem() {
        override val id = Int.MIN_VALUE
    }

    abstract val id: Int
}
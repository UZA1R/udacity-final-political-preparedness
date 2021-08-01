package com.example.android.politicalpreparedness.ui_screens.representative.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data_source.network.models.Channel
import com.example.android.politicalpreparedness.databinding.ItemRepresentativeBinding
import com.example.android.politicalpreparedness.ui_screens.common_view_holder.HeaderViewHolder
import com.example.android.politicalpreparedness.ui_screens.common_view_holder.ITEM_HEADER
import com.example.android.politicalpreparedness.ui_screens.representative.model.Representative
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_REPRESENTATIVE = 2

class RepresentativeListAdapter :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(RepresentativeDiffCallback()) {
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is DataItem.Header -> ITEM_HEADER
        is DataItem.RepresentativeItem -> ITEM_REPRESENTATIVE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_HEADER -> HeaderViewHolder.from(parent)
            ITEM_REPRESENTATIVE -> RepresentativeViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RepresentativeViewHolder -> {
                val item = getItem(position) as DataItem.RepresentativeItem
                holder.bind(item.representative)
            }

            is HeaderViewHolder -> {
                val headerTitle = getItem(position) as DataItem.Header
                holder.bind(headerTitle.title)
            }
        }
    }

    fun addHeaderAndSubmitList(headerTitle: String, list: List<Representative>) {
        adapterScope.launch {
            val items =
                listOf(DataItem.Header(headerTitle)) + list.map { DataItem.RepresentativeItem(it) }

            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }
}

class RepresentativeViewHolder(private val binding: ItemRepresentativeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    //TODO: Add companion object to inflate ViewHolder (from)
    companion object {
        fun from(parent: ViewGroup) = RepresentativeViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_representative, parent,
                false
            )
        )
    }

    fun bind(item: Representative) {
        binding.representative = item

        //TODO: Show social links ** Hint: Use provided helper methods
        item.official.channels?.let {
            showSocialLinks(item.official.channels)
        }

        //TODO: Show www link ** Hint: Use provided helper methods
        item.official.urls?.let {
            showWWWLinks(item.official.urls)
        }

        binding.executePendingBindings()
    }

    private fun showSocialLinks(channels: List<Channel>) {
        val facebookUrl = getFacebookUrl(channels)
        if (!facebookUrl.isNullOrBlank()) {
            enableLink(binding.facebookIcon, facebookUrl)
        }

        val twitterUrl = getTwitterUrl(channels)
        if (!twitterUrl.isNullOrBlank()) {
            enableLink(binding.twitterIcon, twitterUrl)
        }
    }

    private fun showWWWLinks(urls: List<String>) {
        enableLink(binding.wwwIcon, urls.first())
    }

    private fun getFacebookUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Facebook" }
            .map { channel -> "https://www.facebook.com/${channel.id}" }
            .firstOrNull()
    }

    private fun getTwitterUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Twitter" }
            .map { channel -> "https://www.twitter.com/${channel.id}" }
            .firstOrNull()
    }

    private fun enableLink(view: ImageView, url: String) {
        view.visibility = View.VISIBLE
        view.setOnClickListener { setIntent(url) }
    }

    private fun setIntent(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(ACTION_VIEW, uri)
        itemView.context.startActivity(intent)
    }
}

//TODO: Create ElectionDiffCallback
class RepresentativeDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DataItem {
    data class RepresentativeItem(val representative: Representative) : DataItem() {
        override val id = representative.office.division.id
    }

    data class Header(val title: String) : DataItem() {
        override val id = ""
    }

    abstract val id: String
}
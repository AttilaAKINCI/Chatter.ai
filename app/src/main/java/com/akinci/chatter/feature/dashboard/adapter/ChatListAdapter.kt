package com.akinci.chatter.feature.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.akinci.chatter.feature.acommon.data.local.entities.relations.MessageWithUser
import com.akinci.chatter.feature.dashboard.adapter.viewholder.BaseViewHolder

class ChatListAdapter(
    dataOwnerId: Long
): ListAdapter<MessageWithUser, BaseViewHolder<MessageWithUser>>(DiffCallBack()) {

    private val typeFactory = ViewHolderTypeFactory(dataOwnerId)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MessageWithUser> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return typeFactory.create(layoutInflater, parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type(typeFactory)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<MessageWithUser>, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class DiffCallBack : DiffUtil.ItemCallback<MessageWithUser>() {
    override fun areItemsTheSame(oldItem: MessageWithUser, newItem: MessageWithUser): Boolean {
        return oldItem.messageEntity.id == newItem.messageEntity.id
    }

    override fun areContentsTheSame(oldItem: MessageWithUser, newItem: MessageWithUser): Boolean {
        return oldItem == newItem
    }
}
package com.akinci.chatter.feature.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.akinci.chatter.databinding.RowMessageLeftBinding
import com.akinci.chatter.databinding.RowMessageRightBinding
import com.akinci.chatter.feature.acommon.data.local.entities.relations.MessageWithUser
import com.akinci.chatter.feature.acommon.data.local.entities.relations.MessageWithUser.Companion.LEFT_VIEW
import com.akinci.chatter.feature.acommon.data.local.entities.relations.MessageWithUser.Companion.RIGHT_VIEW
import com.akinci.chatter.feature.dashboard.adapter.viewholder.BaseViewHolder
import com.akinci.chatter.feature.dashboard.adapter.viewholder.LeftViewHolder
import com.akinci.chatter.feature.dashboard.adapter.viewholder.RightViewHolder

class ViewHolderTypeFactory(
    private val dataOwnerId: Long
) {
    fun create(layoutInflater:LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder<MessageWithUser> {
        return when(viewType) {
            LEFT_VIEW -> LeftViewHolder(RowMessageLeftBinding.inflate(layoutInflater, parent, false))
            else -> RightViewHolder(RowMessageRightBinding.inflate(layoutInflater, parent, false))
        }
    }

    fun type(item: MessageWithUser) =
        if(item.userEntity.id == dataOwnerId){
            RIGHT_VIEW
        }else{
            LEFT_VIEW
        }
}
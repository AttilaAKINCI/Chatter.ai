package com.akinci.chatter.feature.acommon.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.akinci.chatter.R
import com.akinci.chatter.feature.acommon.data.local.entities.MessageEntity
import com.akinci.chatter.feature.acommon.data.local.entities.UserEntity
import com.akinci.chatter.feature.dashboard.adapter.ViewHolderTypeFactory

data class MessageWithUser (
    @Embedded val messageEntity: MessageEntity,
    @Relation(
        parentColumn = "messageOwnerId",
        entityColumn = "id"
    )
    val userEntity: UserEntity
) {
    /**
     *  This object is used for message list view. The functions below are part of Visitor Pattern
     *  of Multiple View Recycler View
     * **/

    fun type(viewHolderTypeFactory: ViewHolderTypeFactory): Int {
        return viewHolderTypeFactory.type(this)
    }

    companion object {
        const val LEFT_VIEW = R.layout.row_message_left
        const val RIGHT_VIEW = R.layout.row_message_right
    }
}
package com.akinci.chatter.feature.dashboard.adapter.viewholder

import com.akinci.chatter.R
import com.akinci.chatter.common.component.setGlideImageCentered
import com.akinci.chatter.databinding.RowMessageRightBinding
import com.akinci.chatter.feature.acommon.data.local.entities.relations.MessageWithUser

class RightViewHolder(val binding: RowMessageRightBinding) : BaseViewHolder<MessageWithUser>(binding.root) {
    override fun bind(data : MessageWithUser) {
        binding.data = data

        //TODO fix here
        binding.messageDate.text = "8:30"

        binding.messageOwnerPicture.setGlideImageCentered(
            data.userEntity.avatarURL,
            binding.root.resources.getDimension(R.dimen.message_user_corner_radius).toInt()
        )
        binding.executePendingBindings()
    }
}
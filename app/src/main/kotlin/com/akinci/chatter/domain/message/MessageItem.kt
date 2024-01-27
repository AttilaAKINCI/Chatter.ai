package com.akinci.chatter.domain.message

sealed class MessageItem {
    data object TypeIndicatorItem : MessageItem()
    data class InboundMessageItem(
        val text: String,
        val time: String,
    ) : MessageItem()

    data class OutboundMessageItem(
        val text: String,
        val time: String,
    ) : MessageItem()
}

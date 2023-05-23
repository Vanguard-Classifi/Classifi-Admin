package com.khalidtouch.classifiadmin.model

data class FeedMessage(
    val messageId: Long,
    val uri: String,
    val feedType: MessageType,
) {
    companion object {
        val Default = FeedMessage(
            messageId = -1,
            uri = "",
            MessageType.Unknown
        )
    }
}
package com.khalidtouch.classifiadmin.model.classifi

import com.khalidtouch.classifiadmin.model.MessageType
import java.time.LocalDateTime


data class ClassifiMessage(
    val messageId: Long,
    val feedId: Long,
    val commentId: Long,
    val type: MessageType,
    val message: String,
    val truncatedMessage: String,
    val dateCreated: LocalDateTime,
)

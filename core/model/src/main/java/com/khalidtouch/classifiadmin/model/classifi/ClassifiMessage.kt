package com.khalidtouch.classifiadmin.model.classifi

import com.khalidtouch.classifiadmin.model.MessageType
import kotlinx.datetime.LocalDateTime


data class ClassifiMessage(
    val messageId: Long,
    val type: MessageType,
    val message: String,
    val truncatedMessage: String,
    val dateCreated: LocalDateTime,
)

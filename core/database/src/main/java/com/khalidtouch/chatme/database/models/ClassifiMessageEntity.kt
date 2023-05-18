package com.khalidtouch.chatme.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.khalidtouch.classifiadmin.model.MessageType
import kotlinx.datetime.LocalDateTime


@Entity
data class ClassifiMessageEntity(
    @PrimaryKey(autoGenerate = true) val messageId: Long,
    val feedId: Long,
    val commentId: Long,
    var type: MessageType? = null,
    var message: String? = null,
    var truncatedMessage: String? = null,
    var dateCreated: LocalDateTime? = null,
)
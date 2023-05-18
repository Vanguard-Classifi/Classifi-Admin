package com.khalidtouch.chatme.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity
data class ClassifiCommentEntity(
    @PrimaryKey(autoGenerate = true) val commentId: Long,
    val feedId: Long,
    var userId: Long? = null,
    var dateCreated: LocalDateTime? = null,
)
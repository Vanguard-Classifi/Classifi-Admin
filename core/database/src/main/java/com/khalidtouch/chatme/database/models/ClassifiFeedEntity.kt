package com.khalidtouch.chatme.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import java.util.Date


@Entity
data class ClassifiFeedEntity(
    @PrimaryKey(autoGenerate = true) val feedId: Long,
    var authorName: String? = null,
    var authorImage: String? = null,
    var dateCreated: LocalDateTime? = null,
)
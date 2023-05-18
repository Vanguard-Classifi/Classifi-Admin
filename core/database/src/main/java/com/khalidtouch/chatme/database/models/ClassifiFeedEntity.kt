package com.khalidtouch.chatme.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class ClassifiFeedEntity(
    @PrimaryKey(autoGenerate = true) val feedId: Long,
    var creatorId: Long? = null,
    var dateCreated: LocalDateTime? = null,
)
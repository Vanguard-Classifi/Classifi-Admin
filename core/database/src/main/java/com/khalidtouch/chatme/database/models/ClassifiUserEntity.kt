package com.khalidtouch.chatme.database.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class ClassifiUserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Long,
    @Embedded
    var account: RUserAccount? = null,
    @Embedded
    var profile: RUserProfile? = null,
    var dateCreated: LocalDateTime? = null,
)
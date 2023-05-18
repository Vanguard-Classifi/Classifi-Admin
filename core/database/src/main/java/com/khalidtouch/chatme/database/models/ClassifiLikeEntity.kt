package com.khalidtouch.chatme.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser


@Entity
data class ClassifiLikeEntity(
    @PrimaryKey(autoGenerate = true) val likeId: Long,
    var feedId: Long? = null,
    var commentId: Long? = null,
    var userId: Long? = null,
)
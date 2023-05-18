package com.khalidtouch.chatme.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.khalidtouch.chatme.database.models.ClassifiCommentEntity
import com.khalidtouch.chatme.database.models.ClassifiLikeEntity

data class CommentWithLikes(
    @Embedded val comment: ClassifiCommentEntity,
    @Relation(
        parentColumn = "commentId",
        entityColumn = "commentId",
    )
    val likes: List<ClassifiLikeEntity>,
)
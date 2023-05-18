package com.khalidtouch.chatme.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.khalidtouch.chatme.database.models.ClassifiCommentEntity
import com.khalidtouch.chatme.database.models.ClassifiMessageEntity

data class CommentWithMessages(
    @Embedded val comment: ClassifiCommentEntity,
    @Relation(
        parentColumn = "commentId",
        entityColumn = "commentId",
    )
    val messages: List<ClassifiMessageEntity>,
)
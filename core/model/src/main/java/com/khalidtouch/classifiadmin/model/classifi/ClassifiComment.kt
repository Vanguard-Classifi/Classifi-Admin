package com.khalidtouch.classifiadmin.model.classifi

import kotlinx.datetime.LocalDateTime
import java.util.Date


data class ClassifiComment(
    val commentId: Long,
    val messages: List<ClassifiMessage>,
    val likes: List<ClassifiLike>,
    val feedId: Long,
    val dateCreated: LocalDateTime,
    val userId: Long,
)
package com.khalidtouch.classifiadmin.model.classifi

import java.time.LocalDateTime


data class ClassifiFeed(
    val feedId: Long,
    val creator: ClassifiUser,
    val messages: List<ClassifiMessage>,
    val likes: List<ClassifiLike>,
    val comments: List<ClassifiComment>,
    val classes: List<ClassifiClass>,
    val dateCreated: LocalDateTime,
)
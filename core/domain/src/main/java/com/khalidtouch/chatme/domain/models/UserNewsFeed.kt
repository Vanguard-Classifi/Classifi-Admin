package com.khalidtouch.chatme.domain.models

import com.khalidtouch.classifiadmin.model.UserData
import com.khalidtouch.classifiadmin.model.classifi.ClassifiClass
import com.khalidtouch.classifiadmin.model.classifi.ClassifiComment
import com.khalidtouch.classifiadmin.model.classifi.ClassifiFeed
import com.khalidtouch.classifiadmin.model.classifi.ClassifiLike
import com.khalidtouch.classifiadmin.model.classifi.ClassifiMessage
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import java.time.LocalDateTime

data class UserNewsFeed internal constructor(
    val feedId: Long,
    val creator: ClassifiUser,
    val messages: List<ClassifiMessage>,
    val likes: List<ClassifiLike>,
    val comments: List<ClassifiComment>,
    val classes: List<ClassifiClass>,
    val dateCreated: LocalDateTime,
    val isLiked: Boolean,
) {
    constructor(feed: ClassifiFeed, userData: UserData) : this(
        feedId = feed.feedId,
        creator = feed.creator,
        messages = feed.messages,
        likes = feed.likes,
        comments = feed.comments,
        classes = feed.classes,
        dateCreated = feed.dateCreated,
        isLiked = userData.likedFeeds.contains(feed.feedId),
    )
}

fun List<ClassifiFeed>.mapToUserNewsFeed(userData: UserData): List<UserNewsFeed> {
    return map { UserNewsFeed(it, userData) }
}
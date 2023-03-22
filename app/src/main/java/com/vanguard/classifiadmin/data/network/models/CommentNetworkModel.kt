package com.vanguard.classifiadmin.data.network.models

import com.vanguard.classifiadmin.data.local.models.CommentModel

data class CommentNetworkModel(
    var commentId: String? = null,
    var parentFeedId: String? = null,
    var text: String? = null,
    var authorId: String? = null,
    var authorName: String? = null,
    var schoolId: String? = null,
    var lastModified: String? = null,
    var verified: Boolean? = null,
    var type: String? = null,
    var mediaUris: ArrayList<String> = arrayListOf(),
    var likes: ArrayList<String> = arrayListOf(),
) {
    fun toLocal() = CommentModel(
        commentId = commentId.orEmpty(),
        parentFeedId = parentFeedId,
        text = text,
        authorId = authorId,
        authorName = authorName,
        schoolId = schoolId,
        lastModified = lastModified,
        verified = verified,
        type = type,
        mediaUris = mediaUris,
        likes = likes,
    )
}
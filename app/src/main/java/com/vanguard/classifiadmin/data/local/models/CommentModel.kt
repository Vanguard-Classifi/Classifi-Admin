package com.vanguard.classifiadmin.data.local.models

import com.vanguard.classifiadmin.data.network.models.CommentNetworkModel

data class CommentModel(
    var commentId: String,
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
    fun toNetwork() = CommentNetworkModel(
        commentId = commentId,
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
package com.vanguard.classifiadmin.data.network.models

import com.vanguard.classifiadmin.data.local.models.FeedModel

data class FeedNetworkModel(
    var feedId: String? = null,
    var text: String? = null,
    var authorId: String? = null,
    var authorName: String? = null,
    var schoolId: String? = null,
    var classIds: ArrayList<String> = arrayListOf(),
    var likes: ArrayList<String> = arrayListOf(),
    var commentIds: ArrayList<String> = arrayListOf(),
    var lastModified: String? = null,
    var type: String? = null,
    var verified: Boolean? = null,
    var mediaUris: ArrayList<String> = arrayListOf(),
) {
    fun toLocal() =
        FeedModel(
            feedId = feedId.orEmpty(),
            text = text,
            authorId = authorId,
            authorName = authorName,
            schoolId = schoolId,
            classIds = classIds,
            commentIds = commentIds,
            likes = likes,
            type = type,
            lastModified = lastModified,
            verified = verified,
            mediaUris = mediaUris,
        )
}
package com.vanguard.classifiadmin.data.local.models

import com.vanguard.classifiadmin.data.network.models.FeedNetworkModel

data class FeedModel(
    var feedId: String,
    var text: String? = null,
    var authorId: String? = null,
    var authorName: String? = null,
    var schoolId: String? = null,
    var lastModified: String? = null,
    var classIds: ArrayList<String> = arrayListOf(),
    var likes: ArrayList<String> = arrayListOf(),
    var commentIds: ArrayList<String> = arrayListOf(),
    var verified: Boolean? = null,
    var type: String? = null,
    var mediaUris: ArrayList<String> = arrayListOf(),
){
    fun toNetwork() =
        FeedNetworkModel(
            feedId = feedId,
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

    companion object {
        val Default = FeedModel(
            feedId = "",
        )
    }
}
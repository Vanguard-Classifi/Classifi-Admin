package com.vanguard.classifiadmin.data.local.models

import com.vanguard.classifiadmin.data.network.models.SessionNetworkModel

data class SessionModel(
    val sessionId: String,
    var sessionName: String? = null,
    var termIds: ArrayList<String> = arrayListOf(),
    var schoolId: String? = null,
    var lastModified: String? = null,
) {
    fun toNetwork() = SessionNetworkModel(
        sessionId = sessionId,
        sessionName = sessionName,
        termIds = termIds,
        schoolId = schoolId,
        lastModified = lastModified,
    )
}
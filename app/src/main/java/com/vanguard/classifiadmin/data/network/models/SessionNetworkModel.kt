package com.vanguard.classifiadmin.data.network.models

import com.vanguard.classifiadmin.data.local.models.SessionModel

data class SessionNetworkModel(
    val sessionId: String? = null,
    var sessionName: String? = null,
    var termIds: ArrayList<String> = arrayListOf(),
    var schoolId: String? = null,
    var lastModified: String? = null,
) {
    fun toLocal() = SessionModel(
        sessionId = sessionId ?: "",
        sessionName = sessionName,
        termIds = termIds,
        schoolId = schoolId,
        lastModified = lastModified,
    )
}
package com.vanguard.classifiadmin.data.network.models

import com.vanguard.classifiadmin.data.local.models.TermModel

data class TermNetworkModel(
    val termId: String? = null,
    var termName: String? = null,
    var startDate: String? = null,
    var endDate: String? = null,
    var schoolId: String? = null,
    var verified: Boolean = false,
    var lastModified: String? = null,
) {
    fun toLocal() = TermModel(
        termId = termId ?: "",
        termName = termName,
        startDate = startDate,
        endDate = endDate,
        schoolId = schoolId,
        verified = verified,
        lastModified = lastModified,
    )
}
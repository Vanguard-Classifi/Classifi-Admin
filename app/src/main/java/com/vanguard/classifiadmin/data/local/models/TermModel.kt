package com.vanguard.classifiadmin.data.local.models

import com.vanguard.classifiadmin.data.network.models.TermNetworkModel

data class TermModel(
    val termId: String,
    var termName: String? = null,
    var startDate: String? = null,
    var endDate: String? = null,
    var schoolId: String? = null,
    var verified: Boolean = false,
    var lastModified: String? = null,
) {
    fun toNetwork() = TermNetworkModel(
        termId = termId,
        termName = termName,
        startDate = startDate,
        endDate = endDate,
        schoolId = schoolId,
        verified = verified,
        lastModified = lastModified,
    )
}
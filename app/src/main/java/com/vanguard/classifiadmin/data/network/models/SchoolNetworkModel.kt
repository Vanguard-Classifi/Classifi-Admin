package com.vanguard.classifiadmin.data.network.models

import com.vanguard.classifiadmin.data.local.models.SchoolModel

data class SchoolNetworkModel(
    val schoolId: String? = null,
    var schoolName: String? = null,
    var schoolAddress: String? = null,
    var superAdminId: String? = null,
    var adminIds: ArrayList<String> = arrayListOf(),
    var dateCreated: String? = null,
    var lastModified: String? = null
) {
    fun toLocal() = SchoolModel(
        schoolId = schoolId ?: "",
        schoolName = schoolName,
        schoolAddress = schoolAddress,
        superAdminId = superAdminId,
        adminIds = adminIds,
        dateCreated = dateCreated,
        lastModified = lastModified,
    )
}
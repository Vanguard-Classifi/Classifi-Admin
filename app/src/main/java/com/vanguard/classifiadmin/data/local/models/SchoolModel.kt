package com.vanguard.classifiadmin.data.local.models

import com.vanguard.classifiadmin.data.network.models.SchoolNetworkModel

data class SchoolModel(
    val schoolId: String,
    var schoolName: String? = null,
    var schoolAddress: String? = null,
    var superAdminId: String? = null,
    var adminIds: ArrayList<String> = arrayListOf(),
    var dateCreated: String? = null,
    var lastModified: String? = null
) {
    fun toNetwork() = SchoolNetworkModel(
        schoolId = schoolId,
        schoolName = schoolName,
        schoolAddress = schoolAddress,
        superAdminId = superAdminId,
        adminIds = adminIds,
        dateCreated = dateCreated,
        lastModified = lastModified,
    )
}
package com.vanguard.classifiadmin.data.network.models

import com.vanguard.classifiadmin.data.local.models.WeeklyPlanModel


data class WeeklyPlanNetworkModel(
    val weeklyPlanId: String? = null,
    var schoolId: String? = null,
    var planIds: ArrayList<String> = arrayListOf(),
    var lastModified: String? = null,
    var duration: String? = null,
) {
    fun toLocal() = WeeklyPlanModel(
        weeklyPlanId = weeklyPlanId ?: "",
        schoolId = schoolId,
        planIds = planIds,
        lastModified = lastModified,
        duration = duration
    )
}
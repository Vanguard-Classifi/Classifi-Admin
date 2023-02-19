package com.vanguard.classifiadmin.data.network.models

import com.vanguard.classifiadmin.data.local.models.PlanModel

data class PlanNetworkModel(
    val planId: String? = null,
    var weeklyPlanId: String? = null,
    var classPeriod: String? = null,
    var subjectName: String? = null,
    var pages: String? = null,
    var topic: String? = null,
    var homeWork: String? = null,
    var notes: String? = null,
    var lastModified: String? = null,
    var dayOfWeek: String? = null,
    var date: String? = null,
) {
    fun toLocal() = PlanModel(
        planId = planId ?: "",
        weeklyPlanId = weeklyPlanId,
        classPeriod = classPeriod,
        subjectName = subjectName,
        pages = pages,
        topic = topic,
        homeWork = homeWork,
        notes = notes,
        lastModified = lastModified,
        dayOfWeek = dayOfWeek,
        date = date,
    )
}
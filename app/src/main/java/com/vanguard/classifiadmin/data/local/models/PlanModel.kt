package com.vanguard.classifiadmin.data.local.models

import com.vanguard.classifiadmin.data.network.models.PlanNetworkModel

data class PlanModel(
    val planId: String,
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
    fun toNetwork() = PlanNetworkModel(
        planId = planId,
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
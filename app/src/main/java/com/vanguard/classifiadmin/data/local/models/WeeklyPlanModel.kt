package com.vanguard.classifiadmin.data.local.models

import com.vanguard.classifiadmin.data.network.models.WeeklyPlanNetworkModel

data class WeeklyPlanModel(
   val weeklyPlanId: String,
   var schoolId: String? = null,
   var planIds: ArrayList<String> = arrayListOf(),
   var lastModified: String? = null,
   var duration: String? = null,
) {
   fun toNetwork() = WeeklyPlanNetworkModel(
      weeklyPlanId = weeklyPlanId,
      schoolId = schoolId,
      planIds = planIds,
      lastModified = lastModified,
      duration = duration
   )
}
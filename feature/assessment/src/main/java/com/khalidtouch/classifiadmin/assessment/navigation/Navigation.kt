package com.khalidtouch.classifiadmin.assessment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.khalidtouch.classifiadmin.assessment.AssessmentRoute

const val assessmentsNavigationRoute = "assessments_navigation_route"

fun NavController.navigateToAssessments( navOptions: NavOptions? = null){
    this.navigate(assessmentsNavigationRoute, navOptions)
}


fun NavGraphBuilder.assessmentsScreen() {
    composable(route = assessmentsNavigationRoute) {
       AssessmentRoute()
    }
}
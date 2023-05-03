package com.khalidtouch.classifiadmin.students.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val studentsNavigationRoute = "studentsNavigationRoute"

fun NavController.navigateToStudents(navOptions: NavOptions? = null) {
    this.navigate(studentsNavigationRoute, navOptions)
}

fun NavGraphBuilder.studentsScreen() {
    composable(route = studentsNavigationRoute) {
       StudentsRoute()
    }
}

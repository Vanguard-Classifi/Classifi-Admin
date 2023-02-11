package com.vanguard.classifiadmin.ui.screens.dashboard

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.router.BottomDestination
import com.vanguard.classifiadmin.viewmodel.MainViewModel

const val MAIN_DASHBOARD_SCREEN = "main_dashboard_screen"

@Composable
fun MainDashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    val navController = rememberNavController()

    BoxWithConstraints(modifier = modifier) {
        Scaffold(modifier = modifier,
            topBar = {
                     TopBar()
            },
            bottomBar = {
                BottomBar(
                    navController = navController,
                )
            },
            content = { padding ->
                BottomContainer(
                    viewModel = viewModel,
                    modifier = modifier.padding(padding),
                    navController = navController,
                )
            })
    }

}


enum class DestinationItem(val label: String, val icon: Int, val screen: String) {
    Feed("Feed", icon = R.drawable.icon_feeds, BottomDestination.feeds),
    Students("Students", R.drawable.icon_students, BottomDestination.students),
    Assessments("Assessments", R.drawable.icon_assessment, BottomDestination.assessments),
    Reports("Reports", R.drawable.icon_reports, BottomDestination.reports)
}

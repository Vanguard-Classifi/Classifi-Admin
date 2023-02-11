package com.vanguard.classifiadmin.ui.screens.dashboard

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var filterActivated by remember { mutableStateOf(false)}

    BoxWithConstraints(modifier = modifier) {
        Scaffold(modifier = modifier,
            topBar = {
                     TopBar(
                         filterActivated = filterActivated,
                         onFilter = {
                             filterActivated = !filterActivated
                         },
                         openProfile = {},
                         openSheet = {},
                         username = "Hamza Jesim",
                         filterLabel = "Grade 1",
                     )
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

package com.vanguard.classifiadmin.ui.screens.dashboard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vanguard.classifiadmin.router.BottomNavGraph
import com.vanguard.classifiadmin.viewmodel.MainViewModel

@Composable
fun BottomContainer(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavHostController = rememberNavController()
) {
    BottomNavGraph(
        viewModel = viewModel,
        modifier = Modifier.fillMaxSize(),
        navController = navController,
    )
}
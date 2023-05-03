package com.vanguard.classifiadmin.router

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vanguard.classifiadmin.ui.theme.ClassifiAdminTheme
import com.vanguard.classifiadmin.viewmodel.MainViewModel


@Deprecated(message = "This function has been deprecated.", replaceWith = ReplaceWith("ClassifiApp"))
@Composable
fun ClassifiApp(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    finishActivity: () -> Unit,
    darkTheme: Boolean = false,
    navController: NavHostController = rememberNavController(),
) {
    ClassifiAdminTheme(darkTheme = darkTheme) {
        Surface(modifier = modifier.fillMaxSize()) {
            NavGraph(
                viewModel = viewModel,
                finishActivity = finishActivity,
                modifier = modifier,
                navController = navController,
            )
        }
    }
}
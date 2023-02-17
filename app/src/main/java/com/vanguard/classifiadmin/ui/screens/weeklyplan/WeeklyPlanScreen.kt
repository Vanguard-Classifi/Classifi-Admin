package com.vanguard.classifiadmin.ui.screens.weeklyplan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vanguard.classifiadmin.viewmodel.MainViewModel

const val WEEKLY_PLAN_SCREEN = "weekly_plan_screen"

@Composable
fun WeeklyPlanScreen (
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
){
    WeeklyPlanScreenContent()
}

@Composable
fun WeeklyPlanScreenContent(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.surface,
    ) {
        Column(modifier = Modifier) {
            Text(text = "Weekly plan")
        }
    }
}
package com.vanguard.classifiadmin.ui.screens.weeklyplan

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.vanguard.classifiadmin.ui.components.WeeklyPlanGrid
import com.vanguard.classifiadmin.ui.components.WeeklyPlanModel
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import java.util.UUID

const val WEEKLY_PLAN_DETAIL_SCREEN = "weekly_plan_detail_screen"


@Composable
fun WeeklyPlanDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    WeeklyPlanDetailScreenContent()
}


@SuppressLint("MutableCollectionMutableState")
@Composable
fun WeeklyPlanDetailScreenContent(
    modifier: Modifier = Modifier,
) {
    val plans by remember { mutableStateOf(WeeklyPlanModel.init())}

    WeeklyPlanGrid(
        onTap = {},
        onEdit = {},
        headingColor = MaterialTheme.colors.primary,
        headingBackgroundColor = MaterialTheme.colors.primary.copy(0.3f),
        plans = plans,
        headings = WeeklyPlanHeading.values().toList().map { it.name },
        modifier = Modifier.fillMaxSize()
    )
}

enum class WeeklyPlanHeading {
    Class, Subject, Pages, Topic, HomeWork, Notes,
}
package com.khalidtouch.classifiadmin.reports.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
internal fun ReportsRoute(
    modifier: Modifier = Modifier,
    reportsViewModel: ReportsViewModel = hiltViewModel<ReportsViewModel>(),
) {
    ReportsScreen(modifier = modifier)
}


@Composable
internal fun ReportsScreen(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Reports Screen",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}
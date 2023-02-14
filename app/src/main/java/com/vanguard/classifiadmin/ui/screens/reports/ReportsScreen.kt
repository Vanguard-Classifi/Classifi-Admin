package com.vanguard.classifiadmin.ui.screens.reports

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vanguard.classifiadmin.viewmodel.MainViewModel

const val REPORTS_SCREEN = "reports_screen"

@Composable
fun ReportsScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Wake up Reports  screen")
        }
    }
}
package com.vanguard.classifiadmin.ui.screens.feeds

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Popup
import com.vanguard.classifiadmin.viewmodel.MainViewModel

const val FEEDS_SCREEN = "feeds_screen"

@Composable
fun FeedsScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    UpcomingActivities(username = "Hamza Jesim")
    Popup {
        Text("This is a popup widget")
    }
}
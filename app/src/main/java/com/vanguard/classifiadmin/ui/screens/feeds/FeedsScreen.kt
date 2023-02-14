package com.vanguard.classifiadmin.ui.screens.feeds

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.vanguard.classifiadmin.viewmodel.MainViewModel

const val FEEDS_SCREEN = "feeds_screen"

@Composable
fun FeedsScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Wake up Feeds  screen")
        }
    }
}
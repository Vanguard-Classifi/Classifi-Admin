package com.vanguard.classifiadmin.ui.screens.feeds

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
   BoxWithConstraints(modifier = modifier.fillMaxWidth().padding(0.dp), contentAlignment = Alignment.BottomCenter) {
       Text("News Feeds")
   }
}
package com.khalidtouch.classifiadmin.feeds

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
internal fun FeedsRoute(
    modifier: Modifier = Modifier,
   feedsViewModel: FeedsViewModel = hiltViewModel<FeedsViewModel>()
) {
    FeedsScreen(modifier = modifier)
}


@Composable
internal fun FeedsScreen(
    modifier: Modifier = Modifier,
){
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Feeds Screen",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}
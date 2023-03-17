package com.vanguard.classifiadmin.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.viewmodel.MainViewModel

@Composable
fun MyAccountScreenAccountSettings(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "fegneine",
                fontSize = 94.sp,
            )
        }
    }
}

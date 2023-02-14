package com.vanguard.classifiadmin.ui.screens.assessments

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vanguard.classifiadmin.viewmodel.MainViewModel

const val ASSESSMENT_SCREEN = "assessment_screen"

@Composable
fun AssessmentsScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Wake up Assessment screen")
        }
    }
}


@Composable
fun AssessmentsScreenContent(
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Text(text = "Assessment")
        }
    }
}
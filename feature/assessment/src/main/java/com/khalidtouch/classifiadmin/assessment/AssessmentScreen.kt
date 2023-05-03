package com.khalidtouch.classifiadmin.assessment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun AssessmentRoute(
    modifier: Modifier = Modifier,
    assessmentViewModel: AssessmentViewModel = hiltViewModel<AssessmentViewModel>()
) {
    AssessmentScreen(modifier = modifier)
}


@Composable
internal fun AssessmentScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Assessments Screen",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}
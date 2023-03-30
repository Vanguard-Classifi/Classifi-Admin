package com.vanguard.classifiadmin.ui.screens.assessments.states

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vanguard.classifiadmin.data.local.models.AssessmentModel
import com.vanguard.classifiadmin.viewmodel.MainViewModel


@Composable
fun AssessmentsScreenContentPublished(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    verticalScroll: LazyListState,
    onPublishedAssessmentOptions: (AssessmentModel) -> Unit,
    onSelectAssessment: (AssessmentModel) -> Unit,
) {
    /**
    LazyColumn(
    modifier = Modifier
    .padding(bottom = 72.dp),
    state = verticalScroll,
    ) {
    items(items) { each ->
    AssessmentItem(
    assessment = each,
    onOptions = onPublishedAssessmentOptions,
    onSelectAssessment = onSelectAssessment,
    )
    }
    }

     */
}

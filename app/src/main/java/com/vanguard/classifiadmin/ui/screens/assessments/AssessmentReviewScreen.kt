package com.vanguard.classifiadmin.ui.screens.assessments

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.ui.components.ChildTopBar
import com.vanguard.classifiadmin.viewmodel.MainViewModel

const val ASSESSMENT_REVIEW_SCREEN = "assessment_review_screen"

@Composable
fun AssessmentReviewScreen (
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
){
    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        Scaffold(modifier = modifier,
            topBar = {
                ChildTopBar(
                    onBack = onBack,
                    heading = stringResource(id = R.string.review_questions),
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.primary,
                )
            },
            content = { padding ->
                AssessmentReviewScreenContent(modifier = modifier.padding(padding))
            })
    }
}

@Composable
fun AssessmentReviewScreenContent(
    modifier: Modifier = Modifier
){

}
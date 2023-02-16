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
import com.vanguard.classifiadmin.ui.components.ChildTopBarWithInfo
import com.vanguard.classifiadmin.viewmodel.MainViewModel

const val MODIFY_ASSESSMENT_SCREEN = "modify_assessment_screen"

@Composable
fun ModifyAssessmentScreen (
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
){
    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        Scaffold(modifier = modifier,
            topBar = {
                ChildTopBarWithInfo(
                    onBack = onBack,
                    heading = "Year 11 Islamic Studies",
                    subheading = "12 Aug 2022 - 14 Aug 2022",
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.primary,
                    onInfo = {}
                )
            },
            content = { padding ->
                ModifyAssessmentScreenContent(modifier = modifier.padding(padding))
            })
    }
}

@Composable
fun ModifyAssessmentScreenContent(
    modifier: Modifier = Modifier
) {

}
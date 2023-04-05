package com.vanguard.classifiadmin.ui.screens.attempt.briefing

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.ui.components.ChildTopBar
import com.vanguard.classifiadmin.ui.components.ChildTopBarWithCloseButtonOnly
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.PrimaryTextButtonFillWidth
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentType
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel

const val BRIEFING_SCREEN = "briefing_screen"

@Composable
fun BriefingScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
) {
    Surface(modifier = modifier) {
        BoxWithConstraints(modifier = modifier) {
            val maxWidth = maxWidth
            val maxHeight = maxHeight

            Scaffold(
                modifier = modifier,
                topBar = {
                       ChildTopBarWithCloseButtonOnly(
                          onClose = onBack,
                       )
                },
                content = {
                    BriefingScreenContent(
                        modifier = modifier.padding(it),
                        onStartAssessment = {},
                        viewModel = viewModel,
                    )
                }
            )
        }
    }
}



@Composable
fun BriefingScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onStartAssessment: () -> Unit,
) {
    val currentAssessmentIdPublished by viewModel.currentAssessmentIdPublished.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val feedByIdNetwork by viewModel.feedByIdNetwork.collectAsState()
    val currentClass = remember(feedByIdNetwork.data){
        if(feedByIdNetwork is Resource.Success && feedByIdNetwork.data != null){
            feedByIdNetwork.data?.assessmentClass.orEmpty()
        } else ""
    }
    val assessmentType = remember(feedByIdNetwork.data){
        if(feedByIdNetwork is Resource.Success && feedByIdNetwork.data != null){
            feedByIdNetwork.data?.assessmentType.orEmpty()
        } else ""
    }

    LaunchedEffect(Unit){
        viewModel.getCurrentSchoolIdPref()
        viewModel.getFeedByIdNetwork(
            currentAssessmentIdPublished.orEmpty(),
            currentSchoolIdPref.orEmpty()
        )
    }

    Surface(modifier = Modifier) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val maxWidth = maxWidth
            val maxHeight = maxHeight

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    val imageRes = when(assessmentType){
                        AssessmentType.Exam.title -> R.drawable.icon_exam
                        AssessmentType.HomeWork.title -> R.drawable.icon_homework
                        AssessmentType.Quiz.title -> R.drawable.icon_quiz
                        else -> R.drawable.icon_homework
                    }

                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = stringResource(id = R.string.assessment_image),
                        modifier = Modifier.size(200.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Class 2",
                    fontSize = 16.sp,
                    color = Black100,
                )

                Text(
                    text = "HomeWork",
                    fontSize = 22.sp,
                    color = Black100,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "This assessment should be submitted latest 23rd April, 2023 by 08:00am. Goodluck!",
                    fontSize = 14.sp,
                    color = Black100,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(maxWidth.times(0.8f))
                )

                Spacer(modifier = Modifier.height(64.dp))

                PrimaryTextButtonFillWidth(
                    label = stringResource(id = R.string.start_assessment),
                    onClick = { onStartAssessment() }
                )
            }
        }
    }
}
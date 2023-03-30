package com.vanguard.classifiadmin.ui.screens.assessments.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.ui.components.LoadingScreen
import com.vanguard.classifiadmin.ui.components.NoDataInline
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentCreationAddQuestionFeature
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentCreationBottomSheetMode
import com.vanguard.classifiadmin.ui.screens.assessments.items.AssessmentCreationAddQuestionFeatureItem
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel

@Composable
fun AssessmentCreationBottomSheetScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onDone: () -> Unit,
    maxHeight: Dp,
    onSelect: (AssessmentCreationAddQuestionFeature) -> Unit,
    addQuestionFeatures: List<AssessmentCreationAddQuestionFeature> =
        AssessmentCreationAddQuestionFeature.values().toList(),
) {
    val mode by viewModel.assessmentCreationBottomSheetMode.collectAsState()
    val stagedAssessmentsNetwork by viewModel.stagedAssessmentsNetwork.collectAsState()

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(16.dp),
                color = Black100.copy(0.5f)
            ) {
                Box(
                    modifier = modifier
                        .width(102.dp)
                        .height(3.dp)
                )
            }
        }

        when (mode) {
            is AssessmentCreationBottomSheetMode.AddQuestion -> {
                addQuestionFeatures.forEach { feature ->
                    AssessmentCreationAddQuestionFeatureItem(
                        feature = feature,
                        onSelect = onSelect,
                    )
                }
            }

            is AssessmentCreationBottomSheetMode.Info -> {
                when (stagedAssessmentsNetwork) {
                    is Resource.Loading -> {
                        LoadingScreen(maxHeight = maxHeight)
                    }

                    is Resource.Success -> {
                        if (stagedAssessmentsNetwork.data?.isNotEmpty() == true) {
                            AssessmentCreationBottomSheetInfo(
                                onDone = onDone,
                                assessment = stagedAssessmentsNetwork.data?.first()?.toLocal()!!
                            )
                        } else {
                            NoDataInline(message = stringResource(id = R.string.could_not_load_assessment))
                        }
                    }

                    is Resource.Error -> {
                        NoDataInline(message = stringResource(id = R.string.could_not_load_assessment))
                    }
                }
            }
        }
    }
}
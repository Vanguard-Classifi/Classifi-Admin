package com.vanguard.classifiadmin.ui.screens.assessments.states

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.AssessmentModel
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.domain.helpers.UserRole
import com.vanguard.classifiadmin.ui.components.LoadingScreen
import com.vanguard.classifiadmin.ui.components.NoDataScreen
import com.vanguard.classifiadmin.ui.screens.assessments.items.AssessmentItem
import com.vanguard.classifiadmin.viewmodel.MainViewModel

@Composable
fun AssessmentsScreenContentInReview(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onInReviewAssessmentOptions: (AssessmentModel) -> Unit,
    onSelectAssessment: (AssessmentModel) -> Unit,
    maxHeight: Dp,
) {
    val verifiedAssessmentsInReviewNetwork by viewModel.verifiedAssessmentsInReviewNetwork.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val currentUserRolePref by viewModel.currentUserRolePref.collectAsState()
    val currentClassFeedPref by viewModel.currentClassFeedPref.collectAsState()
    val verifiedAssessmentsInReviewForClassNetwork by viewModel.verifiedAssessmentsInReviewForClassNetwork.collectAsState()

    LaunchedEffect(Unit, currentUserRolePref, currentClassFeedPref) {
        viewModel.getCurrentSchoolIdPref()
        viewModel.getCurrentUserRolePref()
    }

    when(currentUserRolePref){
        UserRole.Teacher.name -> {
            when (verifiedAssessmentsInReviewForClassNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedAssessmentsInReviewForClassNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier,
                            state = rememberLazyListState()
                        ) {
                            val verifiedAssessmentsInReviewSorted =
                                verifiedAssessmentsInReviewForClassNetwork.data?.sortedByDescending { it.lastModified }
                            items(verifiedAssessmentsInReviewSorted!!) { assessment ->
                                AssessmentItem(
                                    assessment = assessment.toLocal(),
                                    onOptions = onInReviewAssessmentOptions,
                                    onSelectAssessment = onSelectAssessment,
                                )
                            }
                        }
                    } else {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.no_assessment_in_review),
                            buttonLabel = "",
                            onClick = {},
                            showButton = false,
                        )
                    }
                }

                is Resource.Error -> {
                    NoDataScreen(
                        maxHeight = maxHeight, message = stringResource(id = R.string.something_went_wrong),
                        buttonLabel = stringResource(id = R.string.retry),
                        onClick = {
                            //reload
                            viewModel.getVerifiedAssessmentsInReviewForClassNetwork(
                                currentClassFeedPref.orEmpty(),
                                currentSchoolIdPref.orEmpty()
                            )
                        }
                    )
                }
            }
        }
        UserRole.Parent.name -> {
            when (verifiedAssessmentsInReviewForClassNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedAssessmentsInReviewForClassNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier,
                            state = rememberLazyListState()
                        ) {
                            items(verifiedAssessmentsInReviewForClassNetwork.data!!) { assessment ->
                                AssessmentItem(
                                    assessment = assessment.toLocal(),
                                    onOptions = onInReviewAssessmentOptions,
                                    onSelectAssessment = onSelectAssessment,
                                )
                            }
                        }
                    } else {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.no_assessment_in_review),
                            buttonLabel = "",
                            onClick = {},
                            showButton = false,
                        )
                    }
                }

                is Resource.Error -> {
                    NoDataScreen(
                        maxHeight = maxHeight, message = stringResource(id = R.string.something_went_wrong),
                        buttonLabel = stringResource(id = R.string.retry),
                        onClick = {
                            //reload
                            viewModel.getVerifiedAssessmentsInReviewForClassNetwork(
                                currentClassFeedPref.orEmpty(),
                                currentSchoolIdPref.orEmpty()
                            )
                        }
                    )
                }
            }
        }
        UserRole.Student.name -> {
            when (verifiedAssessmentsInReviewForClassNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedAssessmentsInReviewForClassNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier,
                            state = rememberLazyListState()
                        ) {
                            items(verifiedAssessmentsInReviewForClassNetwork.data!!) { assessment ->
                                AssessmentItem(
                                    assessment = assessment.toLocal(),
                                    onOptions = onInReviewAssessmentOptions,
                                    onSelectAssessment = onSelectAssessment,
                                )
                            }
                        }
                    } else {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.no_assessment_in_review),
                            buttonLabel = "",
                            onClick = {},
                            showButton = false,
                        )
                    }
                }

                is Resource.Error -> {
                    NoDataScreen(
                        maxHeight = maxHeight, message = stringResource(id = R.string.something_went_wrong),
                        buttonLabel = stringResource(id = R.string.retry),
                        onClick = {
                            //reload
                            viewModel.getVerifiedAssessmentsInReviewForClassNetwork(
                                currentClassFeedPref.orEmpty(),
                                currentSchoolIdPref.orEmpty()
                            )
                        }
                    )
                }
            }
        }
        UserRole.Admin.name -> {
            when (verifiedAssessmentsInReviewNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedAssessmentsInReviewNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier,
                            state = rememberLazyListState()
                        ) {
                            items(verifiedAssessmentsInReviewNetwork.data!!) { assessment ->
                                AssessmentItem(
                                    assessment = assessment.toLocal(),
                                    onOptions = onInReviewAssessmentOptions,
                                    onSelectAssessment = onSelectAssessment,
                                )
                            }
                        }
                    } else {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.no_assessment_in_review),
                            buttonLabel = "",
                            onClick = {},
                            showButton = false,
                        )
                    }
                }

                is Resource.Error -> {
                    NoDataScreen(
                        maxHeight = maxHeight, message = stringResource(id = R.string.something_went_wrong),
                        buttonLabel = stringResource(id = R.string.retry),
                        onClick = {
                            //reload
                            viewModel.getVerifiedAssessmentsInReviewNetwork(currentSchoolIdPref.orEmpty())
                        }
                    )
                }
            }
        }
        UserRole.SuperAdmin.name -> {
            when (verifiedAssessmentsInReviewNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedAssessmentsInReviewNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier,
                            state = rememberLazyListState()
                        ) {
                            items(verifiedAssessmentsInReviewNetwork.data!!) { assessment ->
                                AssessmentItem(
                                    assessment = assessment.toLocal(),
                                    onOptions = onInReviewAssessmentOptions,
                                    onSelectAssessment = onSelectAssessment,
                                )
                            }
                        }
                    } else {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.no_assessment_in_review),
                            buttonLabel = "",
                            onClick = {},
                            showButton = false,
                        )
                    }
                }

                is Resource.Error -> {
                    NoDataScreen(
                        maxHeight = maxHeight, message = stringResource(id = R.string.something_went_wrong),
                        buttonLabel = stringResource(id = R.string.retry),
                        onClick = {
                            //reload
                            viewModel.getVerifiedAssessmentsInReviewNetwork(currentSchoolIdPref.orEmpty())
                        }
                    )
                }
            }
        }
    }
}

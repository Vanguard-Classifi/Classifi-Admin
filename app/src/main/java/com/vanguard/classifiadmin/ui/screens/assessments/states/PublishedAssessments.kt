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
fun AssessmentsScreenContentPublished(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onPublishedAssessmentOptions: (AssessmentModel) -> Unit,
    onSelectAssessment: (AssessmentModel) -> Unit,
    maxHeight: Dp,
) {
    val verifiedAssessmentsPublishedNetwork by viewModel.verifiedAssessmentsPublishedNetwork.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val currentUserRolePref by viewModel.currentUserRolePref.collectAsState()
    val currentClassFeedPref by viewModel.currentClassFeedPref.collectAsState()
    val verifiedAssessmentsPublishedForClassNetwork by viewModel.verifiedAssessmentsPublishedForClassNetwork.collectAsState()

    LaunchedEffect(Unit, currentUserRolePref, currentClassFeedPref) {
        viewModel.getCurrentUserRolePref()
        viewModel.getCurrentSchoolIdPref()
    }

    when(currentUserRolePref){
        UserRole.Teacher.name -> {
            when (verifiedAssessmentsPublishedForClassNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedAssessmentsPublishedForClassNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier,
                            state = rememberLazyListState()
                        ) {
                            val verifiedAssessmentsPublishedSorted =
                                verifiedAssessmentsPublishedForClassNetwork.data?.sortedByDescending { it.lastModified }
                            items(verifiedAssessmentsPublishedSorted!!) { assessment ->
                                AssessmentItem(
                                    assessment = assessment.toLocal(),
                                    onOptions = onPublishedAssessmentOptions,
                                    onSelectAssessment = onSelectAssessment,
                                )
                            }
                        }
                    } else {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.no_published_assessment),
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
                            viewModel.getVerifiedAssessmentsPublishedForClassNetwork(
                                currentClassFeedPref.orEmpty(),
                                currentSchoolIdPref.orEmpty()
                            )
                        }
                    )
                }
            }
        }
        UserRole.Parent.name -> {
            when (verifiedAssessmentsPublishedForClassNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedAssessmentsPublishedForClassNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier,
                            state = rememberLazyListState()
                        ) {
                            items(verifiedAssessmentsPublishedForClassNetwork.data!!) { assessment ->
                                AssessmentItem(
                                    assessment = assessment.toLocal(),
                                    onOptions = onPublishedAssessmentOptions,
                                    onSelectAssessment = onSelectAssessment,
                                )
                            }
                        }
                    } else {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.no_published_assessment),
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
                            viewModel.getVerifiedAssessmentsPublishedForClassNetwork(
                                currentClassFeedPref.orEmpty(),
                                currentSchoolIdPref.orEmpty()
                            )
                        }
                    )
                }
            }
        }
        UserRole.Student.name -> {
            when (verifiedAssessmentsPublishedForClassNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedAssessmentsPublishedForClassNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier,
                            state = rememberLazyListState()
                        ) {
                            items(verifiedAssessmentsPublishedForClassNetwork.data!!) { assessment ->
                                AssessmentItem(
                                    assessment = assessment.toLocal(),
                                    onOptions = onPublishedAssessmentOptions,
                                    onSelectAssessment = onSelectAssessment,
                                )
                            }
                        }
                    } else {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.no_published_assessment),
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
                            viewModel.getVerifiedAssessmentsPublishedForClassNetwork(
                                currentClassFeedPref.orEmpty(),
                                currentSchoolIdPref.orEmpty()
                            )
                        }
                    )
                }
            }
        }
        UserRole.Admin.name -> {
            when (verifiedAssessmentsPublishedNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedAssessmentsPublishedNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier,
                            state = rememberLazyListState()
                        ) {
                            items(verifiedAssessmentsPublishedNetwork.data!!) { assessment ->
                                AssessmentItem(
                                    assessment = assessment.toLocal(),
                                    onOptions = onPublishedAssessmentOptions,
                                    onSelectAssessment = onSelectAssessment,
                                )
                            }
                        }
                    } else {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.no_published_assessment),
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
                            viewModel.getVerifiedAssessmentsPublishedNetwork(currentSchoolIdPref.orEmpty())
                        }
                    )
                }
            }
        }
        UserRole.SuperAdmin.name -> {
            when (verifiedAssessmentsPublishedNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedAssessmentsPublishedNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier,
                            state = rememberLazyListState()
                        ) {
                            items(verifiedAssessmentsPublishedNetwork.data!!) { assessment ->
                                AssessmentItem(
                                    assessment = assessment.toLocal(),
                                    onOptions = onPublishedAssessmentOptions,
                                    onSelectAssessment = onSelectAssessment,
                                )
                            }
                        }
                    } else {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.no_published_assessment),
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
                            viewModel.getVerifiedAssessmentsPublishedNetwork(currentSchoolIdPref.orEmpty())
                        }
                    )
                }
            }
        }
    }
}

package com.vanguard.classifiadmin.ui.screens.assessments.states

import androidx.compose.foundation.lazy.LazyColumn
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
fun AssessmentsScreenContentDraft(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onDraftAssessmentOptions: (AssessmentModel) -> Unit,
    onSelectAssessment: (AssessmentModel) -> Unit,
    maxHeight: Dp,
) {
    val verifiedAssessmentsDraftNetwork by viewModel.verifiedAssessmentsDraftNetwork.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val currentUserRolePref by viewModel.currentUserRolePref.collectAsState()
    val currentClassFeedPref by viewModel.currentClassFeedPref.collectAsState()
    val verifiedAssessmentsDraftForClassNetwork by viewModel.verifiedAssessmentsDraftForClassNetwork.collectAsState()

    LaunchedEffect(Unit, currentUserRolePref, currentClassFeedPref) {
        viewModel.getCurrentUserRolePref()
        viewModel.getCurrentSchoolIdPref()
    }

    when(currentUserRolePref){
        UserRole.Teacher.name -> {
            when (verifiedAssessmentsDraftForClassNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedAssessmentsDraftForClassNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier,
                            state = rememberLazyListState()
                        ) {
                            items(verifiedAssessmentsDraftForClassNetwork.data!!) { assessment ->
                                AssessmentItem(
                                    assessment = assessment.toLocal(),
                                    onOptions = onDraftAssessmentOptions,
                                    onSelectAssessment = onSelectAssessment,
                                )
                            }
                        }
                    } else {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.no_assessment_in_draft),
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
                            viewModel.getVerifiedAssessmentsDraftForClassNetwork(
                                currentClassFeedPref.orEmpty(),
                                currentSchoolIdPref.orEmpty()
                            )
                        }
                    )
                }
            }
        }
        UserRole.Parent.name -> {
            when (verifiedAssessmentsDraftForClassNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedAssessmentsDraftForClassNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier,
                            state = rememberLazyListState()
                        ) {
                            items(verifiedAssessmentsDraftForClassNetwork.data!!) { assessment ->
                                AssessmentItem(
                                    assessment = assessment.toLocal(),
                                    onOptions = onDraftAssessmentOptions,
                                    onSelectAssessment = onSelectAssessment,
                                )
                            }
                        }
                    } else {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.no_assessment_in_draft),
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
                            viewModel.getVerifiedAssessmentsDraftForClassNetwork(
                                currentClassFeedPref.orEmpty(),
                                currentSchoolIdPref.orEmpty()
                            )
                        }
                    )
                }
            }
        }
        UserRole.Student.name -> {
            when (verifiedAssessmentsDraftForClassNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedAssessmentsDraftForClassNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier,
                            state = rememberLazyListState()
                        ) {
                            items(verifiedAssessmentsDraftForClassNetwork.data!!) { assessment ->
                                AssessmentItem(
                                    assessment = assessment.toLocal(),
                                    onOptions = onDraftAssessmentOptions,
                                    onSelectAssessment = onSelectAssessment,
                                )
                            }
                        }
                    } else {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.no_assessment_in_draft),
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
                            viewModel.getVerifiedAssessmentsDraftForClassNetwork(
                                currentClassFeedPref.orEmpty(),
                                currentSchoolIdPref.orEmpty()
                            )
                        }
                    )
                }
            }
        }
        UserRole.Admin.name -> {
            when (verifiedAssessmentsDraftNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedAssessmentsDraftNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier,
                            state = rememberLazyListState()
                        ) {
                            items(verifiedAssessmentsDraftNetwork.data!!) { assessment ->
                                AssessmentItem(
                                    assessment = assessment.toLocal(),
                                    onOptions = onDraftAssessmentOptions,
                                    onSelectAssessment = onSelectAssessment,
                                )
                            }
                        }
                    } else {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.no_assessment_in_draft),
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
                            viewModel.getVerifiedAssessmentsDraftNetwork(currentSchoolIdPref.orEmpty())
                        }
                    )
                }
            }
        }
        UserRole.SuperAdmin.name -> {
            when (verifiedAssessmentsDraftNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedAssessmentsDraftNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier,
                            state = rememberLazyListState()
                        ) {
                            items(verifiedAssessmentsDraftNetwork.data!!) { assessment ->
                                AssessmentItem(
                                    assessment = assessment.toLocal(),
                                    onOptions = onDraftAssessmentOptions,
                                    onSelectAssessment = onSelectAssessment,
                                )
                            }
                        }
                    } else {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.no_assessment_in_draft),
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
                            viewModel.getVerifiedAssessmentsDraftNetwork(currentSchoolIdPref.orEmpty())
                        }
                    )
                }
            }
        }
    }
}

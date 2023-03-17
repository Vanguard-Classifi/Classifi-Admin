package com.vanguard.classifiadmin.ui.screens.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.SubjectModel
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.domain.helpers.generateColorFromClassName
import com.vanguard.classifiadmin.ui.components.ChildTopBarWithOptions
import com.vanguard.classifiadmin.ui.components.ClassFilterManageButton
import com.vanguard.classifiadmin.ui.components.LoadingScreen
import com.vanguard.classifiadmin.ui.components.MessageBar
import com.vanguard.classifiadmin.ui.components.NoDataScreen
import com.vanguard.classifiadmin.ui.components.StagedItemIcon
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val MANAGE_SUBJECT_ADMIN_SCREEN = "manage_subject_admin_screen"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ManageSubjectAdminScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: MainViewModel,
    onAddSubject: () -> Unit,
    onManageSubjectDetail: (SubjectModel) -> Unit,
) {
    val message by viewModel.manageSubjectMessage.collectAsState()
    var optionState by remember { mutableStateOf(false) }
    val verifiedSubjectsNetwork by viewModel.verifiedSubjectsNetwork.collectAsState()
    val scope = rememberCoroutineScope()
    val selectedSubjects by viewModel.selectedSubjectManageSubjectAdminBuffer.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val subjectByIdNetwork by viewModel.subjectByIdNetwork.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentSchoolIdPref()
        viewModel.getVerifiedSubjectsNetwork(currentSchoolIdPref.orEmpty())
    }


    LaunchedEffect(message) {
        if (message !is ManageSubjectMessage.NoMessage) {
            delay(3000)
            viewModel.onManageSubjectMessageChanged(ManageSubjectMessage.NoMessage)
        }
    }

    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        Scaffold(
            modifier = Modifier,
            topBar = {
                ChildTopBarWithOptions(
                    onBack = onBack,
                    heading = stringResource(id = R.string.manage_subjects),
                    elevation = 0.dp,
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                    iconTint = MaterialTheme.colors.onPrimary,
                    onOptions = {
                         optionState = !optionState
                    },
                )
            },
            content = {
                ManageSubjectAdminScreenContent(
                    modifier = modifier.padding(it),
                    viewModel = viewModel,
                    onBack = onBack,
                    maxHeight = maxHeight,
                    onAddSubject = onAddSubject,
                    onManageSubjectDetail = onManageSubjectDetail,
                )
            }
        )

        //message
        if (message !is ManageSubjectMessage.NoMessage) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                MessageBar(
                    message = message.message,
                    icon = R.drawable.icon_info,
                    onClose = {
                        viewModel.onManageSubjectMessageChanged(ManageSubjectMessage.NoMessage)
                    },
                    maxWidth = maxWidth,
                    modifier = modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                )
            }
        }

        AnimatedVisibility(
            visible = optionState,
            enter = scaleIn(
                initialScale = 0.8f, animationSpec = tween(
                    durationMillis = 50, easing = FastOutLinearInEasing
                )
            ),
            exit = scaleOut(
                targetScale = 0.8f,
                animationSpec = tween(
                    durationMillis = 50, easing = FastOutLinearInEasing
                ),
            ),
        ) {
            Popup(alignment = Alignment.TopEnd,
                offset = IntOffset(0, 100),
                onDismissRequest = { optionState = false }) {
                ManageSubjectPopupOptionsScreen(
                    onSelectOption = {
                        when (it) {
                            ManageSubjectPopupOption.SelectAll -> {
                                //select all classes
                                if (verifiedSubjectsNetwork.data?.isNotEmpty() == true) {
                                    verifiedSubjectsNetwork.data?.map { subject ->
                                        viewModel.onAddToSelectedSubjectManageSubjectBuffer(subject.subjectId.orEmpty())
                                    }
                                }
                            }

                            ManageSubjectPopupOption.Delete -> {
                                //delete selected
                                scope.launch {
                                    if (selectedSubjects.isNotEmpty()) {
                                        selectedSubjects.map { subjectId ->
                                            // get class by code and delete
                                            viewModel.getSubjectByIdNetwork(
                                                subjectId,
                                                currentSchoolIdPref.orEmpty()
                                            )
                                            delay(1000)
                                            if (subjectByIdNetwork.data != null)
                                                viewModel.deleteSubjectNetwork(subjectByIdNetwork.data!!) {
                                                }
                                        }
                                        viewModel.onDecSubjectSelectionListenerManageSubject()
                                        viewModel.clearSelectedSubjectManageSubjectBuffer()
                                    } else {
                                        viewModel.onManageSubjectMessageChanged(ManageSubjectMessage.NoItemSelected())
                                    }
                                }
                            }
                        }
                        optionState = false
                    },
                )
            }
        }
    }
}


@Composable
fun ManageSubjectAdminScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    maxHeight: Dp,
    onAddSubject: () -> Unit,
    onManageSubjectDetail: (SubjectModel) -> Unit,
) {
    val TAG = "ManageSubjectAdminScreenContent"
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val verifiedSubjectsNetwork by viewModel.verifiedSubjectsNetwork.collectAsState()
    val selectedSubjects by viewModel.selectedSubjectManageSubjectAdminBuffer.collectAsState()
    val subjectSelectionListener by viewModel.subjectSelectionListenerManageSubject.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.clearSelectedSubjectManageSubjectBuffer()
        viewModel.getCurrentSchoolIdPref()
        viewModel.getVerifiedSubjectsNetwork(currentSchoolIdPref.orEmpty())
    }

    LaunchedEffect(subjectSelectionListener) {
        viewModel.getVerifiedSubjectsNetwork(currentSchoolIdPref.orEmpty())
    }


    Column(
        modifier = modifier
    ) {
        Card(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .padding(top = 8.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 2.dp,
        ) {
            when (verifiedSubjectsNetwork) {
                is Resource.Loading -> {
                    LoadingScreen(maxHeight = maxHeight)
                }

                is Resource.Success -> {
                    if (verifiedSubjectsNetwork.data?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.Center,
                            state = rememberLazyListState(),
                        ) {
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            items(verifiedSubjectsNetwork.data!!) { each ->
                                SubjectListItemAdmin(
                                    subject = each.toLocal(),
                                    selected = selectedSubjects.contains(each.subjectId),
                                    onManageClass = {
                                        //go to subject detail
                                        viewModel.onSelectedSubjectManageSubjectAdminChanged(it)
                                        onManageSubjectDetail(it)
                                    },
                                    onHold = {
                                        viewModel.onAddToSelectedSubjectManageSubjectBuffer(it.subjectId)
                                        viewModel.onIncSubjectSelectionListenerManageSubject()
                                    },
                                    onTap = {
                                        if (selectedSubjects.isNotEmpty()) {
                                            if (selectedSubjects.contains(it.subjectId)) {
                                                viewModel.onRemoveFromSelectedSubjectManageSubjectBuffer(
                                                    it.subjectId
                                                )
                                            } else {
                                                viewModel.onAddToSelectedSubjectManageSubjectBuffer(it.subjectId)
                                            }
                                        } else {
                                            viewModel.onManageSubjectMessageChanged(
                                                ManageSubjectMessage.HoldToMark()
                                            )
                                        }
                                        viewModel.onIncSubjectSelectionListenerManageSubject()
                                    }
                                )
                            }
                        }
                    } else {
                        NoDataScreen(
                            maxHeight = maxHeight,
                            message = stringResource(id = R.string.subjects_not_available),
                            buttonLabel = stringResource(id = R.string.add_a_subject),
                            onClick = onAddSubject,
                        )
                    }
                }

                is Resource.Error -> {
                    NoDataScreen(
                        maxHeight = maxHeight,
                        message = stringResource(id = R.string.error_loading_subjects),
                        buttonLabel = stringResource(id = R.string.go_back),
                        onClick = onBack
                    )
                }
            }
        }
    }
}


@Composable
fun SubjectListItemAdmin(
    modifier: Modifier = Modifier,
    subject: SubjectModel,
    selected: Boolean = false,
    onManageClass: (SubjectModel) -> Unit,
    onHold: (SubjectModel) -> Unit,
    onTap: (SubjectModel) -> Unit,
) {
    val constraints = SubjectListItemAdminConstraints(8.dp)
    val innerModifier = Modifier
    val maxWidth = remember { mutableStateOf(0) }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .onGloballyPositioned { maxWidth.value = it.size.width }
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onHold(subject)
                    },
                    onTap = {
                        onTap(subject)
                    }
                )
            }
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.primary.copy(
                0.1f
            )
        )
    ) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            constraintSet = constraints
        ) {
            StagedItemIcon(
                icon = R.drawable.icon_subject,
                modifier = innerModifier.layoutId("icon"),
                color = Color(generateColorFromClassName(subject.subjectName ?: "")),
            )

            Text(
                modifier = innerModifier
                    .layoutId("subjectName")
                    .widthIn(
                        max = with(LocalDensity.current) {
                            maxWidth.value
                                .toDp()
                                .times(0.4f)
                        }
                    ),
                text = subject.subjectName.orEmpty(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color(generateColorFromClassName(subject.subjectName.orEmpty()))
            )

            Text(
                modifier = innerModifier
                    .layoutId("code")
                    .widthIn(
                        max = with(LocalDensity.current) {
                            maxWidth.value
                                .toDp()
                                .times(0.4f)
                        }
                    ),
                text = subject.subjectCode.orEmpty(),
                fontSize = 12.sp,
                color = Black100.copy(0.8f)
            )


            ClassFilterManageButton(
                modifier = innerModifier.layoutId("manage"),
                icon = R.drawable.icon_settings,
                label = stringResource(id = R.string.manage),
                className = subject.subjectName ?: "",
                onSelect = { onManageClass(subject) },
            )
        }
    }
}


private fun SubjectListItemAdminConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val icon = createRefFor("icon")
        val subjectName = createRefFor("subjectName")
        val code = createRefFor("code")
        val manage = createRefFor("manage")

        constrain(icon) {
            start.linkTo(parent.start, margin = margin)
            top.linkTo(subjectName.top, margin = 0.dp)
        }

        constrain(subjectName) {
            top.linkTo(parent.top, margin = margin)
            start.linkTo(icon.end, margin = 8.dp)
        }

        constrain(code) {
            top.linkTo(subjectName.bottom, margin = 4.dp)
            start.linkTo(subjectName.start, margin = 0.dp)
        }

        constrain(manage) {
            top.linkTo(subjectName.top, margin = 0.dp)
            bottom.linkTo(code.bottom, margin = 0.dp)
            end.linkTo(parent.end, margin = margin)
        }
    }
}


@Composable
fun ManageSubjectPopupOptionsScreen(
    modifier: Modifier = Modifier,
    onSelectOption: (ManageSubjectPopupOption) -> Unit,
    options: List<ManageSubjectPopupOption> = ManageSubjectPopupOption.values().toList(),
) {
    Card(
        modifier = modifier.padding(start = 92.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.onPrimary
    ) {
        Column(modifier = modifier) {
            Spacer(modifier = modifier.height(16.dp))
            options.forEach { ManageSubjectOptionItem(option = it, onSelect = onSelectOption) }
            Spacer(modifier = modifier.height(16.dp))
        }
    }
}


@Composable
fun ManageSubjectOptionItem(
    modifier: Modifier = Modifier,
    option: ManageSubjectPopupOption,
    onSelect: (ManageSubjectPopupOption) -> Unit,
) {
    Surface(
        modifier = modifier
            .clickable { onSelect(option) }
            .clip(RoundedCornerShape(2.dp)),
        shape = RoundedCornerShape(2.dp),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = option.title,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}


sealed class ManageSubjectMessage(val message: String) {
    class HoldToMark() : ManageSubjectMessage("Please hold to mark item")
    class NoItemSelected() : ManageSubjectMessage("No item selected")
    object NoMessage : ManageSubjectMessage("")
}

enum class ManageSubjectPopupOption(val title: String) {
    SelectAll("Select All"),
    Delete("Delete")
}


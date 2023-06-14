package com.khalidtouch.chatme.admin.teachers.details

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.khalidtouch.chatme.admin.R
import com.khalidtouch.chatme.admin.teachers.TeacherScreenViewModel
import com.khalidtouch.classifiadmin.model.classifi.ClassifiClass
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import com.khalidtouch.core.common.extensions.ifNullOrBlank
import com.khalidtouch.core.common.extensions.isEqualToDefaultImageUrl
import com.khalidtouch.core.common.extensions.orDefaultImageUrl
import com.khalidtouch.core.designsystem.components.ClassifiIconButton
import com.khalidtouch.core.designsystem.components.ClassifiMenu
import com.khalidtouch.core.designsystem.components.ClassifiSimpleTopAppBar
import com.khalidtouch.core.designsystem.components.ClassifiTextButton
import com.khalidtouch.core.designsystem.components.MenuItem
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import com.khalidtouch.core.designsystem.theme.LocalContentAlpha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDetailScreen(
    teacherDetailViewModel: TeacherDetailViewModel,
    teacherScreenViewModel: TeacherScreenViewModel,
    onBackPressed: () -> Unit,
) {
    val uiState by teacherDetailViewModel.uiState.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val mySchoolId by teacherDetailViewModel.mySchoolId.collectAsStateWithLifecycle()


    when(uiState) {
        is TeacherDetailUiState.Loading -> Unit
        is TeacherDetailUiState.Success -> {
            if (!(uiState as TeacherDetailUiState.Success).data.shouldShowExpandedImage) {
                Scaffold(
                    topBar = {
                        ClassifiSimpleTopAppBar(
                            title = {
                                val headerStyle = MaterialTheme.typography.titleMedium
                                Box(Modifier.padding(start = 16.dp)) {
                                    ProvideTextStyle(headerStyle) {
                                        Text(
                                            text = stringResource(id = R.string.teacher_detail)
                                        )
                                    }
                                }
                            },
                            navIcon = {
                                Box(modifier = Modifier) {
                                    IconButton(
                                        onClick = onBackPressed,
                                        enabled = true,
                                    ) {
                                        Icon(
                                            painter = painterResource(id = ClassifiIcons.Back),
                                            contentDescription = stringResource(id = R.string.go_back)
                                        )
                                    }
                                }
                            },
                            actions = {
                                Box(modifier = Modifier) {
                                    IconButton(
                                        onClick = { teacherDetailViewModel.updateTeacherMenuState(true) },
                                        enabled = true,
                                    ) {
                                        Icon(
                                            painter = painterResource(id = ClassifiIcons.OptionsVertical),
                                            contentDescription = stringResource(id = R.string.options)
                                        )
                                    }
                                }
                            }
                        )
                    },
                    content = {
                        TeacherDetailScreen(
                            modifier = Modifier.padding(it),
                            teacherDetailViewModel = teacherDetailViewModel,
                            teacherScreenViewModel = teacherScreenViewModel,
                        )
                    }
                )
            }
        }
    }

    when(uiState) {
        is TeacherDetailUiState.Loading -> Unit
        is TeacherDetailUiState.Success -> {
            val teacher = (uiState as TeacherDetailUiState.Success).data.teacher

            if ((uiState as TeacherDetailUiState.Success).data.shouldShowExpandedImage) {
                BoxWithConstraints(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center,
                ) {
                    val maxWidth = maxWidth
                    val profileImage = teacher?.profile?.profileImage

                    when (profileImage?.isEqualToDefaultImageUrl() ?: true) {
                        true -> {
                            Icon(
                                painterResource(id = ClassifiIcons.Personal),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline.copy(0.7f),
                                modifier = Modifier
                                    .height(maxWidth)
                                    .width(maxWidth)
                                    .padding(16.dp)
                            )
                        }

                        false -> {
                            val profileImageUri = Uri.parse(profileImage)
                            AsyncImage(
                                modifier = Modifier
                                    .height(maxWidth)
                                    .width(maxWidth)
                                    .padding(16.dp)
                                    .clip(CircleShape),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(profileImageUri).build(),
                                contentDescription = null,
                                alpha = LocalContentAlpha.current.alpha,
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }

                    Box(
                        Modifier
                            .matchParentSize()
                            .padding(top = 22.dp, end = 16.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        ClassifiIconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.outline.copy(0.7f)
                            ),
                            onClick = { teacherDetailViewModel.updateExpandedImageState(false) },
                            icon = {
                                Icon(
                                    painterResource(id = ClassifiIcons.Close),
                                    contentDescription = null,
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    //delete dialog
    when (uiState) {
        is TeacherDetailUiState.Loading -> Unit
        is TeacherDetailUiState.Success -> {
            val onDismissDeleteDialog: () -> Unit =
                { teacherDetailViewModel.updateDeleteDialogState(false) }
            val localModifier = Modifier
            val email = (uiState as TeacherDetailUiState.Success).data.email
            val canBeDeleted = (uiState as TeacherDetailUiState.Success).data.canBeDeleted
            val teacher = (uiState as TeacherDetailUiState.Success).data.teacher
            val shouldShowDeleteDialog =
                (uiState as TeacherDetailUiState.Success).data.shouldShowDeleteDialog

            if (shouldShowDeleteDialog) {
                AlertDialog(
                    properties = DialogProperties(usePlatformDefaultWidth = false),
                    modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
                    onDismissRequest = { onDismissDeleteDialog() },
                    title = {
                        Text(
                            text = stringResource(id = R.string.delete_contact),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    },
                    text = {
                        Column {
                            Text(
                                text = stringResource(id = R.string.type_your_email_to_proceed),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.outline.copy(
                                        LocalContentAlpha.current.alpha
                                    )
                                )
                            )
                            Spacer(Modifier.height(12.dp))
                            OutlinedTextField(
                                modifier = localModifier.fillMaxWidth(),
                                value = email,
                                onValueChange = teacherDetailViewModel::onEmailChanged,
                                placeholder = {
                                    Text(
                                        text = teacher?.account?.email.orEmpty(),
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = MaterialTheme.colorScheme.outline.copy(
                                                LocalContentAlpha.current.alpha
                                            )
                                        )
                                    )
                                },
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.outline
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.outline,
                                    unfocusedTextColor = MaterialTheme.colorScheme.outline,
                                    focusedBorderColor = MaterialTheme.colorScheme.outline,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                    cursorColor = MaterialTheme.colorScheme.outline,
                                ),
                                singleLine = true,
                                maxLines = 1,
                                shape = RoundedCornerShape(8.dp),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done
                                )
                            )
                        }
                    },
                    dismissButton = {
                        ClassifiTextButton(
                            onClick = { onDismissDeleteDialog() },
                            enabled = true,
                            content = {
                                Text(
                                    text = stringResource(id = R.string.cancel),
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            }
                        )
                    },
                    confirmButton = {
                        ClassifiTextButton(
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.error,
                                disabledContentColor = MaterialTheme.colorScheme.outline.copy(0.5f),
                                containerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                            ),
                            onClick = {
                                teacherDetailViewModel.unregisterParentFromSchool(
                                    teacherId = teacher?.userId ?: -1L,
                                    schoolId = mySchoolId,
                                )
                                onDismissDeleteDialog()
                                onBackPressed()
                            },
                            enabled = canBeDeleted,
                            content = {
                                Text(
                                    text = stringResource(id = R.string.delete),
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            }
                        )
                    },
                    tonalElevation = 2.dp,
                    shape = RoundedCornerShape(16.dp),
                )
            }
        }
    }

    //menu
    when (uiState) {
        is TeacherDetailUiState.Loading -> Unit
        is TeacherDetailUiState.Success -> {
            val shouldShowTeacherMenu =
                (uiState as TeacherDetailUiState.Success).data.shouldShowTeacherMenu

            if (shouldShowTeacherMenu) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
                    val localModifier = Modifier
                    Popup(
                        alignment = Alignment.TopCenter,
                        offset = IntOffset(0, 20),
                        onDismissRequest = { teacherDetailViewModel.updateTeacherMenuState(false) }
                    ) {
                        ClassifiMenu(
                            modifier = localModifier.width(configuration.screenWidthDp.dp - 48.dp),
                            content = {
                                TeacherMenu.values().map { menu ->
                                    MenuItem(
                                        modifier = localModifier,
                                        onClick = {
                                            when (menu) {
                                                TeacherMenu.Delete -> {
                                                    teacherDetailViewModel.updateTeacherMenuState(
                                                        false
                                                    )
                                                    teacherDetailViewModel.updateDeleteDialogState(
                                                        true
                                                    )
                                                }

                                                TeacherMenu.AssignToClass -> {
                                                    /*todo -> assign to class */
                                                    teacherDetailViewModel.updateTeacherMenuState(
                                                        false
                                                    )
                                                }
                                            }

                                        },
                                        icon = {
                                            Icon(
                                                painterResource(id = menu.icon),
                                                contentDescription = menu.title,
                                                tint = MaterialTheme.colorScheme.outline.copy(
                                                    LocalContentAlpha.current.alpha
                                                )
                                            )
                                        },
                                        text = {
                                            Text(
                                                text = menu.title,
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    color = MaterialTheme.colorScheme.outline.copy(
                                                        LocalContentAlpha.current.alpha
                                                    )
                                                ),
                                            )
                                        }
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    BackHandler(enabled = true) {
        when (uiState) {
            is TeacherDetailUiState.Loading -> Unit
            is TeacherDetailUiState.Success -> {
                val shouldShowExpandedImage =
                    (uiState as TeacherDetailUiState.Success).data.shouldShowExpandedImage
                if (shouldShowExpandedImage) {
                    teacherDetailViewModel.updateExpandedImageState(false)
                } else {
                    onBackPressed()
                }
            }
        }
    }
}


@Composable
private fun TeacherDetailScreen(
    modifier: Modifier = Modifier,
    teacherDetailViewModel: TeacherDetailViewModel,
    teacherScreenViewModel: TeacherScreenViewModel,
) {
    val context = LocalContext.current
    val teacherForDetailId by teacherScreenViewModel.teacherForDetailId.collectAsStateWithLifecycle()
    val uiState by teacherDetailViewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(teacherForDetailId) {
        teacherDetailViewModel.loadTeacherInfo(teacherForDetailId)
        onDispose {}
    }

    when (uiState) {
        is TeacherDetailUiState.Loading -> Unit
        is TeacherDetailUiState.Success -> {
            val teacher = (uiState as TeacherDetailUiState.Success).data.teacher

            LazyColumn(
                contentPadding = PaddingValues(12.dp),
                modifier = Modifier.padding(top = 46.dp)
            ) {
                headerItem(
                    profileImage = teacher?.profile?.profileImage.orDefaultImageUrl(),
                    isProfileImageDefault = teacher?.profile?.profileImage?.isEqualToDefaultImageUrl()
                        ?: true,
                    onExpand = { teacherDetailViewModel.updateExpandedImageState(true) },
                )

                infoItem(
                    username = teacher?.account?.username.ifNullOrBlank(context.getString(R.string.name_not_specified)),
                    email = teacher?.account?.email.ifNullOrBlank(context.getString(R.string.email_not_added)),
                    bio = teacher?.profile?.bio.ifNullOrBlank(context.getString(R.string.empty_bio)),
                )

                assignedClassesItem(
                    assignedClasses = teacher?.joinedClasses ?: listOf(),
                )
                item {
                    Spacer(Modifier.height(32.dp))
                }
            }
        }
    }
}


private fun LazyListScope.headerItem(
    profileImage: String,
    isProfileImageDefault: Boolean,
    onExpand: (String) -> Unit
) {
    item {
        Spacer(Modifier.height(16.dp))
        Box(
            Modifier
                .padding(32.dp)
                .fillMaxWidth()
                .clickable(
                    enabled = true,
                    onClick = { onExpand(profileImage) },
                    role = Role.Button
                ),
            contentAlignment = Alignment.Center
        ) {
            when (isProfileImageDefault) {
                true -> {
                    Icon(
                        painterResource(id = ClassifiIcons.Personal),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline.copy(LocalContentAlpha.current.alpha),
                        modifier = Modifier
                            .height(78.dp)
                            .width(78.dp)
                    )
                }

                false -> {
                    val profileImageUri = Uri.parse(profileImage)
                    AsyncImage(
                        modifier = Modifier
                            .height(78.dp)
                            .width(78.dp)
                            .clip(CircleShape),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(profileImageUri).build(),
                        contentDescription = null,
                        alpha = LocalContentAlpha.current.alpha,
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
    }
}


fun LazyListScope.infoItem(
    username: String,
    email: String,
    bio: String,
) {
    item {
        Text(
            text = username,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.outline.copy(LocalContentAlpha.current.alpha)
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = email,
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.outline.copy(LocalContentAlpha.current.alpha)
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = bio,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.outline.copy(LocalContentAlpha.current.alpha)
            ),
        )
    }
}


@OptIn(ExperimentalLayoutApi::class)
private fun LazyListScope.assignedClassesItem(
    assignedClasses: List<ClassifiClass>,
) {
    item {
        FlowRow(Modifier.fillMaxWidth()) {
            assignedClasses.map { item ->
                Text(
                    text = item.className.ifNullOrBlank(stringResource(id = R.string.class_name_not_set)),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.outline.copy(LocalContentAlpha.current.alpha)
                    ),
                )
            }
        }
    }
}


enum class TeacherMenu(val title: String, val icon: Int) {
    AssignToClass("Assign class", ClassifiIcons.Classroom),
    Delete("Delete", ClassifiIcons.Delete),
}
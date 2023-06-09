package com.khalidtouch.chatme.admin.teachers.details

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.khalidtouch.chatme.admin.R
import com.khalidtouch.chatme.admin.teachers.TeacherScreenViewModel
import com.khalidtouch.classifiadmin.model.classifi.ClassifiClass
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import com.khalidtouch.core.common.extensions.ifNullOrBlank
import com.khalidtouch.core.common.extensions.isEqualToDefaultImageUrl
import com.khalidtouch.core.common.extensions.orDefaultImageUrl
import com.khalidtouch.core.designsystem.components.ClassifiIconButton
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import com.khalidtouch.core.designsystem.theme.ContentAlpha
import com.khalidtouch.core.designsystem.theme.LocalContentAlpha

@Composable
fun TeacherDetailScreen(
    teacherDetailViewModel: TeacherDetailViewModel,
    teacherScreenViewModel: TeacherScreenViewModel,
    onBackPressed: () -> Unit,
) {
    val uiState by teacherDetailViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {},
        content = {
            TeacherDetailScreen(
                modifier = Modifier.padding(it),
                teacherDetailViewModel = teacherDetailViewModel,
                teacherScreenViewModel = teacherScreenViewModel,
            )
        }
    )

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
                contentPadding = PaddingValues(12.dp)
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
}


fun LazyListScope.headerItem(
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


fun LazyListScope.assignedStudentsItem(
    students: LazyPagingItems<ClassifiUser>,
    onClick: (String) -> Unit,
    isProfileImageDefault: Boolean,
) {
    item {
        var topPadding by remember { mutableStateOf(0) }
        Box {
            LazyRow(
                Modifier.padding(
                    top = 8.dp + with(LocalDensity.current) {
                        topPadding.toDp()
                    },
                )
            ) {
                items(students) { student ->
                    StudentRowItem(
                        profileImage = student?.profile?.profileImage.orDefaultImageUrl(),
                        onClick = onClick,
                        isProfileImageDefault = isProfileImageDefault,
                    )
                }
            }

            Box(Modifier.matchParentSize(), contentAlignment = Alignment.TopEnd) {
                Text(
                    text = stringResource(id = R.string.see_more),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.outline.copy(LocalContentAlpha.current.alpha)
                    ),
                    modifier = Modifier.onGloballyPositioned { topPadding = it.size.height }
                )
            }
        }
    }
}


@Composable
private fun StudentRowItem(
    profileImage: String,
    onClick: (String) -> Unit,
    isProfileImageDefault: Boolean,
) {
    Box(Modifier.clickable(
        enabled = true,
        onClick = { onClick(profileImage) }
    )) {
        when (isProfileImageDefault) {
            true -> {
                Icon(
                    painterResource(id = ClassifiIcons.Personal),
                    contentDescription = null,
                    modifier = Modifier
                        .height(42.dp)
                        .width(42.dp),
                    tint = MaterialTheme.colorScheme.outline.copy(LocalContentAlpha.current.alpha)
                )
            }

            false -> {
                val profileImageUri = Uri.parse(profileImage)
                AsyncImage(
                    modifier = Modifier
                        .height(42.dp)
                        .width(42.dp)
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

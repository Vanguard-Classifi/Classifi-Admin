package com.khalidtouch.chatme.admin.parents.details

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import com.khalidtouch.chatme.admin.parents.ParentScreenViewModel
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
fun ParentDetailScreen(
    parentScreenViewModel: ParentScreenViewModel,
    parentDetailViewModel: ParentDetailViewModel,
    onBackPressed: () -> Unit,
) {
    val uiState by parentDetailViewModel.uiState.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val mySchoolId by parentDetailViewModel.mySchoolId.collectAsStateWithLifecycle()


    when(uiState) {
        is ParentDetailUiState.Loading -> Unit
        is ParentDetailUiState.Success -> {
            if(!(uiState as ParentDetailUiState.Success).data.shouldShowExpandedImage) {
                Scaffold(
                    topBar = {
                        ClassifiSimpleTopAppBar(
                            title = {
                                val headerStyle = MaterialTheme.typography.titleMedium
                                Box(Modifier.padding(start = 16.dp)) {
                                    ProvideTextStyle(headerStyle) {
                                        Text(
                                            text = stringResource(id = R.string.parent_detail)
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
                                        onClick = { parentDetailViewModel.updateParentMenuState(true) },
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
                        ParentDetailScreen(
                            modifier = Modifier.padding(it),
                            parentScreenViewModel = parentScreenViewModel,
                            parentDetailViewModel = parentDetailViewModel,
                        )
                    }
                )

            }
        }
    }

    //expanded view
    when (uiState) {
        is ParentDetailUiState.Loading -> Unit
        is ParentDetailUiState.Success -> {
            val parent = (uiState as ParentDetailUiState.Success).data.parent

            if ((uiState as ParentDetailUiState.Success).data.shouldShowExpandedImage) {
                BoxWithConstraints(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center,
                ) {
                    val maxWidth = maxWidth
                    val profileImage = parent?.profile?.profileImage

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
                            onClick = { parentDetailViewModel.updateExpandedImageState(false) },
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
        is ParentDetailUiState.Loading -> Unit
        is ParentDetailUiState.Success -> {
            val onDismissDeleteDialog: () -> Unit =
                { parentDetailViewModel.updateDeleteDialogState(false) }
            val localModifier = Modifier
            val email = (uiState as ParentDetailUiState.Success).data.email
            val canBeDeleted = (uiState as ParentDetailUiState.Success).data.canBeDeleted
            val parent = (uiState as ParentDetailUiState.Success).data.parent
            val shouldShowDeleteDialog =
                (uiState as ParentDetailUiState.Success).data.shouldShowDeleteDialog

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
                                onValueChange = parentDetailViewModel::onEmailChanged,
                                placeholder = {
                                    Text(
                                        text = parent?.account?.email.orEmpty(),
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
                                disabledContentColor = MaterialTheme.colorScheme.outline.copy(
                                    0.5f
                                ),
                                containerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                            ),
                            onClick = {
                                parentDetailViewModel.unregisterParentFromSchool(
                                    parentId = parent?.userId ?: -1L,
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
        is ParentDetailUiState.Loading -> Unit
        is ParentDetailUiState.Success -> {
            val shouldShowParentMenu =
                (uiState as ParentDetailUiState.Success).data.shouldShowParentMenu

            if (shouldShowParentMenu) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
                    val localModifier = Modifier
                    Popup(
                        alignment = Alignment.TopCenter,
                        offset = IntOffset(0, 20),
                        onDismissRequest = { parentDetailViewModel.updateParentMenuState(false) }
                    ) {
                        ClassifiMenu(
                            modifier = localModifier.width(configuration.screenWidthDp.dp - 48.dp),
                            content = {
                                ParentMenu.values().map { menu ->
                                    MenuItem(
                                        modifier = localModifier,
                                        onClick = {
                                            when (menu) {
                                                ParentMenu.Delete -> {
                                                    parentDetailViewModel.updateParentMenuState(
                                                        false
                                                    )
                                                    parentDetailViewModel.updateDeleteDialogState(
                                                        true
                                                    )
                                                }

                                                ParentMenu.JoinWithWard -> {
                                                    /*todo -> assign to class */
                                                    parentDetailViewModel.updateParentMenuState(
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
            is ParentDetailUiState.Loading -> Unit
            is ParentDetailUiState.Success -> {
                val shouldShowExpandedImage =
                    (uiState as ParentDetailUiState.Success).data.shouldShowExpandedImage
                if (shouldShowExpandedImage) {
                    parentDetailViewModel.updateExpandedImageState(false)
                } else {
                    onBackPressed()
                }
            }
        }
    }
}


@Composable
private fun ParentDetailScreen(
    modifier: Modifier = Modifier,
    parentScreenViewModel: ParentScreenViewModel,
    parentDetailViewModel: ParentDetailViewModel,
) {
    val context = LocalContext.current
    val parentForDetailId by parentScreenViewModel.parentForDetailId.collectAsStateWithLifecycle()
    val uiState by parentDetailViewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(parentForDetailId) {
        parentDetailViewModel.loadParentInfo(parentForDetailId)
        onDispose {}
    }

    when (uiState) {
        is ParentDetailUiState.Loading -> Unit
        is ParentDetailUiState.Success -> {
            val parent = (uiState as ParentDetailUiState.Success).data.parent

            LazyColumn(
                contentPadding = PaddingValues(12.dp),
                modifier = Modifier.padding(top = 46.dp)
            ) {
                headerItem(
                    profileImage = parent?.profile?.profileImage.orDefaultImageUrl(),
                    isProfileImageDefault = parent?.profile?.profileImage?.isEqualToDefaultImageUrl()
                        ?: true,
                    onExpand = { parentDetailViewModel.updateExpandedImageState(true) },
                )

                infoItem(
                    username = parent?.account?.username.ifNullOrBlank(context.getString(R.string.name_not_specified)),
                    email = parent?.account?.email.ifNullOrBlank(context.getString(R.string.email_not_added)),
                    bio = parent?.profile?.bio.ifNullOrBlank(context.getString(R.string.empty_bio)),
                )


//                assignedStudentsItem(
//                    students = ,
//                    onClick = {},
//                )

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


private fun LazyListScope.infoItem(
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
                        isProfileImageDefault = student?.profile?.profileImage?.isEqualToDefaultImageUrl()
                            ?: true,
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

enum class ParentMenu(val title: String, val icon: Int) {
    JoinWithWard("Join with ward", ClassifiIcons.Personal),
    Delete("Delete", ClassifiIcons.Delete)
}
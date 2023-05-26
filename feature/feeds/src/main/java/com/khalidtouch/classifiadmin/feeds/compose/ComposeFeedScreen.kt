package com.khalidtouch.classifiadmin.feeds.compose

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.khalidtouch.classifiadmin.feeds.R
import com.khalidtouch.classifiadmin.model.FeedMessage
import com.khalidtouch.classifiadmin.model.MessageType
import com.khalidtouch.core.designsystem.components.ClassifiButton
import com.khalidtouch.core.designsystem.components.ClassifiComposeFeedBottomBar
import com.khalidtouch.core.designsystem.components.ClassifiIconButton
import com.khalidtouch.core.designsystem.components.ClassifiSimpleTopAppBar
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ComposeFeedRoute(
    composeFeedViewModel: ComposeFeedViewModel = hiltViewModel<ComposeFeedViewModel>(),
    onCloseComposeFeedScreen: () -> Unit,
    onTakePhoto: () -> Unit,
    onChooseImage: () -> Unit,
) {
    val TAG = "ComposeFeed"
    val uiState by composeFeedViewModel.uiState.collectAsStateWithLifecycle()

    ComposeFeedScreen(
        uiState = uiState,
        onCloseComposeFeedScreen = onCloseComposeFeedScreen,
        onPostFeed = { /*TODO*/ },
        onChooseImage = onChooseImage,
        feedScope = "All classes",
        schoolName = "Future Leaders International School",
        onTitleValueChange = composeFeedViewModel::onTitleValueChange,
        onContentValueChange = composeFeedViewModel::onContentValueChange,
        clearBottomSheetSelection = composeFeedViewModel::clearBottomSheetSelection,
        openAttachmentsBottomSheet = {
            composeFeedViewModel.onComposeFeedBottomSheetSelectionChange(
                ComposeFeedBottomSheetSelection.Attachments
            )
        },
        onTakePhoto = onTakePhoto,
        onDeleteMediaMessage = composeFeedViewModel::onDeleteMediaMessage,
        enqueueMediaMessage = composeFeedViewModel::enqueueMediaMessage
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
private fun ComposeFeedScreen(
    onCloseComposeFeedScreen: () -> Unit,
    onPostFeed: () -> Unit,
    onChooseImage: () -> Unit,
    uiState: ComposeFeedUiState,
    feedScope: String,
    schoolName: String,
    onTitleValueChange: (String) -> Unit,
    onContentValueChange: (String) -> Unit,
    clearBottomSheetSelection: () -> Unit,
    onDeleteMediaMessage: (FeedMessage) -> Unit,
    openAttachmentsBottomSheet: () -> Unit,
    onTakePhoto: () -> Unit,
    enqueueMediaMessage: () -> Unit,
) {

    val TAG = "ComposeFeed"
    var showModalBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        Log.e(TAG, "ComposeFeedScreen: has been called")
        if (uiState is ComposeFeedUiState.Success) {
            Log.e(TAG, "ComposeFeedScreen: size of messages ${uiState.data.mediaMessages.size}")
            showModalBottomSheet = if (uiState.data.isBottomSheetShown) {
                sheetState.show()
                true
            } else {
                sheetState.hide()
                false
            }
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ClassifiSimpleTopAppBar(
                title = { },
                navIcon = {
                    ClassifiIconButton(
                        onClick = onCloseComposeFeedScreen,
                        icon = {
                            Icon(
                                painter = painterResource(id = ClassifiIcons.Close),
                                contentDescription = stringResource(id = R.string.close)
                            )
                        }
                    )
                },
                actions = {
                    val buttonTextStyle = MaterialTheme.typography.labelMedium

                    Box(modifier = Modifier.padding(end = 8.dp)) {
                        ClassifiButton(
                            onClick = onPostFeed,
                            enabled = when (uiState) {
                                is ComposeFeedUiState.Loading -> false
                                is ComposeFeedUiState.Success -> {
                                    uiState.data.isFeedPostable
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(0.2f)
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (uiState) {
                                    is ComposeFeedUiState.Loading -> MaterialTheme.colorScheme.onPrimary.copy(
                                        0.2f
                                    )

                                    is ComposeFeedUiState.Success -> {
                                        if (uiState.data.isFeedPostable) {
                                            MaterialTheme.colorScheme.onPrimary
                                        } else {
                                            MaterialTheme.colorScheme.onPrimary.copy(0.2f)
                                        }
                                    }
                                },
                            ),
                            text = {
                                ProvideTextStyle(value = buttonTextStyle) {
                                    Text(
                                        text = stringResource(R.string.post)
                                    )
                                }
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {
            val composeFeedButtonColors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black,
            )

            ClassifiComposeFeedBottomBar(
                filter = {
                    Box(modifier = Modifier.padding(end = 8.dp)) {
                        ClassifiButton(
                            onClick = { /*TODO*/ },
                            enabled = true,
                            text = {
                                Text(
                                    text = feedScope,
                                    style = MaterialTheme.typography.labelMedium,
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = ClassifiIcons.CommentSolid),
                                    contentDescription = null,
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.onBackground,
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }
                },
                actions = {
                    ComposeFeedBottomBarActions.values().map { action ->
                        Box {
                            ClassifiIconButton(
                                onClick = {
                                    when (action) {
                                        ComposeFeedBottomBarActions.More -> {
                                            openAttachmentsBottomSheet()
                                        }

                                        ComposeFeedBottomBarActions.Camera -> {
                                            onTakePhoto()
                                        }

                                        ComposeFeedBottomBarActions.Video -> {

                                        }

                                        ComposeFeedBottomBarActions.Photo -> {
                                            onChooseImage()
                                        }
                                    }
                                },
                                colors = composeFeedButtonColors,
                                icon = {
                                    Icon(
                                        painter = painterResource(id = action.icon),
                                        contentDescription = action.name,
                                    )
                                }
                            )
                        }
                    }
                }
            )
        },
        content = {
            ComposeFeedBody(
                modifier = Modifier.padding(it),
                uiState = uiState,
                schoolName = schoolName,
                onTitleValueChange = onTitleValueChange,
                onContentValueChange = onContentValueChange,
                onDeleteMediaMessage = onDeleteMediaMessage,
            )
        }
    )

    if (showModalBottomSheet)
        ModalBottomSheet(
            onDismissRequest = {
                clearBottomSheetSelection()
            },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shape = RoundedCornerShape(
                topStartPercent = 5,
                topEndPercent = 5
            ),
            tonalElevation = 2.dp,
        ) {
            when (uiState) {
                is ComposeFeedUiState.Loading -> Unit
                is ComposeFeedUiState.Success -> {
                    when (uiState.data.bottomSheetState) {
                        is ComposeFeedBottomSheetSelection.Attachments -> {
                            ComposeFeedMainActions.values().map {
                                ActionListItem(
                                    icon = {
                                        Icon(
                                            painter = painterResource(id = it.icon),
                                            contentDescription = it.title
                                        )
                                    },
                                    text = {
                                        Text(
                                            it.title,
                                            style = MaterialTheme.typography.titleSmall,
                                        )
                                    },
                                    onClick = {
                                        scope.launch {
                                            clearBottomSheetSelection()

                                            when (it) {
                                                ComposeFeedMainActions.TakePhoto -> {
                                                    onTakePhoto()
                                                }

                                                ComposeFeedMainActions.RecordVideo -> {

                                                }

                                                ComposeFeedMainActions.ImportPhoto -> {
                                                    onChooseImage()
                                                }

                                                ComposeFeedMainActions.AddDocument -> {

                                                }
                                            }
                                        }
                                    }
                                )
                            }
                        }

                        else -> {
                            /*TODO()*/
                        }
                    }
                }

            }

        }
}


@Composable
fun ActionListItem(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(
                enabled = true,
                onClick = onClick,
                role = Role.Button,
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.padding(16.dp)) {
            icon()
        }
        Box(Modifier.weight(1f)) {
            text()
        }
    }
}

@Composable
private fun ComposeFeedBody(
    modifier: Modifier = Modifier,
    uiState: ComposeFeedUiState,
    schoolName: String,
    onTitleValueChange: (String) -> Unit,
    onDeleteMediaMessage: (FeedMessage) -> Unit,
    onContentValueChange: (String) -> Unit,
    textFieldColors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        cursorColor = MaterialTheme.colorScheme.onBackground,
    ),
) {
    val TAG = "ComposeFeed"
    val catImage = "https://www.petsworld.in/blog/wp-content/uploads/2014/09/funny-cat.jpg"
    when (uiState) {
        is ComposeFeedUiState.Loading -> Unit
        is ComposeFeedUiState.Success -> {
            ComposeFeedBody(
                modifier = modifier,
                profileImage = {
                    Box(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                                shape = CircleShape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        AsyncImage(
                            placeholder = painterResource(id = ClassifiIcons.Profile),
                            model = catImage,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(58.dp)
                                .clip(CircleShape)
                        )
                    }
                },
                author = {
                    Column(modifier = modifier.padding(end = 32.dp)) {
                        Text(
                            text = uiState.data.username,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Text(
                            text = schoolName,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                },
                title = {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.data.feedTextEntry.title,
                        onValueChange = onTitleValueChange,
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.title),
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        textStyle = MaterialTheme.typography.titleMedium,
                        colors = textFieldColors,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )

                },
                text = {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.data.feedTextEntry.content,
                        onValueChange = onContentValueChange,
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.what_you_write_about),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        colors = textFieldColors,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        )
                    )
                },
                mediaItem = {
                    mediaItem(
                        uiState.data.mediaMessages,
                        onDeleteMediaMessage = onDeleteMediaMessage,
                    )
                }
            )
        }
    }
}


@Composable
private fun ComposeFeedBody(
    modifier: Modifier = Modifier,
    profileImage: @Composable () -> Unit,
    author: @Composable () -> Unit,
    title: @Composable () -> Unit,
    text: @Composable () -> Unit,
    mediaItem: LazyListScope.() -> Unit,
) {
    val TAG = "ComposeFeed"

    LazyColumn {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Black
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier.padding(8.dp)) {
                    profileImage()
                }

                Box(modifier = Modifier.weight(1f)) {
                    author()
                }
            }
            /* title textfield */

            Spacer(modifier = Modifier.height(12.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Black
                    )
            ) { title() }

            /*divider */
            Divider(Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            /*content textfield */
            Box(
                Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Black
                    )
            ) {
                text()
            }

        }

        mediaItem()
        item {
            Spacer(modifier = Modifier.height(98.dp))
        }
    }
}


fun LazyListScope.mediaItem(
    mediaMessages: List<FeedMessage>,
    onDeleteMediaMessage: (FeedMessage) -> Unit,
) {
    item {
        mediaMessages.map { message ->
            MediaItemCard(
                mediaMessage = message,
                onDeleteMediaMessage = onDeleteMediaMessage,
            )
        }
    }
}

@Composable
fun MediaItemCard(
    mediaMessage: FeedMessage,
    onDeleteMediaMessage: (FeedMessage) -> Unit,
) {
    val uri = Uri.parse(mediaMessage.uri)
    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(
                top = 16.dp,
                start = 8.dp,
                end = 8.dp,
                bottom = 16.dp,
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Box {
            AsyncImage(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxWidth(),
                model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                    .data(uri).build(),
                contentDescription = null,
                contentScale = androidx.compose.ui.layout.ContentScale.Fit
            )

            Box(
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        end = 16.dp,
                    )
                    .matchParentSize(), contentAlignment = Alignment.TopEnd
            ) {
                ClassifiIconButton(
                    onClick = { onDeleteMediaMessage(mediaMessage) },
                    icon = {
                        Icon(
                            painter = painterResource(id = ClassifiIcons.Close),
                            contentDescription = null,
                        )
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Black.copy(0.8f),
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            }
        }
    }
}


enum class ComposeFeedBottomBarActions(val icon: Int) {
    Camera(ClassifiIcons.Camera),
    Video(ClassifiIcons.VideoCamera),
    Photo(ClassifiIcons.Image),
    More(ClassifiIcons.OptionsHorizontal)
}

enum class ComposeFeedMainActions(val title: String, val icon: Int) {
    ImportPhoto("Add a photo", ClassifiIcons.Image),
    TakePhoto("Take a photo", ClassifiIcons.Camera),
    AddDocument("Add a document", ClassifiIcons.Doc),
    RecordVideo("Record a video", ClassifiIcons.VideoCamera)
}
package com.khalidtouch.classifiadmin.feeds.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.khalidtouch.classifiadmin.feeds.R
import com.khalidtouch.core.designsystem.components.ClassifiButton
import com.khalidtouch.core.designsystem.components.ClassifiComposeFeedBottomBar
import com.khalidtouch.core.designsystem.components.ClassifiIconButton
import com.khalidtouch.core.designsystem.components.ClassifiSimpleTopAppBar
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import kotlinx.coroutines.launch


@Composable
fun ComposeFeedRoute(
    composeFeedViewModel: ComposeFeedViewModel = hiltViewModel<ComposeFeedViewModel>(),
    onCloseComposeFeedScreen: () -> Unit,
) {
    val feedTitle by composeFeedViewModel.feedTitle.collectAsStateWithLifecycle()
    val feedContent by composeFeedViewModel.feedContent.collectAsStateWithLifecycle()
    val isFeedPostable by composeFeedViewModel.isFeedPostable.collectAsStateWithLifecycle()
    val isBottomSheetShown by composeFeedViewModel.isBottomSheetShown.collectAsStateWithLifecycle()
    val currentBottomSheetSelection
            by composeFeedViewModel.currentComposeFeedBottomSheetSelection.collectAsStateWithLifecycle()

    ComposeFeedScreen(
        onCloseComposeFeedScreen = onCloseComposeFeedScreen,
        onPostFeed = { /*TODO*/ },
        isFeedPostable = isFeedPostable,
        feedScope = "All classes",
        authorName = "Muhammed Bilal",
        schoolName = "Future Leaders International School",
        currentTitle = feedTitle,
        currentContent = feedContent,
        isBottomSheetShown = isBottomSheetShown,
        onTitleValueChange = composeFeedViewModel::onTitleValueChange,
        onContentValueChange = composeFeedViewModel::onContentValueChange,
        clearBottomSheetSelection = composeFeedViewModel::clearBottomSheetSelection,
        currentBottomSheetSelection = currentBottomSheetSelection,
        openAttachmentsBottomSheet = {
            composeFeedViewModel.onComposeFeedBottomSheetSelectionChange(
                ComposeFeedBottomSheetSelection.Attachments
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ComposeFeedScreen(
    onCloseComposeFeedScreen: () -> Unit,
    onPostFeed: () -> Unit,
    isFeedPostable: Boolean,
    feedScope: String,
    authorName: String,
    schoolName: String,
    currentTitle: String,
    currentContent: String,
    isBottomSheetShown: Boolean,
    onTitleValueChange: (String) -> Unit,
    onContentValueChange: (String) -> Unit,
    clearBottomSheetSelection: () -> Unit,
    currentBottomSheetSelection: ComposeFeedBottomSheetSelection,
    openAttachmentsBottomSheet: () -> Unit,
) {

    var showModalBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(isBottomSheetShown) {
        if (isBottomSheetShown) {
            sheetState.show()
            showModalBottomSheet = true
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
                            enabled = isFeedPostable,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(0.2f)
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color =
                                if (isFeedPostable) {
                                    MaterialTheme.colorScheme.onPrimary
                                } else {
                                    MaterialTheme.colorScheme.onPrimary.copy(0.2f)
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

                                        else -> {

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
                schoolName = schoolName,
                authorName = authorName,
                currentTitle = currentTitle,
                currentContent = currentContent,
                onTitleValueChange = onTitleValueChange,
                onContentValueChange = onContentValueChange,
            )
        }
    )

    if (showModalBottomSheet)
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                    showModalBottomSheet = false
                    clearBottomSheetSelection()
                }
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
            when (currentBottomSheetSelection) {
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
                                /*todo -> do your thing */
                                scope.launch {
                                    sheetState.hide()
                                    showModalBottomSheet = false
                                    clearBottomSheetSelection()
                                }
                            }
                        )
                    }
                }

                else -> {

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
    authorName: String,
    schoolName: String,
    currentTitle: String,
    currentContent: String,
    onTitleValueChange: (String) -> Unit,
    onContentValueChange: (String) -> Unit,
    textFieldColors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        cursorColor = MaterialTheme.colorScheme.onBackground,
    ),
) {

    val catImage = "https://www.petsworld.in/blog/wp-content/uploads/2014/09/funny-cat.jpg"

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
                    text = authorName,
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
                value = currentTitle,
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
                value = currentContent,
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
        }
    )
}


@Composable
private fun ComposeFeedBody(
    modifier: Modifier = Modifier,
    profileImage: @Composable () -> Unit,
    author: @Composable () -> Unit,
    title: @Composable () -> Unit,
    text: @Composable () -> Unit,
) {
    Column {

        Row(
            Modifier
                .fillMaxWidth(),
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
        Box(Modifier.fillMaxWidth()) { title() }

        /*divider */
        Divider(Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(12.dp))
        /*content textfield */
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            text()
        }

        /*todo -> images section if any */
        /*todo -> videos section if any */
        /*todo -> docs section if any */
        /*todo -> audio section if any */


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

@Composable
@Preview
private fun ComposeFeedBodyPreview() {
    ComposeFeedBody(
        profileImage = {
            Text(
                "The Main Text", style = MaterialTheme.typography.headlineLarge
            )
        },
        author = {
            Text(
                "The Main Text", style = MaterialTheme.typography.headlineLarge
            )
        },
        title = {
            Text(
                "The Main Text", style = MaterialTheme.typography.headlineLarge
            )
        },
        text = {
            Text(
                "The Main Text", style = MaterialTheme.typography.headlineLarge
            )
        }
    )
}

@Composable
@Preview
private fun ComposeFeedBodyPreview2() {
    ComposeFeedBody(
        authorName = "Khalid Isah",
        schoolName = "Future Leaders International School",
        currentTitle = "Exam Timetable",
        currentContent = "I'm happy to announce the launch of the new exam time table",
        onContentValueChange = {},
        onTitleValueChange = {},
    )
}
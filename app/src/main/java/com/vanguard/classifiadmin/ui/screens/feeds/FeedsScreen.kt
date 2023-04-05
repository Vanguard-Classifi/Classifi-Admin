package com.vanguard.classifiadmin.ui.screens.feeds

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.FeedModel
import com.vanguard.classifiadmin.domain.helpers.MimeType
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.domain.helpers.runnableBlock
import com.vanguard.classifiadmin.domain.helpers.todayComputational
import com.vanguard.classifiadmin.ui.components.DropdownButton
import com.vanguard.classifiadmin.ui.components.FeedItem
import com.vanguard.classifiadmin.ui.components.FeedItemFeature
import com.vanguard.classifiadmin.ui.components.LoadingScreen
import com.vanguard.classifiadmin.ui.components.NoDataScreen
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.PrimaryTextButtonWithIcon
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.components.TertiaryTextButtonWithIcon
import com.vanguard.classifiadmin.ui.screens.dashboard.DashboardMessage
import com.vanguard.classifiadmin.ui.screens.dashboard.DefaultAvatar
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

const val FEEDS_SCREEN = "feeds_screen"

/**
 * A screen where you get to create and manage
 * feeds. A feed may be a discussion, an assessment,
 * a live class or a lesson.
 * All these can be created from this screen
 */

@Composable
fun FeedsScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onSelectClasses: () -> Unit,
    onDetails: (FeedModel) -> Unit,
    onViewReport: (FeedModel) -> Unit,
    onTakeAssessment: (FeedModel) -> Unit,
) {
    FeedsScreenContent(
        viewModel = viewModel,
        onSelectClasses = onSelectClasses,
        onDetails = onDetails,
        onViewReport = onViewReport,
        onTakeAssessment = onTakeAssessment,
    )
}


@Composable
fun FeedsScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onSelectClasses: () -> Unit,
    onDetails: (FeedModel) -> Unit,
    onViewReport: (FeedModel) -> Unit,
    onTakeAssessment: (FeedModel) -> Unit,
) {
    val TAG = "FeedsScreenContent"
    var upcomingActivitiesExpanded by remember { mutableStateOf(false) }
    val verticalScroll = rememberScrollState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val discussionTextCreateFeed by viewModel.discussionTextCreateFeed.collectAsState()
    val classFilterBufferFeeds by viewModel.classFilterBufferFeeds.collectAsState()
    val currentUsernamePref by viewModel.currentUsernamePref.collectAsState()
    val scope = rememberCoroutineScope()
    val composeDiscussionState by viewModel.composeDiscussionState.collectAsState()
    val feedActionListener by viewModel.feedActionListener.collectAsState()
    val stagedFeedsNetwork by viewModel.stagedFeedsNetwork.collectAsState()
    val currentClassFeedPref by viewModel.currentClassFeedPref.collectAsState()
    val verifiedFeedsByClassNetwork by viewModel.verifiedFeedsByClassNetwork.collectAsState()
    val classFilterBufferReadFeedsListener by viewModel.classFilterBufferReadFeedsListener.collectAsState()
    val classFilterBufferFeedsListener by viewModel.classFilterBufferFeedsListener.collectAsState()
    val classFilterBufferReadFeeds by viewModel.classFilterBufferReadFeeds.collectAsState()
    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { fileUri ->
            /**
            if (fileUri != null)
            viewModel.uploadFileToCache(fileUri)
             */
        }
    )

    LaunchedEffect(Unit, classFilterBufferReadFeedsListener, currentClassFeedPref) {
        viewModel.getCurrentClassFeedPref()
        viewModel.getCurrentSchoolIdPref()
        viewModel.getCurrentUserIdPref()
        viewModel.getCurrentUsernamePref()
        delay(1000)
        viewModel.getVerifiedFeedsByClassNetwork(
            currentClassFeedPref.orEmpty(),
            currentSchoolIdPref.orEmpty()
        )
        viewModel.getStagedFeedsNetwork(
            currentSchoolIdPref.orEmpty()
        )
    }

    LaunchedEffect(composeDiscussionState) {
        viewModel.getStagedFeedsNetwork(
            currentSchoolIdPref.orEmpty()
        )

        viewModel.getVerifiedFeedsByClassNetwork(
            currentClassFeedPref.orEmpty(),
            currentSchoolIdPref.orEmpty()
        )
        if (composeDiscussionState == true) {
            //create a blank feed on stage
            scope.launch {
                val classIds = arrayListOf<String>()
                classIds.addAll(classFilterBufferFeeds)

                val feed = FeedModel(
                    feedId = UUID.randomUUID().toString(),
                    authorId = currentUserIdPref,
                    authorName = currentUsernamePref,
                    schoolId = currentSchoolIdPref,
                    lastModified = todayComputational(),
                    classIds = classIds,
                    likes = arrayListOf(),
                    commentIds = arrayListOf(),
                    type = FeedType.Discussion.title,
                    mediaUris = arrayListOf(),
                    state = FeedState.Published.name,
                )

                viewModel.saveFeedAsStagedNetwork(
                    feed.toNetwork(), onResult = {}
                )
            }
        } else {
            //todo; delete all staged feeds
        }

    }

    LaunchedEffect(feedActionListener, classFilterBufferReadFeedsListener) {
        viewModel.getVerifiedFeedsByClassNetwork(
            currentClassFeedPref.orEmpty(),
            currentSchoolIdPref.orEmpty()
        )
    }

    LaunchedEffect(classFilterBufferFeedsListener) {
        viewModel.getStagedFeedsNetwork(
            currentSchoolIdPref.orEmpty()
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.surface,
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val maxHeight = maxHeight
            val maxWidth = maxWidth

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
                contentAlignment = Alignment.TopCenter,
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.padding(bottom = 72.dp),
                        state = rememberLazyListState(),
                    ) {
                        item {
                            DiscussionBox(
                                viewModel = viewModel,
                                onSelectClasses = onSelectClasses,
                                onSelectFeedType = {
                                    /*todo: on Select Feed Type : */
                                },
                                onPost = {
                                    scope.launch {
                                        viewModel.getStagedFeedsByClassNetwork(
                                            classFilterBufferFeeds.first(),
                                            currentSchoolIdPref.orEmpty()
                                        )
                                        delay(1000)

                                        if (stagedFeedsNetwork is Resource.Success &&
                                            stagedFeedsNetwork.data?.isNotEmpty() == true
                                        ) {
                                            scope.launch {
                                                val classIds = arrayListOf<String>()
                                                classIds.addAll(classFilterBufferFeeds)

                                                stagedFeedsNetwork.data!!.first().text =
                                                    discussionTextCreateFeed
                                                stagedFeedsNetwork.data!!.first().classIds =
                                                    classIds
                                                stagedFeedsNetwork.data!!.first().lastModified =
                                                    todayComputational()
                                                viewModel.saveFeedAsVerifiedNetwork(
                                                    stagedFeedsNetwork.data!!.first(),
                                                    onResult = {}
                                                )
                                            }.invokeOnCompletion {
                                                runnableBlock {
                                                    //switch back to the start discussion box
                                                    viewModel.onComposeDiscussionStateChanged(null)
                                                    //clear the discussion text field
                                                    viewModel.clearDiscussionTextField()
                                                    viewModel.onDashboardMessageChanged(
                                                        DashboardMessage.FeedCreated
                                                    )
                                                }
                                            }
                                        }
                                    }
                                },
                                onAttach = {
                                    /*todo; on Attach -> */
                                }
                            )
                        }

                        when (verifiedFeedsByClassNetwork) {
                            is Resource.Loading -> {
                                item {
                                    LoadingScreen(maxHeight = maxHeight)
                                }
                            }

                            is Resource.Success -> {
                                if (verifiedFeedsByClassNetwork.data?.isNotEmpty() == true) {
                                    val feedsSorted =
                                        verifiedFeedsByClassNetwork.data?.sortedByDescending { it.lastModified }
                                    items(feedsSorted!!) { feed ->
                                        FeedItem(
                                            feed = feed.toLocal(),
                                            currentUserId = currentUserIdPref.orEmpty(),
                                            onEngage = { feature ->
                                                when (feature) {
                                                    FeedItemFeature.Like -> {
                                                        scope.launch {
                                                            if (!feed.likes.contains(
                                                                    currentUserIdPref
                                                                )
                                                            ) {
                                                                //like
                                                                feed.likes.add(currentUserIdPref.orEmpty())
                                                            } else {
                                                                //unlike
                                                                feed.likes.remove(currentUserIdPref)
                                                            }
                                                            viewModel.saveFeedAsVerifiedNetwork(
                                                                feed,
                                                                onResult = {}
                                                            )
                                                        }.invokeOnCompletion {
                                                            runnableBlock {
                                                                viewModel.onIncFeedActionListener()
                                                            }
                                                        }
                                                    }

                                                    FeedItemFeature.Comment -> {
                                                        viewModel.onSelectedFeedChanged(feed.toLocal())
                                                        viewModel.onFeedDetailModeChanged(
                                                            FeedDetailMode.Comment
                                                        )
                                                        onDetails(feed.toLocal())
                                                    }

                                                    FeedItemFeature.Share -> {
                                                        /*todo; on share */
                                                    }
                                                }
                                            },
                                            onOptions = {
                                                /*todo: on click options */
                                            },
                                            onDetails = {
                                                viewModel.onSelectedFeedChanged(it)
                                                viewModel.onFeedDetailModeChanged(FeedDetailMode.Content)
                                                onDetails(it)
                                            },
                                            onViewReport = {
                                                viewModel.onCurrentAssessmentIdPublishedChanged(
                                                    it.feedId
                                                )
                                                onViewReport(it)
                                            },
                                            viewModel = viewModel,
                                            onTakeAssessment = {
                                                viewModel.onCurrentAssessmentIdPublishedChanged(
                                                    it.feedId
                                                )
                                                onTakeAssessment(it)
                                            }
                                        )
                                    }
                                } else {
                                    item {
                                        NoDataScreen(
                                            maxHeight = maxHeight,
                                            message = stringResource(id = R.string.no_feeds_available_yet),
                                            buttonLabel = "",
                                            showButton = false,
                                            onClick = {}
                                        )
                                    }
                                }
                            }

                            is Resource.Error -> {
                                item {
                                    NoDataScreen(
                                        maxHeight = maxHeight,
                                        message = stringResource(id = R.string.something_went_wrong),
                                        buttonLabel = "",
                                        showButton = false,
                                        onClick = {}
                                    )
                                }
                            }
                        }
                    }
                }
            }


            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter,
            ) {
                UpcomingActivities(
                    username = "Brown Jemilatu",
                    onToggleExpand = {
                        upcomingActivitiesExpanded = true
                    },
                    expanded = upcomingActivitiesExpanded
                )
            }
        }
    }
}


@Composable
fun DiscussionBox(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onSelectClasses: () -> Unit,
    onSelectFeedType: (FeedType) -> Unit,
    onPost: () -> Unit,
    onAttach: () -> Unit,
) {
    val composeDiscussionState by viewModel.composeDiscussionState.collectAsState()
    val classFilterBufferFeeds by viewModel.classFilterBufferFeeds.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val classByIdNetwork by viewModel.classByIdNetwork.collectAsState()
    val currentUsernamePref by viewModel.currentUsernamePref.collectAsState()
    val classFilterBufferFeedsListener by viewModel.classFilterBufferFeedsListener.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentUsernamePref()
        if (classFilterBufferFeeds.isNotEmpty()) {
            viewModel.getCurrentSchoolIdPref()
            val topClassId = classFilterBufferFeeds.first()
            viewModel.getClassByIdNetwork(topClassId, currentSchoolIdPref.orEmpty())
        }
    }

    LaunchedEffect(classFilterBufferFeedsListener) {
        if (classFilterBufferFeeds.isNotEmpty()) {
            viewModel.getCurrentSchoolIdPref()
            val topClassId = classFilterBufferFeeds.first()
            viewModel.getClassByIdNetwork(topClassId, currentSchoolIdPref.orEmpty())
        }
    }

    Box(
        modifier = Modifier,
        contentAlignment = Alignment.TopCenter,
    ) {
        AnimatedVisibility(
            modifier = modifier.fillMaxWidth(),
            visible = composeDiscussionState == true,
            enter = fadeIn(initialAlpha = 0.9f),
            exit = fadeOut(targetAlpha = 0.9f)
        ) {
            CreateDiscussionSection(
                viewModel = viewModel,
                currentClassCode = when {
                    classFilterBufferFeeds.isEmpty() -> stringResource(id = R.string.select_class)
                    classFilterBufferFeeds.size == 1 -> classByIdNetwork.data?.classCode.orEmpty()
                    classFilterBufferFeeds.size > 1 -> "${classByIdNetwork.data?.classCode.orEmpty()}..[]"
                    else -> stringResource(id = R.string.select_class)
                },
                onAbort = {
                    viewModel.deleteStagedFeedsNetwork(currentSchoolIdPref.orEmpty(), onResult = {})
                    viewModel.onComposeDiscussionStateChanged(null)
                },
                onSelectClasses = onSelectClasses,
                onPost = onPost,
                onAttach = onAttach,
            )
        }

        AnimatedVisibility(
            modifier = modifier.fillMaxWidth(),
            visible = composeDiscussionState != true,
            enter = fadeIn(initialAlpha = 0.9f),
            exit = fadeOut(targetAlpha = 0.9f)
        ) {
            StartDiscussionSection(
                username = currentUsernamePref.orEmpty(),
                onCreateDiscussion = {
                    viewModel.onComposeDiscussionStateChanged(true)
                },
                onSelectFeedType = onSelectFeedType,
            )
        }
    }
}


@Composable
fun CreateDiscussionSection(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    currentClassCode: String,
    onAbort: () -> Unit,
    onSelectClasses: () -> Unit,
    onPost: () -> Unit,
    onAttach: () -> Unit,
) {
    val discussionTextCreateFeed by viewModel.discussionTextCreateFeed.collectAsState()
    val classFilterBufferFeedsListener by viewModel.classFilterBufferFeedsListener.collectAsState()
    val classFilterBufferFeeds by viewModel.classFilterBufferFeeds.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val postable = remember(discussionTextCreateFeed, classFilterBufferFeedsListener) {
        discussionTextCreateFeed != null &&
                discussionTextCreateFeed?.isNotBlank() == true &&
                classFilterBufferFeeds.isNotEmpty()
    }

    LaunchedEffect(Unit) {
        viewModel.getCurrentSchoolIdPref()
    }

    LaunchedEffect(discussionTextCreateFeed) {
        viewModel.getStagedFeedsNetwork(
            currentSchoolIdPref.orEmpty()
        )
    }

    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(top = 8.dp, bottom = 64.dp, start = 4.dp, end = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 2.dp,
    ) {

        Column(
            modifier = modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Spacer(modifier = modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.start_a_discussion).uppercase(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Black100,
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 92.dp),
                value = discussionTextCreateFeed.orEmpty(),
                onValueChange = viewModel::onDiscussionTextCreateFeedChanged,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.discuss_something),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary.copy(0.3f),
                    )
                },
                shape = RoundedCornerShape(16.dp),
                textStyle = TextStyle(
                    color = Black100,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
            )

            DropdownButton(
                prefix = stringResource(id = R.string.post_to),
                text = currentClassCode,
                icon = R.drawable.icon_dropdown,
                onClick = onSelectClasses,
            )

            //assets go here


            CreateDiscussionBottomBar(
                onAbort = onAbort,
                onPost = onPost,
                onAttach = onAttach,
                postable = postable,
            )
        }

    }
}


@Composable
fun CreateDiscussionBottomBar(
    modifier: Modifier = Modifier,
    onAbort: () -> Unit,
    onPost: () -> Unit,
    postable: Boolean = false,
    onAttach: () -> Unit,
) {
    val constraints = CreateDiscussionBottomBarConstraints(4.dp)
    val innerModifier = Modifier
    var rowHeight by remember { mutableStateOf(0) }

    Surface(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .onGloballyPositioned { rowHeight = it.size.height },
        color = Black100.copy(0.02f),
        shape = CircleShape,
    ) {
        Box(
            modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            ConstraintLayout(
                modifier = modifier
                    .padding(0.dp)
                    .fillMaxWidth(),
                constraintSet = constraints,
            ) {
                TertiaryTextButtonWithIcon(
                    label = stringResource(id = R.string.attach), icon = R.drawable.icon_attach,
                    onClick = onAttach,
                    modifier = innerModifier.layoutId("attach"),
                    iconSize = 18.dp
                )

                RoundedIconButton(
                    modifier = innerModifier.layoutId("abortIcon"),
                    onClick = onAbort, icon = R.drawable.icon_abort,
                    surfaceColor = Color.Transparent,
                    size = 18.dp,
                )

                Divider(
                    modifier = innerModifier
                        .layoutId("divider")
                        .height(
                            with(LocalDensity.current) {
                                rowHeight
                                    .toDp()
                                    .times(.7f)
                            }
                        )
                        .width(1.dp)
                )

                PrimaryTextButton(
                    label = stringResource(id = R.string.post),
                    onClick = onPost,
                    modifier = innerModifier.layoutId("post"),
                    enabled = postable,
                )
            }
        }
    }
}

private fun CreateDiscussionBottomBarConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val attach = createRefFor("attach")
        val abortIcon = createRefFor("abortIcon")
        val divider = createRefFor("divider")
        val post = createRefFor("post")

        constrain(attach) {
            start.linkTo(parent.start, margin)
            top.linkTo(parent.top, 0.dp)
            bottom.linkTo(parent.bottom, 0.dp)
        }
        constrain(abortIcon) {
            top.linkTo(attach.top, 0.dp)
            bottom.linkTo(attach.bottom, 0.dp)
            end.linkTo(divider.start, 8.dp)
        }
        constrain(divider) {
            top.linkTo(attach.top, 0.dp)
            bottom.linkTo(attach.bottom, 0.dp)
            end.linkTo(post.start, 8.dp)
        }
        constrain(post) {
            end.linkTo(parent.end, margin)
            top.linkTo(attach.top, 0.dp)
            bottom.linkTo(attach.bottom, 0.dp)
        }
    }
}


@Composable
fun StartDiscussionSection(
    modifier: Modifier = Modifier,
    username: String,
    onCreateDiscussion: () -> Unit,
    onSelectFeedType: (FeedType) -> Unit,
) {
    val constraints = StartDiscussionSectionConstraints(8.dp)
    val innerModifier = Modifier
    var avatarHeight by remember { mutableStateOf(0) }

    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(top = 8.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 2.dp,
    ) {

        ConstraintLayout(
            modifier = Modifier.fillMaxWidth(),
            constraintSet = constraints,
        ) {
            DefaultAvatar(
                label = username,
                onClick = {},
                modifier = innerModifier
                    .layoutId("avatar")
                    .onGloballyPositioned { avatarHeight = it.size.height }
            )

            Box(
                modifier = innerModifier.layoutId("field"),
                contentAlignment = Alignment.CenterStart,
            ) {
                OutlinedTextField(
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier
                        .clickable { onCreateDiscussion() }
                        .padding(bottom = 8.dp)
                        .height(
                            with(LocalDensity.current) {
                                avatarHeight
                                    .toDp()
                                    .times(1.15f)
                            }
                        ),
                    value = "",
                    onValueChange = {},
                    label = {
                    },
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Black100.copy(0.5f),
                        backgroundColor = Color.Transparent,
                        focusedBorderColor = Black100.copy(0.5f),
                        placeholderColor = Black100.copy(0.5f),
                        focusedLabelColor = Black100.copy(0.5f)
                    )
                )

                Box(
                    modifier = Modifier.padding(start = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = stringResource(id = R.string.create_a_discussion_with_your_student),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Black100.copy(0.3f),
                        modifier = Modifier.padding(horizontal = 2.dp, vertical = 2.dp)
                    )
                }
            }

            Divider(
                modifier = innerModifier.layoutId("divider"),
                color = Black100.copy(0.1f),
            )

            FeedTypeRow(
                modifier = innerModifier.layoutId("feedTypeRow"),
                onSelectFeedType = onSelectFeedType,
            )
        }
    }
}


private fun StartDiscussionSectionConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val avatar = createRefFor("avatar")
        val field = createRefFor("field")
        val divider = createRefFor("divider")
        val feedTypeRow = createRefFor("feedTypeRow")

        constrain(avatar) {
            top.linkTo(field.top, 0.dp)
            bottom.linkTo(field.bottom, 0.dp)
            start.linkTo(parent.start, margin)
        }

        constrain(field) {
            top.linkTo(parent.top, 8.dp)
            end.linkTo(parent.end, margin)
            start.linkTo(avatar.end, 8.dp)
            width = Dimension.fillToConstraints
        }

        constrain(divider) {
            start.linkTo(parent.start, 4.dp)
            end.linkTo(parent.end, 4.dp)
            top.linkTo(field.bottom, 8.dp)
            width = Dimension.fillToConstraints
        }

        constrain(feedTypeRow) {
            start.linkTo(parent.start, 8.dp)
            end.linkTo(parent.end, 8.dp)
            top.linkTo(divider.bottom, 8.dp)
            width = Dimension.fillToConstraints
        }

    }
}

@Composable
fun FeedTypeRow(
    modifier: Modifier = Modifier,
    types: MutableList<FeedType> = FeedType.values().toMutableList(),
    onSelectFeedType: (FeedType) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        types.dropLast(1).forEach { type ->
            PrimaryTextButtonWithIcon(
                label = type.title,
                icon = type.icon,
                onClick = { onSelectFeedType(type) }
            )
        }
    }
}


enum class FeedType(val title: String, val icon: Int) {
    Assessment("Assessment", R.drawable.icon_assessment),
    LiveClass("Live Class", R.drawable.icon_video_camera),
    Lesson("Lesson", R.drawable.icon_subject),
    Discussion("Discussion", R.drawable.icon_discussion),
}

enum class FeedState {
    Pending, Published
}

fun String.toFeedType(): FeedType {
    return when (this) {
        FeedType.Discussion.title -> FeedType.Discussion
        FeedType.Assessment.title -> FeedType.Assessment
        FeedType.LiveClass.title -> FeedType.LiveClass
        FeedType.Lesson.title -> FeedType.Lesson
        else -> FeedType.Discussion
    }
}

@Composable
@Preview
private fun FeedTypeRowPreview() {
    FeedTypeRow(onSelectFeedType = {})
}

@Preview
@Composable
private fun CreateFeedSectionPreview() {
    StartDiscussionSection(
        username = "Khalid Isah",
        onCreateDiscussion = {},
        onSelectFeedType = {},
    )
}

@Composable
@Preview
private fun CreateDiscussionBottomBarPreview() {
    CreateDiscussionBottomBar(
        onAbort = {},
        onPost = {},
        onAttach = {}
    )
}


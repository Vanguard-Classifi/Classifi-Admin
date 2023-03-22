package com.vanguard.classifiadmin.ui.screens.feeds

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.CommentModel
import com.vanguard.classifiadmin.data.local.models.FeedModel
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.domain.helpers.runnableBlock
import com.vanguard.classifiadmin.domain.helpers.todayComputational
import com.vanguard.classifiadmin.ui.components.ChildTopBar
import com.vanguard.classifiadmin.ui.components.CommentItem
import com.vanguard.classifiadmin.ui.components.FeedItem
import com.vanguard.classifiadmin.ui.components.FeedItemFeature
import com.vanguard.classifiadmin.ui.components.FeedItemFooter
import com.vanguard.classifiadmin.ui.components.FeedItemHeader
import com.vanguard.classifiadmin.ui.components.LoadingScreen
import com.vanguard.classifiadmin.ui.components.NoDataScreen
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.components.TertiaryTextButtonWithIcon
import com.vanguard.classifiadmin.ui.screens.admin.ManageSubjectAdminDetailScreenContent
import com.vanguard.classifiadmin.ui.screens.dashboard.DefaultAvatar
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

const val FEED_DETAIL_SCREEN = "feed_detail_screen"

@Composable
fun FeedDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
) {

    Surface(modifier = Modifier) {
        BoxWithConstraints(modifier = Modifier) {
            val maxWidth = maxWidth
            val maxHeight = maxHeight

            Scaffold(
                modifier = modifier,
                topBar = {
                    ChildTopBar(
                        onBack = onBack,
                        elevation = 0.dp,
                        heading = stringResource(id = R.string.feed),
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary,
                    )
                },
                content = {
                    FeedDetailScreenContent(
                        modifier = modifier.padding(it),
                        viewModel = viewModel,
                        maxHeight = maxHeight,
                    )
                }
            )
        }
    }

}


@Composable
fun FeedDetailScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    maxHeight: Dp,
) {
    val verticalScroll = rememberScrollState()
    val selectedFeed by viewModel.selectedFeed.collectAsState()
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val currentUsernamePref by viewModel.currentUsernamePref.collectAsState()
    val scope = rememberCoroutineScope()
    val feedByIdNetwork by viewModel.feedByIdNetwork.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()
    val feedActionListener by viewModel.feedActionListener.collectAsState()
    val commentTextFieldState by viewModel.commentTextFieldState.collectAsState()
    val columnHeight = remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.getCurrentUserIdPref()
        viewModel.getCurrentUsernamePref()
        viewModel.getCurrentSchoolIdPref()
        viewModel.getFeedByIdNetwork(selectedFeed?.feedId.orEmpty(), currentSchoolIdPref.orEmpty())
    }

    LaunchedEffect(feedActionListener) {
        viewModel.getFeedByIdNetwork(selectedFeed?.feedId.orEmpty(), currentSchoolIdPref.orEmpty())
    }

    LaunchedEffect(commentTextFieldState) {
        viewModel.getFeedByIdNetwork(selectedFeed?.feedId.orEmpty(), currentSchoolIdPref.orEmpty())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScroll)
            .onGloballyPositioned { columnHeight.value = it.size.height }
    ) {
        when (feedByIdNetwork) {
            is Resource.Loading -> {
                LoadingScreen(maxHeight = maxHeight)
            }

            is Resource.Success -> {
                if (feedByIdNetwork.data != null) {
                    FeedDetailItem(
                        feed = feedByIdNetwork.data?.toLocal()!!,
                        currentUserId = currentUserIdPref.orEmpty(),
                        onEngage = { feature ->
                            when (feature) {
                                FeedItemFeature.Like -> {
                                    scope.launch {
                                        if (!(selectedFeed ?: FeedModel.Default).likes.contains(
                                                currentUserIdPref
                                            )
                                        ) {
                                            //like
                                            (selectedFeed
                                                ?: FeedModel.Default).likes.add(currentUserIdPref.orEmpty())
                                        } else {
                                            //unlike
                                            (selectedFeed ?: FeedModel.Default).likes.remove(
                                                currentUserIdPref
                                            )
                                        }
                                        viewModel.saveFeedAsVerifiedNetwork(
                                            (selectedFeed ?: FeedModel.Default).toNetwork(),
                                            onResult = {}
                                        )
                                    }.invokeOnCompletion {
                                        runnableBlock {
                                            viewModel.onIncFeedActionListener()
                                        }
                                    }
                                }

                                FeedItemFeature.Comment -> {
                                    scope.launch {
                                        delay(500)
                                        verticalScroll.animateScrollTo(columnHeight.value)
                                    }
                                }

                                FeedItemFeature.Share -> {
                                    /*todo; on share */
                                }
                            }
                        },
                        onOptions = {
                            /*todo: on options */
                        },
                        viewModel = viewModel,
                        currentUsername = currentUsernamePref.orEmpty(),
                        onAttach = {
                            /*todo: on Attach */
                        },
                        onPostComment = { text ->
                            if (text.isNotBlank()) {
                                scope.launch {
                                    val commentId = UUID.randomUUID().toString()
                                    viewModel.saveCommentNetwork(
                                        CommentModel(
                                            commentId = commentId,
                                            parentFeedId = selectedFeed?.feedId.orEmpty(),
                                            text = text,
                                            authorId = currentUserIdPref,
                                            authorName = currentUsernamePref,
                                            schoolId = currentSchoolIdPref,
                                            lastModified = todayComputational(),
                                        ).toNetwork(),
                                        onResult = {}
                                    )
                                    //add to comments under feed
                                    if (!feedByIdNetwork.data?.commentIds!!.contains(commentId)) {
                                        feedByIdNetwork.data?.commentIds!!.add(commentId)
                                        //update feed
                                        viewModel.saveFeedAsVerifiedNetwork(
                                            feedByIdNetwork.data!!,
                                            onResult = {}
                                        )
                                    }
                                }.invokeOnCompletion {
                                    runnableBlock {
                                        //close compose comment screen
                                        viewModel.onCommentTextFieldStateChanged(null)
                                        //clear comment field
                                        viewModel.clearCommentTextField()
                                        //scroll to end of comments
                                        scope.launch {
                                            delay(500)
                                            verticalScroll.animateScrollTo(columnHeight.value)
                                        }
                                    }
                                }
                            }
                        },
                        onEngageComment = { feature, comment ->
                            when (feature) {
                                FeedItemFeature.Like -> {
                                    scope.launch {
                                        if (!comment.likes.contains(currentUserIdPref)) {
                                            comment.likes.add(currentUserIdPref.orEmpty())
                                        } else {
                                            comment.likes.remove(currentUserIdPref)
                                        }
                                        viewModel.saveCommentNetwork(
                                            comment.toNetwork(),
                                            onResult = {}
                                        )
                                    }.invokeOnCompletion {
                                        runnableBlock {
                                            viewModel.onIncFeedActionListener()
                                        }
                                    }
                                }

                                FeedItemFeature.Share -> {
                                    /*todo on share comment */
                                }

                                else -> {}
                            }
                        }
                    )
                } else {
                    NoDataScreen(
                        maxHeight = maxHeight,
                        message = stringResource(id = R.string.could_not_load_feed),
                        buttonLabel = "",
                        showButton = false,
                        onClick = {}
                    )
                }
            }

            is Resource.Error -> {
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


@Composable
fun FeedDetailItem(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    feed: FeedModel,
    currentUserId: String,
    currentUsername: String,
    onEngage: (FeedItemFeature) -> Unit,
    onEngageComment: (FeedItemFeature, CommentModel) -> Unit,
    onOptions: () -> Unit,
    onAttach: () -> Unit,
    onPostComment: (String) -> Unit,
) {
    val commentsByFeedNetwork by viewModel.commentsByFeedNetwork.collectAsState()
    val commentTextFeedState by viewModel.commentTextFieldState.collectAsState()
    val currentSchoolIdPref by viewModel.currentSchoolIdPref.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentSchoolIdPref()
        delay(100)
        viewModel.getCommentsByFeedNetwork(feed.feedId, currentSchoolIdPref.orEmpty())
    }

    LaunchedEffect(commentTextFeedState) {
        viewModel.getCommentsByFeedNetwork(feed.feedId, currentSchoolIdPref.orEmpty())
    }

    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(0.dp))
            .padding(top = 8.dp, bottom = 8.dp, start = 0.dp, end = 0.dp),
        shape = RoundedCornerShape(0.dp),
        elevation = 2.dp,
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val maxHeight = maxHeight
            val maxWidth = maxWidth

            Column(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 0.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                FeedItemHeader(
                    feed = feed,
                    onOptions = onOptions,
                    type = feed.type?.toFeedType() ?: FeedType.Discussion
                )
                Spacer(modifier = modifier.height(8.dp))
                //content
                if (feed.text?.isNotBlank() == true && feed.text != null) {
                    Spacer(modifier = modifier.height(8.dp))
                    Text(
                        text = feed.text.orEmpty(),
                        fontSize = 13.sp,
                        color = Black100,
                        maxLines = 10,
                        modifier = modifier
                            .widthIn(max = maxWidth.times(.93f))
                            .heightIn(min = 200.dp)
                            .fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start,
                    )
                }
                if (feed.mediaUris.isNotEmpty()) {
                    feed.mediaUris.map { media ->
                        Spacer(modifier = modifier.height(8.dp))
                        //todo: media here

                    }
                }

                Spacer(modifier = modifier.height(8.dp))
                FeedItemFooter(feed = feed, currentUserId = currentUserId, onEngage = onEngage)
                Divider(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    color = Black100.copy(0.1f)
                )
                Spacer(modifier = modifier.height(8.dp))
                // compose comment
                ComposeCommentScreen(
                    viewModel = viewModel,
                    currentUsername = currentUsername,
                    onAttach = onAttach,
                    onPostComment = onPostComment,
                )

                Divider(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    color = Black100.copy(0.1f)
                )
                Spacer(modifier = modifier.height(8.dp))

                when (commentsByFeedNetwork) {
                    is Resource.Success -> {
                        if (commentsByFeedNetwork.data?.isNotEmpty() == true) {
                            val commentsSorted = commentsByFeedNetwork.data?.sortedBy {
                                it.lastModified
                            }
                            commentsSorted?.forEach { comment ->
                                CommentItem(
                                    comment = comment.toLocal(),
                                    currentUserId = currentUserId,
                                    onEngage = onEngageComment,
                                )
                                Spacer(modifier = modifier.height(8.dp))
                            }
                        }
                    }

                    else -> {}
                }

                Spacer(modifier = modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ComposeCommentScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    currentUsername: String,
    onAttach: () -> Unit,
    onPostComment: (String) -> Unit,
) {
    val avatarHeight = remember { mutableStateOf(0) }
    val commentTextFeedState by viewModel.commentTextFieldState.collectAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment =
        if (commentTextFeedState == true) Alignment.Top else Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        DefaultAvatar(
            modifier = modifier
                .onGloballyPositioned { avatarHeight.value = it.size.height },
            label = currentUsername,
            onClick = {},
        )

        Spacer(modifier = Modifier.width(8.dp))

        CommentTextFieldScreen(
            viewModel = viewModel,
            onComposeComment = {
                viewModel.onCommentTextFieldStateChanged(true)
            },
            height = with(LocalDensity.current) {
                avatarHeight.value.toDp().times(1.15f)
            },
            onAttach = onAttach,
            onAbortComment = {
                viewModel.onCommentTextFieldStateChanged(null)
            },
            onPostComment = onPostComment
        )
    }
}


@Composable
fun CommentTextFieldScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onComposeComment: () -> Unit,
    height: Dp,
    onAttach: () -> Unit,
    onAbortComment: () -> Unit,
    onPostComment: (String) -> Unit,
) {
    val commentTextFieldState by viewModel.commentTextFieldState.collectAsState()
    val commentTextFeed by viewModel.commentTextFeed.collectAsState()

    when (commentTextFieldState) {
        true -> {
            AnimatedVisibility(
                modifier = modifier.fillMaxWidth(),
                visible = true,
                enter = slideInHorizontally(),
                exit = slideOutHorizontally()
            ) {
                Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        OutlinedTextField(
                            enabled = true,
                            modifier = Modifier
                                .heightIn(min = height.times(4f)),
                            value = commentTextFeed.orEmpty(),
                            onValueChange = viewModel::onCommentTextFeedChanged,
                            label = {
                            },
                            shape = RoundedCornerShape(
                                topStart = 8.dp,
                                topEnd = 8.dp,
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Default,
                            ),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Black100,
                                backgroundColor = Color.Transparent,
                                focusedBorderColor = Black100.copy(0.5f),
                                placeholderColor = Black100.copy(0.5f),
                                focusedLabelColor = Black100.copy(0.5f)
                            )
                        )

                        ComposeCommentBottomBar(
                            onAbort = onAbortComment,
                            onPost = { onPostComment(commentTextFeed.orEmpty()) },
                            onAttach = onAttach,
                            postable = commentTextFeed != null && commentTextFeed?.isNotBlank() == true,
                        )
                    }
                }
            }
        }

        else -> {
            AnimatedVisibility(
                modifier = modifier.fillMaxWidth(),
                visible = true,
                enter = slideInHorizontally(),
                exit = slideOutHorizontally()
            ) {
                Box(
                    modifier = modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier
                            .clickable { onComposeComment() }
                            .padding(bottom = 8.dp)
                            .height(height),
                        value = "",
                        onValueChange = {},
                        label = {
                        },
                        trailingIcon = {
                            RoundedIconButton(
                                onClick = { onAttach() },
                                icon = R.drawable.icon_attach,
                                tint = Black100,
                                size = 18.dp,
                                surfaceColor = Color.Transparent,
                                surfaceSize = 22.dp,
                            )
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
                            text = stringResource(id = R.string.write_your_comment),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = Black100.copy(0.3f),
                            modifier = Modifier.padding(horizontal = 2.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ComposeCommentBottomBar(
    modifier: Modifier = Modifier,
    onAbort: () -> Unit,
    onPost: () -> Unit,
    postable: Boolean = false,
    onAttach: () -> Unit,
) {
    val constraints = ComposeCommentBottomBarConstraints(4.dp)
    val innerModifier = Modifier
    var rowHeight by remember { mutableStateOf(0) }

    Surface(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .onGloballyPositioned { rowHeight = it.size.height },
        color = Black100.copy(0.1f),
        shape = RoundedCornerShape(
            bottomStart = 8.dp,
            bottomEnd = 8.dp,
        ),
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

private fun ComposeCommentBottomBarConstraints(margin: Dp): ConstraintSet {
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


sealed class FeedDetailMode {
    object Content : FeedDetailMode()
    object Comment : FeedDetailMode()
}
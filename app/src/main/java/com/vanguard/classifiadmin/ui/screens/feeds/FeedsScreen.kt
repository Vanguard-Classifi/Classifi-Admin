package com.vanguard.classifiadmin.ui.screens.feeds

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.vanguard.classifiadmin.ui.components.DropdownButton
import com.vanguard.classifiadmin.ui.components.PrimaryTextButton
import com.vanguard.classifiadmin.ui.components.PrimaryTextButtonWithIcon
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.components.TertiaryTextButtonWithIcon
import com.vanguard.classifiadmin.ui.screens.dashboard.DefaultAvatar
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel

const val FEEDS_SCREEN = "feeds_screen"

@Composable
fun FeedsScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    FeedsScreenContent(viewModel = viewModel)
}

@Composable
fun FeedsScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    var upcomingActivitiesExpanded by remember { mutableStateOf(false) }
    val verticalScroll = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.surface,
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter,
            ) {
                //todo: news feed
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(verticalScroll)
                ) {
                    DiscussionBox(viewModel = viewModel)
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
) {
    val composeDiscussionState: MutableState<Boolean> = remember { mutableStateOf(false) }

    when (composeDiscussionState.value) {
        true -> {
            CreateDiscussionSection(
                viewModel = viewModel, currentClassCode = "GRD001",
                onAbort = { composeDiscussionState.value = false }
            )
        }

        false -> {
            StartDiscussionSection(
                username = "Khalid Isah",
                onCreateDiscussion = {
                    composeDiscussionState.value = true
                },
                onSelectFeedType = {/*todo: select feed type */ }
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
) {
    val discussionTextCreateFeed by viewModel.discussionTextCreateFeed.collectAsState()

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
                    imeAction = ImeAction.Default,
                ),
            )

            DropdownButton(
                prefix = stringResource(id = R.string.post_to),
                text = currentClassCode,
                icon = R.drawable.icon_dropdown,
                onClick = { /*TODO*/ }
            )

            CreateDiscussionBottomBar(
                onAbort = onAbort,
                onPost = {},
                onAttach = {}
            )
        }

    }
}


@Composable
fun CreateDiscussionBottomBar(
    modifier: Modifier = Modifier,
    onAbort: () -> Unit,
    onPost: () -> Unit,
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
        color = Black100.copy(0.1f),
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
                    modifier = innerModifier.layoutId("attach")
                )

                RoundedIconButton(
                    modifier = innerModifier.layoutId("abortIcon"),
                    onClick = onAbort, icon = R.drawable.icon_abort,
                    surfaceColor = Color.Transparent
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
                    modifier = innerModifier.layoutId("post")
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
            .padding(top = 8.dp, bottom = 64.dp, start = 8.dp, end = 8.dp),
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
                color = Black100.copy(0.5f),
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
    types: List<FeedType> = FeedType.values().toList(),
    onSelectFeedType: (FeedType) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        types.forEach { type ->
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
    Lesson("Lesson", R.drawable.icon_subject)
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
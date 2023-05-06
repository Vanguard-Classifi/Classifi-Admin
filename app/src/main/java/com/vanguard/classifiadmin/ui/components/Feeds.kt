package com.vanguard.classifiadmin.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.FeedModel
import com.vanguard.classifiadmin.domain.extensions.toAssessmentType
import com.vanguard.classifiadmin.domain.helpers.UserRole
import com.vanguard.classifiadmin.domain.helpers.lastModified
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentType
import com.vanguard.classifiadmin.ui.screens.dashboard.DefaultAvatar
import com.vanguard.classifiadmin.ui.screens.feeds.FeedType
import com.vanguard.classifiadmin.ui.screens.feeds.toFeedType
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.ui.theme.Blue100
import com.vanguard.classifiadmin.ui.theme.Green100
import com.vanguard.classifiadmin.ui.theme.Red100
import com.vanguard.classifiadmin.viewmodel.MainViewModel

@Composable
fun FeedItem(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    feed: FeedModel,
    currentUserId: String,
    onEngage: (FeedItemFeature) -> Unit,
    onOptions: () -> Unit,
    onDetails: (FeedModel) -> Unit,
    onViewReport: (FeedModel) -> Unit,
    onTakeAssessment: (FeedModel) -> Unit,
) {

    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(0.dp))
            .clickable { onDetails(feed) }
            .padding(top = 8.dp, bottom = 8.dp, start = 0.dp, end = 0.dp),
        shape = RoundedCornerShape(0.dp),
        elevation = 2.dp,
    ) {
        BoxWithConstraints(modifier = Modifier) {
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
                when (feed.type) {
                    FeedType.Discussion.title -> {
                        if (feed.text?.isNotBlank() == true && feed.text != null) {
                            Spacer(modifier = modifier.height(8.dp))
                            Text(
                                text = feed.text.orEmpty(),
                                fontSize = 13.sp,
                                color = Black100,
                                maxLines = 10,
                                modifier = modifier
                                    .widthIn(max = maxWidth.times(.93f))
                                    .heightIn(min = 100.dp)
                                    .fillMaxWidth(),
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Start,
                            )
                        }
                        if (feed.mediaUris.isNotEmpty()) {
                            if (feed.mediaUris.size < 3) {
                                feed.mediaUris.map { media ->
                                    Spacer(modifier = modifier.height(8.dp))
                                    //todo: media here

                                }
                            } else {
                                feed.mediaUris.subList(0, 2).map { media ->
                                    Spacer(modifier = modifier.height(8.dp))
                                    //todo: media here

                                }

                                TextButton(
                                    onClick = { onDetails(feed) },
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.see_more),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Black100,
                                        style = TextStyle(
                                            textDecoration = TextDecoration.Underline,
                                        )
                                    )
                                }
                            }
                        }
                    }

                    FeedType.Assessment.title -> {
                        FeedAssessmentBody(
                            feed = feed,
                            onViewReport = onViewReport,
                            viewModel = viewModel,
                            onTakeAssessment = onTakeAssessment,
                        )
                    }

                    FeedType.Lesson.title -> {

                    }

                    FeedType.LiveClass.title -> {

                    }
                }
                Spacer(modifier = modifier.height(8.dp))
                FeedItemFooter(feed = feed, currentUserId = currentUserId, onEngage = onEngage)
            }
        }
    }
}


@Composable
fun FeedAssessmentBody(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    feed: FeedModel,
    onViewReport: (FeedModel) -> Unit,
    onTakeAssessment: (FeedModel) -> Unit,
) {
    Card(
        modifier = modifier
            .padding(12.dp)
            .clickable { },
        elevation = 2.dp,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(0.5f),
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            FeedAssessmentBodyHeader(feed = feed, modifier = modifier)
            FeedAssessmentBodyMiddle(feed = feed, modifier = modifier)
            FeedAssessmentBodyFooter(
                feed = feed, onViewReport = onViewReport,
                modifier = modifier,
                viewModel = viewModel,
                onTakeAssessment = onTakeAssessment,
            )
        }
    }
}


@Composable
fun FeedAssessmentBodyHeader(
    modifier: Modifier = Modifier,
    feed: FeedModel,
) {
    val constraints = FeedAssessmentBodyHeaderConstraints(8.dp)
    val innerModifier = Modifier

    Surface(modifier = modifier) {
        ConstraintLayout(modifier = modifier, constraintSet = constraints) {
            AssessmentTypeIcon(
                type = feed.type?.toAssessmentType()!!,
                modifier = innerModifier.layoutId("icon")
            )

            Text(
                text = feed.assessmentName.orEmpty().uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Black100,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = innerModifier.layoutId("header")
            )

            Text(
                text = feed.assessmentSubject.orEmpty(),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Black100.copy(0.8f),
                modifier = innerModifier.layoutId("subheading")
            )
        }
    }

}

private fun FeedAssessmentBodyHeaderConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val icon = createRefFor("icon")
        val header = createRefFor("header")
        val subheading = createRefFor("subheading")

        constrain(icon) {
            start.linkTo(parent.start, 2.dp)
            top.linkTo(parent.top, 4.dp)
        }
        constrain(header) {
            top.linkTo(icon.top, 4.dp)
            start.linkTo(icon.end, 8.dp)
        }
        constrain(subheading) {
            top.linkTo(header.bottom, 4.dp)
            bottom.linkTo(icon.bottom, 4.dp)
            start.linkTo(header.start, 0.dp)
        }

    }
}


@Composable
fun FeedAssessmentBodyMiddle(
    modifier: Modifier = Modifier,
    feed: FeedModel,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = modifier.padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "OPEN",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Green100,
            )
            Text(
                text = stringResource(id = R.string.status),
                fontSize = 12.sp,
                color = Black100.copy(0.8f),
            )
        }

        Column(
            modifier = modifier.padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = feed.assessmentEndTime.orEmpty(),
                fontSize = 12.sp,
                color = Black100,
            )
            Text(
                text = stringResource(id = R.string.due_date),
                fontSize = 12.sp,
                color = Black100.copy(0.8f),
            )
        }
    }
}


@Composable
fun FeedAssessmentBodyFooter(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    feed: FeedModel,
    onViewReport: (FeedModel) -> Unit,
    onTakeAssessment: (FeedModel) -> Unit,
) {
    val currentUserIdPref by viewModel.currentUserIdPref.collectAsState()
    val currentUserRolePref by viewModel.currentUserRolePref.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentUserIdPref()
        viewModel.getCurrentUserRolePref()
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        PrimaryTextButton(
            label = stringResource(
                id =
                if (currentUserRolePref == UserRole.SuperAdmin.name ||
                    currentUserRolePref == UserRole.Admin.name
                )
                    R.string.take_assessment else
                    R.string.view_report
            ).uppercase(),
            onClick = { onTakeAssessment(feed) })
    }
}


@Composable
fun AssessmentTypeIcon(
    modifier: Modifier = Modifier,
    type: AssessmentType,
) {
    val color = when (type) {
        AssessmentType.HomeWork -> Blue100
        AssessmentType.Exam -> Red100
        AssessmentType.Quiz -> Green100
    }

    Surface(
        modifier = modifier,
        color = color,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_book),
                contentDescription = stringResource(id = R.string.icon),
                tint = MaterialTheme.colors.onPrimary,
                modifier = modifier.padding(16.dp)
            )
        }
    }
}


@Composable
fun DocumentThumbnail(
    modifier: Modifier = Modifier,
    icon: Int,
    filename: String,
    fileInfo: String,
    onOptions: () -> Unit,
) {
    val constraints = DocumentThumbnailConstraints(4.dp)
    val innerModifier = Modifier

    Card(
        modifier = modifier,
        elevation = 2.dp,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(
            width = 1.5.dp,
            color = MaterialTheme.colors.secondary,
        )
    ) {
        BoxWithConstraints(
            modifier = Modifier,
            contentAlignment = Alignment.Center,
        ) {
            val maxWidth = maxWidth
            val maxHeight = maxHeight

            ConstraintLayout(modifier = Modifier, constraintSet = constraints) {
                DocumentIcon(
                    icon = icon,
                    modifier = innerModifier.layoutId("icon")
                )

                Text(
                    text = filename,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Black100,
                    modifier = innerModifier
                        .layoutId("title")
                        .width(maxWidth.times(0.82f)),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = fileInfo,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = Black100.copy(0.8f),
                    modifier = innerModifier
                        .layoutId("subtitle")
                        .width(maxWidth.times(0.82f))
                )

                RoundedIconButton(
                    onClick = onOptions,
                    modifier = innerModifier.layoutId(""),
                    icon = R.drawable.icon_options_horizontal,
                )
            }
        }
    }
}


private fun DocumentThumbnailConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val icon = createRefFor("icon")
        val title = createRefFor("title")
        val subtitle = createRefFor("subtitle")
        val options = createRefFor("options")

        constrain(icon) {
            start.linkTo(parent.start, margin)
            top.linkTo(parent.top, margin)
            bottom.linkTo(parent.bottom, margin)
        }
        constrain(title) {
            top.linkTo(icon.top, 2.dp)
            start.linkTo(icon.end, 8.dp)
        }
        constrain(subtitle) {
            bottom.linkTo(icon.bottom, 2.dp)
            top.linkTo(title.bottom, 0.dp)
            start.linkTo(title.start, 0.dp)
        }
        constrain(options) {
            end.linkTo(parent.end, margin)
            top.linkTo(icon.top, 0.dp)
            bottom.linkTo(icon.bottom, 0.dp)
        }
    }
}


@Composable
fun DocumentIcon(
    modifier: Modifier = Modifier,
    icon: Int,
    surfaceColor: Color = MaterialTheme.colors.secondary.copy(0.5f),
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        color = surfaceColor,
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = R.string.icon),
                modifier = modifier.padding(8.dp)
            )
        }
    }
}


@Composable
fun VideoThumbnail(
    modifier: Modifier = Modifier
) {

}

@Composable
fun AudioThumbnail(
    modifier: Modifier = Modifier
) {

}


@Composable
fun FeedItemFooter(
    modifier: Modifier = Modifier,
    feed: FeedModel,
    features: List<FeedItemFeature> = FeedItemFeature.values().toList(),
    currentUserId: String,
    onEngage: (FeedItemFeature) -> Unit,
) {
    val rowWidth = remember { mutableStateOf(0) }

    Surface(modifier = Modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { rowWidth.value = it.size.width },
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            features.forEach { feature ->
                val label = when (feature) {
                    FeedItemFeature.Like -> if (feed.likes.isEmpty()) "" else feed.likes.size.toString()
                    FeedItemFeature.Comment -> if (feed.commentIds.isEmpty()) "" else feed.commentIds.size.toString()
                    else -> ""
                }

                val engaged = when (feature) {
                    FeedItemFeature.Like -> feed.likes.contains(currentUserId)
                    FeedItemFeature.Comment -> feed.commentIds.contains(currentUserId)
                    else -> false
                }

                FeedItemFeatureScreen(
                    icon = feature.icon,
                    iconEngaged = feature.iconEngaged,
                    label = label,
                    onEngage = { onEngage(feature) },
                    engaged = engaged,
                    width = with(LocalDensity.current) {
                        rowWidth.value.toDp().times(0.3f)
                    },
                )
            }
        }
    }
}


@Composable
fun FeedItemFeatureScreen(
    modifier: Modifier = Modifier,
    engaged: Boolean = false,
    icon: Int,
    iconEngaged: Int,
    label: String,
    onEngage: () -> Unit,
    width: Dp,
    enabled: Boolean = true,
    colorEngaged: Color = MaterialTheme.colors.primary,
) {
    val animatedColor = animateColorAsState(
        targetValue = if (engaged) colorEngaged else Black100.copy(
            0.8f
        ),
    )

    TextButton(
        enabled = enabled,
        onClick = onEngage,
        modifier = modifier
            .width(width)
            .padding(0.dp)
    ) {
        Icon(
            painter = painterResource(id = if (engaged) iconEngaged else icon),
            contentDescription = stringResource(id = R.string.icon),
            tint = animatedColor.value,
            modifier = modifier
                .padding(2.dp)
                .size(18.dp)
        )

        Text(
            text = label,
            fontSize = 12.sp,
            color = Black100.copy(0.8f),
            modifier = modifier.padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
        )
    }
}


@Composable
fun FeedItemHeader(
    modifier: Modifier = Modifier,
    feed: FeedModel,
    type: FeedType = FeedType.Discussion,
    onOptions: () -> Unit,
) {
    val constraints = FeedItemHeaderConstraints(4.dp)
    val innerModifier = Modifier
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(feed.authorName?.uppercase().orEmpty())
        }
        when (feed.type) {
            FeedType.Discussion.title -> {
                append(" started a ")
            }

            FeedType.Assessment.title -> {
                append(" created an ")
            }

            FeedType.Lesson.title -> {
                append(" created a ")
            }

            FeedType.LiveClass.title -> {
                append(" started a ")
            }
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(type.title.lowercase())
        }
    }

    Surface(modifier = Modifier) {
        BoxWithConstraints(modifier = Modifier) {
            val maxHeight = maxHeight
            val maxWidth = maxWidth

            ConstraintLayout(modifier = Modifier.fillMaxWidth(), constraintSet = constraints) {
                DefaultAvatar(
                    label = feed.authorName.orEmpty(),
                    onClick = { },
                    modifier = innerModifier.layoutId("avatar")
                )

                Text(
                    text = annotatedString,
                    modifier = innerModifier
                        .layoutId("author")
                        .width(maxWidth.times(0.7f)),
                    maxLines = 2,
                    fontSize = 12.sp,
                    color = Black100,
                    lineHeight = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = feed.lastModified?.lastModified().orEmpty(),
                    modifier = innerModifier.layoutId("lastModified"),
                    fontSize = 11.sp,
                    color = Black100.copy(0.8f)
                )


                RoundedIconButton(
                    onClick = onOptions,
                    icon = R.drawable.icon_options_horizontal,
                    modifier = innerModifier.layoutId("options"),
                    tint = Black100
                )

            }
        }
    }
}


private fun FeedItemHeaderConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val avatar = createRefFor("avatar")
        val lastModified = createRefFor("lastModified")
        val author = createRefFor("author")
        val options = createRefFor("options")

        constrain(avatar) {
            top.linkTo(parent.top, 0.dp)
            bottom.linkTo(parent.bottom, 0.dp)
            start.linkTo(parent.start, margin)
        }
        constrain(author) {
            top.linkTo(avatar.top, 0.dp)
            start.linkTo(avatar.end, 8.dp)
        }
        constrain(lastModified) {
            bottom.linkTo(avatar.bottom, 0.dp)
            start.linkTo(author.start, 0.dp)
            top.linkTo(author.bottom, 2.dp)
        }
        constrain(options) {
            top.linkTo(avatar.top, 0.dp)
            bottom.linkTo(avatar.bottom, 0.dp)
            end.linkTo(parent.end, margin)
        }
    }
}

enum class FeedItemFeature(val icon: Int, val iconEngaged: Int) {
    Like(R.drawable.icon_like_outline, R.drawable.icon_like_solid),
    Comment(R.drawable.icon_comment_outline, R.drawable.icon_comment_solid),
    Share(R.drawable.icon_share, R.drawable.icon_share),
}


@Composable
@Preview
private fun FeedItemFeaturePreview() {
    FeedItemFeatureScreen(
        icon = R.drawable.icon_like_outline,
        iconEngaged = R.drawable.icon_like_solid,
        onEngage = {},
        label = "9",
        width = 23.dp,
    )
}


@Composable
@Preview
private fun DocumentIconPreview() {
    DocumentIcon(
        icon = R.drawable.icon_pdf_file,
    )
}


@Composable
@Preview
private fun AssessmentTypeIconPreview() {
    AssessmentTypeIcon(
        type = AssessmentType.HomeWork
    )
}

@Composable
@Preview
private fun FeedAssessmentBodyMiddlePreview() {
    FeedAssessmentBodyMiddle(
        feed = FeedModel(
            feedId = "refd",
            text = "feufejf",
            assessmentEndTime = "4th Apr, 2023 6:34am"
        ),
    )
}

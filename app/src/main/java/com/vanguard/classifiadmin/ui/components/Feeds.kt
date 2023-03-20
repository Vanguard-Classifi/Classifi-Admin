package com.vanguard.classifiadmin.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
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
import com.vanguard.classifiadmin.domain.helpers.lastModified
import com.vanguard.classifiadmin.ui.screens.dashboard.DefaultAvatar
import com.vanguard.classifiadmin.ui.screens.feeds.FeedType
import com.vanguard.classifiadmin.ui.screens.feeds.toFeedType
import com.vanguard.classifiadmin.ui.theme.Black100

@Composable
fun FeedItem(
    modifier: Modifier = Modifier,
    feed: FeedModel,
    currentUserId: String,
    onEngage: (FeedItemFeature) -> Unit,
    onOptions: () -> Unit,
) {

    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(0.dp))
            .padding(top = 8.dp, bottom = 64.dp, start = 0.dp, end = 0.dp),
        shape = RoundedCornerShape(0.dp),
        elevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FeedItemHeader(feed = feed, onOptions = onOptions, type = feed.type?.toFeedType() ?: FeedType.Discussion)
            Spacer(modifier = modifier.height(16.dp))
            FeedItemFooter(feed = feed, currentUserId = currentUserId, onEngage = onEngage)
        }

    }
}


@Composable
fun FeedItemFooter(
    modifier: Modifier = Modifier,
    feed: FeedModel,
    features: List<FeedItemFeature> = FeedItemFeature.values().toList(),
    currentUserId: String,
    onEngage: (FeedItemFeature) -> Unit
) {
    Surface(modifier = Modifier) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceAround
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
        onClick = onEngage
    ) {
        Icon(
            painter = painterResource(id = if (engaged) iconEngaged else icon),
            contentDescription = stringResource(id = R.string.icon),
            tint = animatedColor.value,
            modifier = modifier
                .padding(8.dp)
                .size(24.dp)
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
            append(feed.authorName?.uppercase())
        }
        append(" started a ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(type.title.lowercase())
        }
    }

    Surface(modifier = Modifier) {
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
                    .width(300.dp),
                maxLines = 2,
                fontSize = 12.sp,
                color = Black100
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
        label = "9"
    )
}
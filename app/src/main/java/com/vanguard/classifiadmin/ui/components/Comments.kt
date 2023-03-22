package com.vanguard.classifiadmin.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.CommentModel
import com.vanguard.classifiadmin.data.local.models.FeedModel
import com.vanguard.classifiadmin.ui.screens.dashboard.DefaultAvatar
import com.vanguard.classifiadmin.ui.screens.feeds.FeedType
import com.vanguard.classifiadmin.ui.screens.feeds.toFeedType
import com.vanguard.classifiadmin.ui.theme.Black100


@Composable
fun CommentItem(
    modifier: Modifier = Modifier,
    comment: CommentModel,
    currentUserId: String,
    onEngage: (FeedItemFeature, CommentModel) -> Unit,
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        DefaultAvatar(
            label = comment.authorName.orEmpty(),
            onClick = {},
        )

        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(0.dp))
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
                    Text(
                        text = comment.authorName.orEmpty().uppercase(),
                        modifier = modifier
                            .width(maxWidth.times(0.7f)),
                        maxLines = 1,
                        fontSize = 12.sp,
                        color = Black100,
                        lineHeight = 12.sp,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = modifier.height(8.dp))
                    //content
                    if (comment.text?.isNotBlank() == true && comment.text != null) {
                        Spacer(modifier = modifier.height(8.dp))
                        Text(
                            text = comment.text.orEmpty(),
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
                    if (comment.mediaUris.isNotEmpty()) {
                        comment.mediaUris.map { media ->
                            Spacer(modifier = modifier.height(8.dp))
                            //todo: media here

                        }
                    }
                    Spacer(modifier = modifier.height(8.dp))
                    CommentItemFooter(
                        comment = comment,
                        currentUserId = currentUserId,
                        onEngage = { onEngage(it, comment) },
                    )
                }
            }
        }
    }
}


@Composable
fun CommentItemFooter(
    modifier: Modifier = Modifier,
    comment: CommentModel,
    features: List<FeedItemFeature> = FeedItemFeature.values().toList(),
    currentUserId: String,
    onEngage: (FeedItemFeature) -> Unit,
) {
    val rowWidth = remember { mutableStateOf(0) }
    val commentFeatures = ArrayList<FeedItemFeature>()

    LaunchedEffect(Unit) {
        features.map { feature ->
            if(feature != FeedItemFeature.Comment) {
                commentFeatures.add(feature)
            }
        }
    }


    Surface(modifier = Modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { rowWidth.value = it.size.width },
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            commentFeatures.forEach { feature ->
                val label = when (feature) {
                    FeedItemFeature.Like -> if (comment.likes.isEmpty()) "" else comment.likes.size.toString()
                    else -> ""
                }

                val engaged = when (feature) {
                    FeedItemFeature.Like -> comment.likes.contains(currentUserId)
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

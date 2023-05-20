package com.khalidtouch.core.designsystem.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.khalidtouch.chatme.domain.models.UserNewsFeed
import com.khalidtouch.classifiadmin.model.MessageType
import com.khalidtouch.classifiadmin.model.classifi.ClassifiFeed
import com.khalidtouch.core.designsystem.R
import com.khalidtouch.core.designsystem.components.ClassifiIconButton
import com.khalidtouch.core.designsystem.components.ClassifiToggleButton
import com.khalidtouch.core.designsystem.icons.ClassifiIcons


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsFeedCard(
    newsFeed: UserNewsFeed,
    modifier: Modifier = Modifier,
    isLiked: Boolean,
    onToggleLike: (Boolean) -> Unit,
    onClick: () -> Unit,
    onStartComment: () -> Unit,
    onViewAuthor: () -> Unit,
    onSharePost: () -> Unit,
    cardColors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface,
    ),
) {
    val clickActionLabel = stringResource(id = R.string.open_feeds)
    Card(
        modifier = modifier.semantics {
            onClick(label = clickActionLabel, action = null)
        },
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = cardColors,
    ) {
        Column {
            NewsHeaderLayerWrapper(
                newsFeed = newsFeed,
                onClick = onViewAuthor,
            )

            Box(modifier = Modifier.padding(NewsFeedDefaults.containerPaddingMedium)) {
                val (checkTextMessage, besideText) = newsFeed.messages.partition { it.type == MessageType.TextMessage }
                val (checkFileMessage, asideFile) = newsFeed.messages.partition { it.type == MessageType.FileMessage }
                val (checkImageMessage, asideImage) = newsFeed.messages.partition { it.type == MessageType.ImageMessage }
                val (checkVideoMessage, asideVideo) = newsFeed.messages.partition { it.type == MessageType.VideoMessage }
                val (checkAudioMessage, asideAudio) = newsFeed.messages.partition { it.type == MessageType.AudioMessage }

                val bodyStyle = MaterialTheme.typography.bodyLarge

                NewsFeedBody(
                    shortMessage = {
                        if (checkTextMessage.isNotEmpty()) {
                            ProvideTextStyle(value = bodyStyle) {
                                Text(
                                    text = checkTextMessage.first().message,
                                    maxLines = 8,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    },
                    photoMessage = {
                        /*todo -> handle photo files */
                    },
                    fileMessage = {
                        /*todo -> handle doc files */
                    },
                    videoMessage = {
                        /*todo -> handle video files */
                    },
                    interactions = {
                        Box(Modifier.padding(NewsFeedDefaults.iconButtonPadding)) {
                            ClassifiToggleButton(
                                checked = isLiked,
                                onCheckedChange = { onToggleLike(it) },
                                checkedIcon = {
                                    Icon(
                                        painter = painterResource(ClassifiIcons.LikeSolid),
                                        contentDescription = stringResource(id = R.string.unlike)
                                    )
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(ClassifiIcons.LikeOutline),
                                        contentDescription = stringResource(id = R.string.like)
                                    )
                                }
                            )
                        }

                        Box(Modifier.padding(NewsFeedDefaults.iconButtonPadding)) {
                            ClassifiIconButton(
                                onClick = { onStartComment() },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = ClassifiIcons.CommentOutline),
                                        contentDescription = stringResource(id = R.string.comment)
                                    )
                                },
                            )
                        }

                        Box(Modifier.padding(NewsFeedDefaults.iconButtonPadding)) {
                            ClassifiIconButton(
                                onClick = { onSharePost() },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = ClassifiIcons.Share),
                                        contentDescription = stringResource(id = R.string.share)
                                    )
                                },
                            )
                        }
                    }
                )
            }
        }
    }
}


@Composable
internal fun NewsFeedBody(
    shortMessage: @Composable (() -> Unit)? = null,
    photoMessage: @Composable (RowScope.() -> Unit)? = null,
    fileMessage: @Composable (RowScope.() -> Unit)? = null,
    videoMessage: @Composable (RowScope.() -> Unit)? = null,
    interactions: @Composable RowScope.() -> Unit,
) {
    Column {
        Spacer(modifier = Modifier.height(12.dp))
        if (shortMessage != null) {
            Box { shortMessage() }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (photoMessage != null) {
            Row {
                photoMessage()
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (fileMessage != null) {
            Row { fileMessage() }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (videoMessage != null) {
            Row { videoMessage() }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Divider(modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            interactions()
        }
    }
}


@Composable
internal fun NewsHeaderLayerWrapper(
    newsFeed: UserNewsFeed,
    onClick: () -> Unit,
) {
    val authorStyle = MaterialTheme.typography.titleMedium
    val descriptionStyle = MaterialTheme.typography.bodyMedium

    NewsHeaderLayer(
        onClick = onClick,
        image = {
            AsyncImage(
                placeholder = painterResource(id = ClassifiIcons.Personal),
                modifier = Modifier
                    .size(NewsFeedDefaults.imageSize),
                contentScale = ContentScale.Crop,
                model = newsFeed.creator.profile?.profileImage,
                contentDescription = null,
            )
        },
        authorName = {
            ProvideTextStyle(authorStyle) {
                Text(
                    text = "Nataraj Sasid",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        description = {
            ProvideTextStyle(descriptionStyle) {
                Text(
                    text = "CEO & founder Grow Rapid",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        timestamp = {
            ProvideTextStyle(descriptionStyle) {
                Text(
                    text = "12h",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    )
}


@Composable
internal fun NewsHeaderLayer(
    onClick: () -> Unit,
    image: @Composable () -> Unit,
    authorName: @Composable () -> Unit,
    description: @Composable () -> Unit,
    timestamp: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                enabled = true,
                role = Role.Button,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.padding(NewsFeedDefaults.imagePadding)) {
            image()
        }
        Column(Modifier.padding(NewsFeedDefaults.containerPaddingSmall)) {
            Box {
                authorName()
            }
            Box { description() }
            Row { timestamp() }
        }
    }
}


object NewsFeedDefaults {
    val imagePadding = 12.dp
    val containerPaddingSmall = 8.dp
    val containerPaddingMedium = 16.dp
    val imageSize = 88.dp
    val iconButtonPadding = 8.dp
}
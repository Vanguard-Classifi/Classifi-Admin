package com.khalidtouch.classifiadmin.feeds.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.khalidtouch.classifiadmin.feeds.R
import com.khalidtouch.core.designsystem.components.ClassifiButton
import com.khalidtouch.core.designsystem.components.ClassifiComposeFeedBottomBar
import com.khalidtouch.core.designsystem.components.ClassifiIconButton
import com.khalidtouch.core.designsystem.components.ClassifiSimpleTopAppBar
import com.khalidtouch.core.designsystem.icons.ClassifiIcons


@Composable
fun ComposeFeedRoute(
    composeFeedViewModel: ComposeFeedViewModel = hiltViewModel<ComposeFeedViewModel>(),
    onCloseComposeFeedScreen: () -> Unit,
) {

    ComposeFeedScreen(
        onCloseComposeFeedScreen = onCloseComposeFeedScreen,
        onPostFeed = { /*TODO*/ },
        isFeedPostable = false,
        feedScope = "All classes",
        authorName = "Muhammed Bilal",
        schoolName = "Future Leaders International School",
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
) {
    Scaffold(
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
                                onClick = { },
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
                authorName = authorName
            )
        }
    )
}


@Composable
private fun ComposeFeedBody(
    modifier: Modifier = Modifier,
    authorName: String,
    schoolName: String,
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
                value = "",
                onValueChange = {},
                placeholder = {
                    Text(
                        text = "Title",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
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
                value = "",
                onValueChange = {},
                placeholder = {
                    Text(
                        text = "What do you want to write about?",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                colors = textFieldColors,
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
    )
}
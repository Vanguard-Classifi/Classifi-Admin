package com.vanguard.classifiadmin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.khalidtouch.classifiadmin.feeds.compose.navigateToComposeFeed
import com.khalidtouch.classifiadmin.settings.navigation.navigateToSettings
import com.khalidtouch.core.designsystem.components.ClassifiAvatar
import com.khalidtouch.core.designsystem.components.ClassifiBackground
import com.khalidtouch.core.designsystem.components.ClassifiCenterTopAppBar
import com.khalidtouch.core.designsystem.components.ClassifiGradientBackground
import com.khalidtouch.core.designsystem.components.ClassifiIconButton
import com.khalidtouch.core.designsystem.components.ClassifiMenu
import com.khalidtouch.core.designsystem.components.ClassifiOutlinedButton
import com.khalidtouch.core.designsystem.components.ClassifiToggleButton
import com.khalidtouch.core.designsystem.components.MenuHeader
import com.khalidtouch.core.designsystem.components.MenuItem
import com.khalidtouch.core.designsystem.extensions.getInitials
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import com.khalidtouch.core.designsystem.theme.GradientColors
import com.khalidtouch.core.designsystem.theme.LocalGradientColors
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.navigation.ClassifiBottomNavHost
import com.vanguard.classifiadmin.navigation.TopLevelDestination

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun ClassifiHomeScreen(
    windowSizeClass: WindowSizeClass,
    appState: ClassifiAppState,
    onComposeFeed: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    val TAG = "HomeScreen"
    val shouldShowGradientBackground =
        appState.currentTopLevelDestination == TopLevelDestination.Feeds

    ClassifiBackground {
        ClassifiGradientBackground(
            gradientColors = if (shouldShowGradientBackground) {
                LocalGradientColors.current
            } else {
                GradientColors()
            }
        ) {
            val snackbarHostState = remember { SnackbarHostState() }
            var menuState by remember { mutableStateOf(false) }

            // If user is not connected to the internet show a snack bar to inform them.
            val notConnectedMessage = stringResource(R.string.not_connected)

            Scaffold(
                modifier = Modifier.semantics {
                    testTagsAsResourceId = true
                },
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                snackbarHost = { SnackbarHost(snackbarHostState) },
                bottomBar = {
                    if (appState.shouldShowBottomBar) {
                        ClassifiBottomBar(
                            destinations = appState.topLevelDestinations,
                            onNavigationToDestination = appState::navigationToTopLevelDestination,
                            currentDestination = appState.currentDestination,
                            modifier = Modifier.testTag("ClassifiBottomBar")
                        )
                    }
                }
            ) { padding ->

                BoxWithConstraints {

                    Row(
                        Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .consumeWindowInsets(padding)
                            .windowInsetsPadding(
                                WindowInsets.safeDrawing.only(
                                    WindowInsetsSides.Horizontal
                                ),
                            ),
                    ) {
                        if (appState.shouldShowNavRail) {
                            ClassifiNavRail(
                                destinations = appState.topLevelDestinations,
                                onNavigateToDestination = appState::navigationToTopLevelDestination,
                                currentDestination = appState.currentDestination,
                                modifier = Modifier
                                    .testTag("ClassifiNavRail")
                                    .safeDrawingPadding()
                            )
                        }

                        Column(Modifier.fillMaxSize()) {
                            val destination = appState.currentTopLevelDestination
                            if (destination != null) {
                                ClassifiCenterTopAppBar(
                                    elevate = false,
                                    text = {
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {

                                            ClassifiOutlinedButton(
                                                modifier = Modifier,
                                                onClick = { /*TODO on select class .... */ },
                                                content = {
                                                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onPrimary) {
                                                        Text(
                                                            text = "KG100",
                                                            style = MaterialTheme.typography.labelMedium,
                                                        )

                                                        Icon(
                                                            painter = painterResource(id = ClassifiIcons.Dropdown),
                                                            contentDescription = null,
                                                        )
                                                    }
                                                }
                                            )

                                            ClassifiIconButton(
                                                onClick = {/*todo -> more features */ },
                                                icon = {
                                                    Icon(
                                                        painter = painterResource(id = ClassifiIcons.Results),
                                                        contentDescription = null
                                                    )
                                                }
                                            )

                                        }
                                    },
                                    leadingIcon = {
                                        Box(modifier = Modifier.padding(start = 8.dp)) {
                                            Icon(
                                                painter = painterResource(id = ClassifiIcons.BrandingBig),
                                                contentDescription = null,
                                            )
                                        }
                                    },
                                    trailingIcon = {
                                        val textStyle = MaterialTheme.typography.labelMedium.copy(
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Box(modifier = Modifier.padding(end = 8.dp)) {
                                            ClassifiAvatar(
                                                onClick = { menuState = true },
                                                text = {
                                                    Box(
                                                        modifier = Modifier,
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        ProvideTextStyle(textStyle) {
                                                            Text(
                                                                text = "Khalid Isah".getInitials(),
                                                            )
                                                        }
                                                    }
                                                },
                                            )
                                        }
                                    },
                                )

                            }

                            ClassifiBottomNavHost(
                                appState.bottomNavController,
                                onComposeFeed = onComposeFeed,
                            )
                        }
                    }

                    if (menuState) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = when (windowSizeClass.widthSizeClass) {
                                WindowWidthSizeClass.Expanded -> Alignment.Center
                                WindowWidthSizeClass.Medium -> Alignment.Center
                                WindowWidthSizeClass.Compact -> Alignment.TopCenter
                                else -> Alignment.TopCenter
                            }
                        ) {
                            val localModifier = Modifier
                            val configuration = LocalConfiguration.current

                            Popup(
                                alignment = Alignment.TopCenter,
                                offset = IntOffset(0, 20),
                                onDismissRequest = { menuState = false },
                            ) {
                                ClassifiMenu(
                                    modifier = localModifier
                                        .width(configuration.screenWidthDp.dp - 16.dp),
                                    header = {
                                        val textStyle =
                                            MaterialTheme.typography.labelMedium.copy(
                                                textAlign = TextAlign.Center,
                                                fontWeight = FontWeight.Bold
                                            )

                                        MenuHeader(
                                            onClick = {
                                                onOpenSettings()
                                                menuState = false
                                            },
                                        ) {
                                            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                                                val nameStyle =
                                                    MaterialTheme.typography.titleMedium
                                                val emailStyle =
                                                    MaterialTheme.typography.titleSmall
                                                Box(modifier = Modifier.padding(16.dp)) {
                                                    ClassifiAvatar(
                                                        onClick = { },
                                                        text = {
                                                            Box(
                                                                modifier = Modifier,
                                                                contentAlignment = Alignment.Center
                                                            ) {
                                                                ProvideTextStyle(textStyle) {
                                                                    Text(
                                                                        text = "Khalid Isah".getInitials(),
                                                                    )
                                                                }
                                                            }
                                                        },
                                                    )
                                                }
                                                Column(
                                                    modifier = Modifier.weight(1f),
                                                    verticalArrangement = Arrangement.SpaceEvenly
                                                ) {
                                                    ProvideTextStyle(value = nameStyle) {
                                                        Text(
                                                            text = "khalid Isah".uppercase()
                                                        )
                                                    }
                                                    ProvideTextStyle(value = emailStyle) {
                                                        Text(
                                                            text = "khalid.isah@gmail.com"
                                                        )
                                                    }

                                                    ProvideTextStyle(value = emailStyle) {
                                                        Text(
                                                            text = "Click here to view profile"
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    content = {
                                        val textStyle = MaterialTheme.typography.labelMedium

                                        MenuItem(
                                            icon = {
                                                Icon(
                                                    painter = painterResource(id = ClassifiIcons.Support),
                                                    contentDescription = stringResource(id = R.string.help_and_support)
                                                )
                                            },
                                            text = {
                                                ProvideTextStyle(textStyle) {
                                                    Text(
                                                        text = stringResource(id = R.string.help_and_support)
                                                    )
                                                }

                                            },
                                            onClick = {
                                                /*todo; on get support */
                                            }
                                        )

                                        MenuItem(
                                            icon = {
                                                Icon(
                                                    painter = painterResource(id = ClassifiIcons.Settings),
                                                    contentDescription = stringResource(id = R.string.my_account)
                                                )
                                            },
                                            text = {
                                                ProvideTextStyle(textStyle) {
                                                    Text(
                                                        text = stringResource(id = R.string.my_account)
                                                    )
                                                }

                                            },
                                            onClick = {
                                                onOpenSettings()
                                                menuState = false
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

            }

        }
    }
}
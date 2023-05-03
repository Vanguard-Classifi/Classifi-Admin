package com.vanguard.classifiadmin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.khalidtouch.core.designsystem.components.ClassifiAvatar
import com.khalidtouch.core.designsystem.components.ClassifiBackground
import com.khalidtouch.core.designsystem.components.ClassifiCenterTopAppBar
import com.khalidtouch.core.designsystem.components.ClassifiGradientBackground
import com.khalidtouch.core.designsystem.components.ClassifiOutlinedButton
import com.khalidtouch.core.designsystem.components.ClassifiToggleButton
import com.khalidtouch.core.designsystem.extensions.getInitials
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import com.khalidtouch.core.designsystem.theme.GradientColors
import com.khalidtouch.core.designsystem.theme.LocalGradientColors
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.navigation.ClassifiNavHost
import com.vanguard.classifiadmin.navigation.TopLevelDestination

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
fun ClassifiApp(
    windowSizeClass: WindowSizeClass,
    appState: ClassifiAppState = rememberClassifiAppState(windowSizeClass = windowSizeClass),
) {
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
                    if(appState.shouldShowBottomBar) {
                        ClassifiBottomBar(
                            destinations = appState.topLevelDestinations,
                            onNavigationToDestination = appState::navigationToTopLevelDestination,
                            currentDestination = appState.currentDestination,
                            modifier = Modifier.testTag("ClassifiBottomBar")
                        )
                    }
                }
            ) { padding ->
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .consumedWindowInsets(padding)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Horizontal
                            ),
                        ),
                ) {
                    if(appState.shouldShowNavRail) {
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
                       if(destination != null){
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
                                           onClick = { /*TODO*/ },
                                           content = {
                                               Text(
                                                   text = "KG100",
                                                   style = MaterialTheme.typography.labelMedium,
                                               )

                                               Icon(
                                                   painter = painterResource(id = ClassifiIcons.Dropdown),
                                                   contentDescription = null,
                                               )
                                           }
                                       )

                                       ClassifiToggleButton(
                                           checked = false,
                                           onCheckedChange = {},
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
                                   Icon(
                                       painter = painterResource(id = ClassifiIcons.BrandingBig),
                                       contentDescription = null,
                                   )
                               },
                               trailingIcon = {
                                   ClassifiAvatar(
                                       onClick = { /*TODO open settings */ },
                                       text = {
                                           Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                                               Text(
                                                   text = "Khalid Isah".getInitials(),
                                                   style = MaterialTheme.typography.labelMedium.copy(
                                                       textAlign = TextAlign.Center,
                                                       fontWeight = FontWeight.Bold
                                                   )
                                               )
                                           }
                                       },
                                   )
                               },
                           )

                       }

                        ClassifiNavHost(appState.navController)
                    }
                }
            }

        }
    }

}
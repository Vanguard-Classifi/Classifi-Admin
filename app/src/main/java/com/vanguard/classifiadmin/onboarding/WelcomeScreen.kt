package com.vanguard.classifiadmin.onboarding

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import com.vanguard.classifiadmin.R
import com.google.accompanist.navigation.animation.composable


@Composable
fun WelcomeScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = ClassifiIcons.Brand),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .width(300.dp)
                .height(300.dp)
                .padding(2.dp),
        )
        Spacer(Modifier.height(12.dp))
        Box {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center
                )
            )
        }
        Spacer(Modifier.height(12.dp))
        Box {
            Text(
                text = stringResource(id = R.string.more_efficiency_in_your_classroom),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center
                )
            )
        }
        Spacer(Modifier.height(12.dp))
    }
}

const val welcomeScreenNavigationRoute = "welcome_screen_navigation_route"

fun NavController.navigateToWelcomeScreen(
    onboardingViewModel: OnboardingViewModel,
) {
    this.navigate(welcomeScreenNavigationRoute) {
        launchSingleTop = true
        popUpTo(welcomeScreenNavigationRoute)
    }
    onboardingViewModel.updateCurrentDestination(OnboardingDestination.WELCOME)
}


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.welcomeScreen() {
    composable(
        route = welcomeScreenNavigationRoute
    ) {
        WelcomeScreen()
    }
}
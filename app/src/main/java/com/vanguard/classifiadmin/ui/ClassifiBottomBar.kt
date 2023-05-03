package com.vanguard.classifiadmin.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.khalidtouch.core.designsystem.components.ClassifiNavigationBar
import com.khalidtouch.core.designsystem.components.ClassifiNavigationBarItem
import com.khalidtouch.core.designsystem.icons.Icon
import com.vanguard.classifiadmin.navigation.TopLevelDestination

@Composable
fun ClassifiBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigationToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    ClassifiNavigationBar(modifier = modifier) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)

            ClassifiNavigationBarItem(
                selected = selected,
                onClick = { onNavigationToDestination(destination) },
                icon = {
                    val icon = if (selected) {
                        destination.selectedIcon
                    } else {
                        destination.unselectedIcon
                    }

                    when (icon) {
                        is Icon.ImageVectorIcon -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = null
                        )

                        is Icon.DrawableResourceIcon -> Icon(
                            painter = painterResource(id = icon.id),
                            contentDescription = null
                        )
                    }

                },
                label = { Text(stringResource(id = destination.iconTextId)) }
            )
        }
    }
}

fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false
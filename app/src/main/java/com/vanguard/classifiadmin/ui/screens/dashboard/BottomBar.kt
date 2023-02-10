package com.vanguard.classifiadmin.ui.screens.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vanguard.classifiadmin.ui.theme.Black100

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    destinations: List<DestinationItem> = DestinationItem.values().toList(),
    navController: NavController = rememberNavController()
) {
    BottomNavigation(
        modifier = modifier.heightIn(min = 72.dp),
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = Black100.copy(0.4f)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        destinations.forEach { each ->
            BottomNavigationItem(
                modifier = modifier,
                selected = currentDestination?.hierarchy?.any { it.route == each.screen } == true,
                icon = {
                    BottomBarItem(
                        modifier = modifier,
                        destinationItem = each,
                        selected = currentDestination?.hierarchy?.any { it.route == each.screen } == true
                    )
                },

                onClick = {
                    navController.navigate(each.screen) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    }
}

@Composable
fun BottomBarItem(
    modifier: Modifier = Modifier,
    destinationItem: DestinationItem,
    selected: Boolean,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            modifier = modifier
                .size(24.dp),
            painter = painterResource(destinationItem.icon),
            contentDescription = destinationItem.label,
            tint = if (selected) MaterialTheme.colors.primary else Black100.copy(0.4f),
        )

        Text(
            text = destinationItem.label,
            color = if (selected) MaterialTheme.colors.primary else Black100.copy(0.4f),
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(2.dp)
        )

    }

}


@Preview
@Composable
private fun BottomBarItemPreview() {
    BottomBarItem(
        destinationItem = DestinationItem.Students,
        selected = false,
    )
}
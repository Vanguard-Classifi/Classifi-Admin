package com.vanguard.classifiadmin.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.router.Destinations
import com.vanguard.classifiadmin.ui.screens.dashboard.DefaultAvatar

@Composable
fun DashboardMenuScreen(
    modifier: Modifier = Modifier,
    username: String,
    email: String,
    status: String,
    onSelectProfile: () -> Unit,
    onSelectMenu: (DashboardMenu) -> Unit,
    dashboardMenus: List<DashboardMenu> = DashboardMenu.values().toList(),
) {

    Card(
        modifier = modifier.padding(start = 32.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.onPrimary
    ) {
        Column(modifier = modifier) {
            DashboardMenuHeader(
                modifier = modifier,
                username = username,
                email = email,
                status = status,
                onSelect = onSelectProfile,
            )

            Divider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )

            LazyColumn(modifier = modifier, state = rememberLazyListState()) {
                items(dashboardMenus) { each ->
                    DashboardMenuItem(dashboardMenu = each, onSelect = onSelectMenu)
                }
            }
        }
    }
}

@Composable
fun DashboardMenuHeader(
    modifier: Modifier = Modifier,
    username: String,
    email: String,
    status: String,
    onSelect: () -> Unit,
) {
    val constraints = dashboardMenuHeaderConstraints(16.dp)
    val innerModifier = Modifier

    Surface(modifier = modifier.clickable { onSelect() }) {
        ConstraintLayout(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth(),
            constraintSet = constraints
        ) {
            DefaultAvatar(
                label = username,
                onClick = { },
                modifier = innerModifier.layoutId("avatar"),
                enabled = false,
            )

            Text(
                text = username.uppercase(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = innerModifier.layoutId("username")
            )

            Text(
                text = email.lowercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                modifier = innerModifier.layoutId("email")
            )

            Text(
                text = status,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                modifier = innerModifier.layoutId("status")
            )

        }
    }
}

private fun dashboardMenuHeaderConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val username = createRefFor("username")
        val email = createRefFor("email")
        val status = createRefFor("status")
        val avatar = createRefFor("avatar")

        constrain(username) {
            top.linkTo(parent.top, margin = margin)
            start.linkTo(avatar.end, margin = 8.dp)
        }

        constrain(email) {
            start.linkTo(username.start, margin = 0.dp)
            top.linkTo(username.bottom, margin = 4.dp)
        }

        constrain(status) {
            start.linkTo(email.start, margin = 0.dp)
            top.linkTo(email.bottom, margin = 8.dp)
        }

        constrain(avatar) {
            top.linkTo(username.top, margin = 0.dp)
            start.linkTo(parent.start, margin = margin)
        }
    }
}


@Composable
fun DashboardMenuItem(
    modifier: Modifier = Modifier,
    dashboardMenu: DashboardMenu,
    onSelect: (DashboardMenu) -> Unit,
) {
    Surface(
        modifier = modifier
            .clickable { onSelect(dashboardMenu) }
            .clip(RoundedCornerShape(2.dp)),
        shape = RoundedCornerShape(2.dp),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = dashboardMenu.icon),
                contentDescription = dashboardMenu.label,
                tint = MaterialTheme.colors.primary,
                modifier = modifier
                    .size(24.dp)
                    .padding(2.dp)
            )

            Text(
                text = dashboardMenu.label,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}


enum class DashboardMenu(val label: String, val icon: Int, val screen: String) {
    HelpAndSupport("Help & Support", R.drawable.icon_support, Destinations.support),
    MyCalendar("My Calendar", R.drawable.icon_calendar, Destinations.calendar),
    MyAccount("My Account", R.drawable.icon_settings, Destinations.account),
}


@Preview
@Composable
private fun DashboardMenuItemPreview() {
    DashboardMenuItem(
        dashboardMenu = DashboardMenu.HelpAndSupport,
        onSelect = {}
    )
}
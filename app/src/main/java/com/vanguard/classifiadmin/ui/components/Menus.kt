package com.vanguard.classifiadmin.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.router.Destinations

@Composable
fun DashboardMenu(
    modifier: Modifier = Modifier,
) {


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
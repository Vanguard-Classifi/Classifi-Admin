package com.khalidtouch.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ClassifiComposeFeedBottomBar(
    modifier: Modifier = Modifier,
    filter: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = modifier
            .weight(1f)
            .padding(ClassifiBottomBarDefaults.containerPadding)) {
            Row {
                actions()
            }
        }

        Box {
            filter()
        }
    }
}

object ClassifiBottomBarDefaults {
    val containerPadding = 8.dp

}
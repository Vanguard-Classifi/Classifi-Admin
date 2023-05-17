package com.khalidtouch.core.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khalidtouch.core.designsystem.extensions.customTabIndicatorOffset

@Composable
fun ClassifiScrollableTabRow(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    pageSize: Int,
    tabs: @Composable () -> Unit,
) {
    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(pageSize) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }

    ScrollableTabRow(
        modifier = modifier,
        edgePadding = 0.dp,
        selectedTabIndex = selectedTabIndex,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.customTabIndicatorOffset(
                    currentTabPosition = tabPositions[selectedTabIndex],
                    tabWidth = tabWidths[selectedTabIndex]
                ),
                height = 8.dp,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
        tabs = tabs
    )
}


@Composable
fun ClassifiTabRow(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    tabs: @Composable () -> Unit,
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                height = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
        tabs = tabs,
    )
}

@Composable
fun ClassifiTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    Tab(
        selected = selected,
        onClick = onClick,
        enabled = enabled,
        selectedContentColor = MaterialTheme.colorScheme.onPrimary,
        unselectedContentColor = MaterialTheme.colorScheme.onPrimary.copy(ClassifiTabDefaults.TabAlpha),
        modifier = modifier,
        text = {
            val style = MaterialTheme.typography.labelLarge.copy(textAlign = TextAlign.Center)
            ProvideTextStyle(
                value = style,
                content = {
                    ClassifiTabContent(
                        selected = selected,
                        text = text,
                        leadingIcon = leadingIcon
                    )
                }
            )
        }
    )
}


@Composable
fun ClassifiTabContent(
    modifier: Modifier = Modifier,
    selected: Boolean,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    if (leadingIcon != null) {
        Box(
            Modifier
                .sizeIn(maxHeight = ButtonDefaults.IconSize),
            contentAlignment = Alignment.CenterStart
        ) {
            leadingIcon()
        }

        if (selected) {
            Box(
                modifier = Modifier.padding(
                    start = ButtonDefaults.IconSpacing.times(3f)
                ),
                contentAlignment = Alignment.CenterStart
            ) {
                text()
            }
        }
    } else {
        Box(
            modifier = Modifier.padding(
                top = ClassifiTabDefaults.TabTopPadding,
                start = 0.dp
            ),
        ) {
            text()
        }
    }
}


object ClassifiTabDefaults {
    val TabTopPadding = 7.dp

    val TabAlpha = 0.4f
}
package com.khalidtouch.core.designsystem.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemColors
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.ClassifiNavigationBarItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon,
    enabled: Boolean= true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
){
  NavigationBarItem(
      selected = selected,
      onClick = onClick,
      icon = if(selected) selectedIcon else icon,
      modifier = modifier,
      enabled = enabled,
      label = label,
      alwaysShowLabel = alwaysShowLabel,
      colors = NavigationBarItemDefaults.colors(
          selectedIconColor = ClassifiNavigationDefaults.navigationSelectedItemColor(),
          unselectedIconColor = ClassifiNavigationDefaults.navigationContentColor(),
          selectedTextColor = ClassifiNavigationDefaults.navigationSelectedItemColor(),
          unselectedTextColor = ClassifiNavigationDefaults.navigationContentColor(),
          indicatorColor = ClassifiNavigationDefaults.navigationIndicatorColor(),
      )
  )
}

@Composable
fun ClassifiNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        contentColor = ClassifiNavigationDefaults.navigationContentColor(),
        tonalElevation = 0.dp,
        content = content,
    )
}


@Composable
fun ClassifiNavigationRail(
    modifier: Modifier = Modifier,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
){
    NavigationRail(
        modifier = modifier,
        containerColor = Color.Transparent,
        contentColor = ClassifiNavigationDefaults.navigationContentColor(),
        header = header,
        content = content,
    )
}

@Composable
fun ClassifiNavigationRailItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
) {
  NavigationRailItem(
      selected = selected,
      onClick = onClick,
      icon = if(selected) selectedIcon else icon,
      enabled = enabled,
      label = label,
      alwaysShowLabel = alwaysShowLabel,
      modifier = modifier,
      colors = NavigationRailItemDefaults.colors(
          selectedIconColor = ClassifiNavigationDefaults.navigationSelectedItemColor(),
          unselectedIconColor = ClassifiNavigationDefaults.navigationContentColor(),
          selectedTextColor = ClassifiNavigationDefaults.navigationSelectedItemColor(),
          unselectedTextColor = ClassifiNavigationDefaults.navigationContentColor(),
          indicatorColor = ClassifiNavigationDefaults.navigationIndicatorColor(),
      )
  )
}


object ClassifiNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}
package com.vanguard.classifiadmin.navigation

import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import com.khalidtouch.core.designsystem.icons.Icon
import com.vanguard.classifiadmin.R

enum class TopLevelDestination(
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    Feeds(
        selectedIcon = Icon.DrawableResourceIcon(ClassifiIcons.FeedsSolid),
        unselectedIcon = Icon.DrawableResourceIcon(ClassifiIcons.Feeds),
        iconTextId = R.string.feeds,
        titleTextId = R.string.feeds
    ),

    Students(
        selectedIcon = Icon.DrawableResourceIcon(ClassifiIcons.Students),
        unselectedIcon = Icon.DrawableResourceIcon(ClassifiIcons.StudentsOutline),
        iconTextId = R.string.students,
        titleTextId = R.string.students
    ),
}
package com.vanguard.classifiadmin.ui.screens.feeds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.domain.helpers.generateColorFromUserName
import com.vanguard.classifiadmin.ui.components.LocalIconButton
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import kotlinx.coroutines.delay

@Composable
fun UpcomingActivities(
    modifier: Modifier = Modifier,
    username: String,
    expanded: Boolean = false,
    onToggleExpand: () -> Unit,
) {
    val innerModifier = Modifier
    var showUpcomingActivities by remember { mutableStateOf(false) }
    val constraints = upcomingActivitiesConstraint(8.dp, showUpcomingActivities)
    val backgroundColor = Color(generateColorFromUserName(username))

    LaunchedEffect(showUpcomingActivities) {
        //reset upcoming activities state to off
        if (showUpcomingActivities) {
            delay(5000)
            showUpcomingActivities = false
        }
    }

    if(expanded) {
        Card(
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            backgroundColor = backgroundColor.copy(0.5f),
            elevation = 0.dp, shape = RoundedCornerShape(16.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxWidth(),
                constraintSet = constraints,
            ) {
                UpcomingActivitiesHeading(
                    modifier = innerModifier.layoutId("upcomingHeading"),
                    heading = stringResource(id = R.string.upcoming_activities),
                    onShow = {
                        showUpcomingActivities = !showUpcomingActivities
                    },
                    onToggleExpand = onToggleExpand,
                )

                if (showUpcomingActivities) {
                    NoUpcomingActivity(
                        modifier = innerModifier.layoutId("activityBody"),
                        message = stringResource(id = R.string.no_upcoming_activities)
                    )
                }

                Text(
                    text = stringResource(id = R.string.add_students_heading),
                    modifier = innerModifier.layoutId("addStudent"),
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.onPrimary.copy(0.5f),
                )

                ClassLinkBar(
                    backgroundColor = backgroundColor,
                    onCopyLink = { /*TODO*/ },
                    modifier = innerModifier.layoutId("classCodeBox"),
                )
            }
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp, end = 16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            LocalIconButton(
                onClick = onToggleExpand,
                icon = R.drawable.icon_calendar,
                iconTint = MaterialTheme.colors.onPrimary,
                iconSize = 20.dp,
                size = 28.dp,
                contentDescription = stringResource(id = R.string.show_activities),
                modifier = modifier,
                onToggleExpand = onToggleExpand,
            )
        }
    }
}


private fun upcomingActivitiesConstraint(
    margin: Dp,
    showUpcomingActivities: Boolean = false
): ConstraintSet {
    return ConstraintSet {
        val upcomingHeading = createRefFor("upcomingHeading")
        val activityBody = createRefFor("activityBody")
        val addStudent = createRefFor("addStudent")
        val classCodeBox = createRefFor("classCodeBox")

        constrain(upcomingHeading) {
            start.linkTo(parent.start, margin = margin)
            end.linkTo(parent.end, margin = margin)
            top.linkTo(parent.top, margin = margin)
            width = Dimension.fillToConstraints
        }

        constrain(activityBody) {
            top.linkTo(upcomingHeading.bottom, margin = 8.dp)
            start.linkTo(upcomingHeading.start, margin = 0.dp)
            end.linkTo(upcomingHeading.end, margin = 0.dp)
            width = Dimension.fillToConstraints
        }

        constrain(addStudent) {
            start.linkTo(activityBody.start, margin = 0.dp)
            if (showUpcomingActivities) {
                top.linkTo(activityBody.bottom, margin = 8.dp)
            } else {
                top.linkTo(upcomingHeading.bottom, margin = 8.dp)
            }
        }

        constrain(classCodeBox) {
            start.linkTo(parent.start, margin = 0.dp)
            end.linkTo(parent.end, margin = 0.dp)
            top.linkTo(addStudent.bottom, margin = 4.dp)
            width = Dimension.fillToConstraints
        }

    }
}


@Composable
fun NoUpcomingActivity(
    modifier: Modifier = Modifier,
    message: String = stringResource(id = R.string.no_upcoming_activities),
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier,
        )
    }
}


@Composable
fun UpcomingActivitiesHeading(
    modifier: Modifier = Modifier,
    heading: String,
    onShow: () -> Unit,
    onToggleExpand: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = heading.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(8.dp)
        )

        LocalIconButton(
            onClick = onShow,
            icon = R.drawable.icon_calendar,
            iconTint = MaterialTheme.colors.onPrimary,
            iconSize = 20.dp,
            size = 28.dp,
            contentDescription = stringResource(id = R.string.show_activities),
            modifier = modifier,
            onToggleExpand = onToggleExpand,
        )
    }
}


@Composable
fun ClassLinkBar(
    modifier: Modifier = Modifier,
    onCopyLink: () -> Unit,
    color: Color = MaterialTheme.colors.onPrimary,
    backgroundColor: Color = MaterialTheme.colors.primary,
) {
    val innerModifier = Modifier
    val constraints = classLinkBarConstraints(8.dp)

    Card(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        backgroundColor = backgroundColor,
        elevation = 2.dp, shape = RoundedCornerShape(16.dp)
    ) {
        BoxWithConstraints(modifier = modifier) {
            ConstraintLayout(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                constraintSet = constraints,
            ) {
                Text(
                    text = stringResource(id = R.string.class_code),
                    fontSize = 12.sp,
                    modifier = innerModifier.layoutId("title"),
                    color = color.copy(0.5f)
                )
                Text(
                    text = "2-43/Year",
                    fontSize = 12.sp,
                    modifier = innerModifier.layoutId("link"),
                    color = color,
                )

                RoundedIconButton(
                    icon = R.drawable.icon_copy,
                    onClick = onCopyLink,
                    modifier = innerModifier.layoutId("icon"),
                    tint = MaterialTheme.colors.onPrimary,
                )
            }
        }
    }
}


private fun classLinkBarConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val icon = createRefFor("icon")
        val title = createRefFor("title")
        val link = createRefFor("link")

        constrain(icon) {
            top.linkTo(title.top, 0.dp)
            bottom.linkTo(link.bottom, 0.dp)
            end.linkTo(parent.end, margin)
        }

        constrain(title) {
            start.linkTo(parent.start, margin)
            top.linkTo(parent.top, margin)
        }

        constrain(link) {
            top.linkTo(title.bottom, 4.dp)
            start.linkTo(title.start, 0.dp)
        }
    }
}


@Composable
@Preview
private fun UpcomingActivitiesHeadingPreview() {
    UpcomingActivitiesHeading(
        heading = "Upcoming Activities",
        onShow = {},
        onToggleExpand = {}
    )
}


@Preview
@Composable
private fun NoUpcomingActivityPreview() {
    NoUpcomingActivity()
}

@Composable
@Preview
private fun UpcomingActivitiesPreview() {
    UpcomingActivities(
        username = "Hamza Jesim",
        onToggleExpand = {}
    )
}
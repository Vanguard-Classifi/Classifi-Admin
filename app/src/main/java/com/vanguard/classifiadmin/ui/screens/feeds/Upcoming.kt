package com.vanguard.classifiadmin.ui.screens.feeds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.domain.helpers.generateColorFromUserName
import com.vanguard.classifiadmin.ui.components.LocalIconButton

@Composable
fun UpcomingActivities(
    modifier: Modifier = Modifier,
    username: String,
) {

    val innerModifier = Modifier
    val constraints = upcomingActivitiesConstraint()

    Surface(modifier = modifier, color = Color(generateColorFromUserName(username))) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(72.dp)
        ) {
            ConstraintLayout(
                modifier = modifier,
                constraintSet = constraints,
            ) {


            }
        }
    }
}


private fun upcomingActivitiesConstraint(): ConstraintSet {
    return ConstraintSet {
        val upcomingHeading = createRefFor("upcomingHeading")
        val noActivityMessage = createRefFor("noActivityMessage")
        val addStudent = createRefFor("addStudent")
        val classCodeBox = createRefFor("classCodeBox")
        val seeMore = createRefFor("seeMore")

        constrain(upcomingHeading) {
            start.linkTo(parent.start, margin = 8.dp)
            end.linkTo(parent.end, margin = 8.dp)
            top.linkTo(parent.top, margin = 8.dp)
            width = Dimension.fillToConstraints
        }

        constrain(noActivityMessage) {
            top.linkTo(upcomingHeading.bottom, margin = 32.dp)
            start.linkTo(upcomingHeading.start, margin = 0.dp)
            end.linkTo(upcomingHeading.end, margin = 0.dp)
            width = Dimension.fillToConstraints
        }

        constrain(addStudent) {
            start.linkTo(upcomingHeading.start, margin = 0.dp)
            top.linkTo(noActivityMessage.bottom, margin = 32.dp)
        }

        constrain(classCodeBox) {
            start.linkTo(upcomingHeading.start, margin = 0.dp)
            end.linkTo(upcomingHeading.end, margin = 0.dp)
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
        )

    }
}


@Composable
@Preview
private fun UpcomingActivitiesHeadingPreview() {
    UpcomingActivitiesHeading(
        heading = "Upcoming Activities",
        onShow = {}
    )
}


@Preview
@Composable
private fun NoUpcomingActivityPreview() {
    NoUpcomingActivity()
}
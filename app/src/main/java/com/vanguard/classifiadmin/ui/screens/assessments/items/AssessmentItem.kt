package com.vanguard.classifiadmin.ui.screens.assessments.items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.AssessmentModel
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentSubjectRow
import com.vanguard.classifiadmin.ui.screens.assessments.DateIcon
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.ui.theme.Green100


@Composable
fun AssessmentItem(
    modifier: Modifier = Modifier,
    assessment: AssessmentModel,
    onOptions: (AssessmentModel) -> Unit,
    onSelectAssessment: (AssessmentModel) -> Unit,
) {
    val constraints = assessmentItemConstraints(8.dp)
    val innerModifier = Modifier

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .clickable(onClick = { onSelectAssessment(assessment) }),
        shape = RoundedCornerShape(16.dp),
        elevation = 2.dp,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(0.1f)
        )
    ) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            constraintSet = constraints
        ) {
            Box(
                modifier = innerModifier
                    .layoutId("attemptStatus")
                    .width(3.dp)
                    .background(
                        color = if (assessment.attempts.isNotEmpty()) Green100 else Black100.copy(
                            0.5f
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
            )

            DateIcon(
                title = assessment.endTime.orEmpty(),
                subtitle = assessment.endTime.orEmpty(),
                modifier = innerModifier.layoutId("dateIcon")
            )

            Text(
                text = assessment.name?.uppercase().orEmpty(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                modifier = innerModifier
                    .layoutId("title")
                    .widthIn(max = 200.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (assessment.endTime != null) {
                Text(
                    text = stringResource(id = R.string.closed).uppercase(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.error,
                    modifier = innerModifier
                        .layoutId("expiry")
                )
            }

            AssessmentSubjectRow(
                assessment = assessment,
                modifier = innerModifier.layoutId("subjectAndType")
            )


            RoundedIconButton(
                modifier = innerModifier.layoutId("extras"),
                onClick = { onOptions(assessment) },
                icon = R.drawable.icon_options_horizontal,
            )
        }
    }
}

private fun assessmentItemConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val dateIcon = createRefFor("dateIcon")
        val attemptStatus = createRefFor("attemptStatus")
        val title = createRefFor("title")
        val expiry = createRefFor("expiry")
        val subjectAndType = createRefFor("subjectAndType")
        val extras = createRefFor("extras")

        constrain(attemptStatus) {
            top.linkTo(parent.top, margin = 0.dp)
            bottom.linkTo(parent.bottom, margin = 0.dp)
            start.linkTo(parent.start, margin = 0.dp)
            height = Dimension.fillToConstraints
        }

        constrain(dateIcon) {
            start.linkTo(attemptStatus.end, margin = 4.dp)
            top.linkTo(title.top, margin = 0.dp)
            bottom.linkTo(subjectAndType.bottom, margin = 0.dp)
        }

        constrain(title) {
            top.linkTo(parent.top, margin = 4.dp)
            start.linkTo(dateIcon.end, margin = 8.dp)
        }

        constrain(expiry) {
            start.linkTo(title.start, margin = 0.dp)
            top.linkTo(title.bottom, margin = 4.dp)
        }

        constrain(subjectAndType) {
            start.linkTo(title.start, margin = 0.dp)
            top.linkTo(expiry.bottom, margin = 4.dp)
        }

        constrain(extras) {
            top.linkTo(parent.top, margin = 0.dp)
            bottom.linkTo(parent.bottom, margin = 0.dp)
            end.linkTo(parent.end, margin = 4.dp)
        }
    }
}

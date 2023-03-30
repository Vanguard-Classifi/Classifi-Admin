package com.vanguard.classifiadmin.ui.screens.assessments.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.AssessmentModel
import com.vanguard.classifiadmin.domain.helpers.toSimpleDate
import com.vanguard.classifiadmin.ui.components.PrimaryTextButtonFillWidth
import com.vanguard.classifiadmin.ui.theme.Black100

@Composable
fun AssessmentCreationBottomSheetInfo(
    modifier: Modifier = Modifier,
    onDone: () -> Unit,
    assessment: AssessmentModel,
) {
    BoxWithConstraints(modifier = modifier) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = assessment.name.orEmpty(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Black100,
            )

            Text(
                text = "Created: ${assessment.lastModified.orEmpty()}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Black100,
            )

            Spacer(modifier = modifier.height(32.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.status),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black100,
                )
                Spacer(modifier = modifier.width(32.dp))
                Text(
                    text = "OPEN",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black100,
                )
            }
            Spacer(modifier = modifier.height(12.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.subject),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black100,
                )
                Spacer(modifier = modifier.width(32.dp))
                Text(
                    text = assessment.subjectName.orEmpty(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black100,
                )
            }
            Spacer(modifier = modifier.height(12.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.start_date),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black100,
                )
                Spacer(modifier = modifier.width(32.dp))
                Text(
                    text = assessment.startDate?.toSimpleDate().orEmpty(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black100,
                )
            }
            Spacer(modifier = modifier.height(12.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.end_date),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black100,
                )
                Spacer(modifier = modifier.width(32.dp))
                Text(
                    text = assessment.endDate?.toSimpleDate().orEmpty(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black100,
                )
            }
            Spacer(modifier = modifier.height(12.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.assesment_type),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black100,
                )
                Spacer(modifier = modifier.width(32.dp))
                Text(
                    text = assessment.type?.uppercase().orEmpty(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black100,
                )
            }
            Spacer(modifier = modifier.height(12.dp))

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.questions),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black100,
                )
                Spacer(modifier = modifier.width(32.dp))
                Text(
                    text = "0",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Black100,
                )
            }
            Spacer(modifier = modifier.height(12.dp))

            PrimaryTextButtonFillWidth(label = stringResource(id = R.string.done), onClick = onDone)
            Spacer(modifier = modifier.height(12.dp))
        }
    }
}

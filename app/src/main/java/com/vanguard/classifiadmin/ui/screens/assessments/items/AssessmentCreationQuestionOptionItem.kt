package com.vanguard.classifiadmin.ui.screens.assessments.items

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentCreationQuestionOptionFeature


@Composable
fun AssessmentCreationQuestionOptionItem (
    modifier: Modifier = Modifier,
    onSelect: (AssessmentCreationQuestionOptionFeature) -> Unit,
    feature: AssessmentCreationQuestionOptionFeature = AssessmentCreationQuestionOptionFeature.EditQuestion,
){
    Surface(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onSelect(feature) },
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(0.8f),
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = feature.icon),
                contentDescription = stringResource(id = R.string.icon),
                tint = MaterialTheme.colors.primary,
                modifier = modifier.size(24.dp)
            )

            Text(
                text = feature.title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                modifier = modifier.padding(16.dp)
            )
        }
    }
}
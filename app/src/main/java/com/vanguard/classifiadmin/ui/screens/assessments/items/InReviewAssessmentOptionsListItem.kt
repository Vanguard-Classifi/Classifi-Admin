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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.ui.screens.assessments.InReviewAssessmentBottomSheetOption


@Composable
fun InReviewAssessmentOptionsListItem(
    modifier: Modifier = Modifier,
    inReviewAssessmentOption: InReviewAssessmentBottomSheetOption,
    onSelect: (InReviewAssessmentBottomSheetOption) -> Unit,
) {
    Surface(
        modifier = modifier
            .padding(8.dp)
            .clickable { onSelect(inReviewAssessmentOption) }
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary,
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = inReviewAssessmentOption.icon),
                contentDescription = inReviewAssessmentOption.label,
                tint = MaterialTheme.colors.primary,
                modifier = modifier
                    .size(24.dp)
                    .padding(2.dp)
            )

            Text(
                text = inReviewAssessmentOption.label,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}

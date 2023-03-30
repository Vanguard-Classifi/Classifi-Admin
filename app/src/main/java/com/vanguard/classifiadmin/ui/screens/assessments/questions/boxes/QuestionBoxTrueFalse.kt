package com.vanguard.classifiadmin.ui.screens.assessments.questions.boxes

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionOptionTrueFalse
import com.vanguard.classifiadmin.ui.theme.Black100


@Composable
fun QuestionBoxTrueFalse(
    modifier: Modifier = Modifier,
    option: QuestionOptionTrueFalse,
    onSelect: (QuestionOptionTrueFalse) -> Unit,
    isLeft: Boolean = false,
    selected: Boolean = false,
    rowWidth: Dp,
) {
    Surface(
        modifier = modifier
            .padding(0.dp)
            .width(rowWidth.times(0.45f)),
        shape = RoundedCornerShape(
            topStartPercent = if (isLeft) 50 else 0,
            bottomStartPercent = if (isLeft) 50 else 0,
            topEndPercent = if (isLeft) 0 else 50,
            bottomEndPercent = if (isLeft) 0 else 50,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(0.5f),
        ),
        color =
        animateColorAsState(
            targetValue =
            if (selected) MaterialTheme.colors.primary else Color.Transparent
        ).value
    ) {
        TextButton(
            onClick = { onSelect(option) },
            modifier = modifier
                .padding(0.dp)
                .width(rowWidth.times(0.45f)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (selected) MaterialTheme.colors.primary else Color.Transparent,
            )
        ) {
            if (isLeft) {
                if (selected) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_mark),
                        contentDescription = stringResource(id = R.string.mark_as_answer),
                        tint = if (selected) MaterialTheme.colors.onPrimary else Black100.copy(
                            0.5f
                        )
                    )
                    Spacer(modifier = modifier.width(16.dp))
                }
                Spacer(modifier = modifier.width(16.dp))
                Text(
                    text = option.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selected) MaterialTheme.colors.onPrimary else Black100.copy(
                        0.5f
                    ),
                )
                Spacer(modifier = modifier.weight(1f))
            } else {
                Spacer(modifier = modifier.weight(1f))
                Text(
                    text = option.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selected) MaterialTheme.colors.onPrimary else Black100.copy(
                        0.5f
                    ),
                )
                Spacer(modifier = modifier.width(16.dp))
                if (selected) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_mark),
                        contentDescription = stringResource(id = R.string.mark_as_answer),
                        tint = if (selected) MaterialTheme.colors.onPrimary else Black100.copy(
                            0.5f
                        )
                    )
                    Spacer(modifier = modifier.width(16.dp))
                }
            }
        }
    }
}
package com.vanguard.classifiadmin.ui.screens.assessments.questions.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.ui.theme.Black100

@Composable
fun ShortAnswerItem(
    modifier: Modifier = Modifier,
    answer: String,
) {
    Column(
        modifier = modifier.padding(0.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.answer).uppercase(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Black100,
            modifier = modifier.padding(top = 4.dp, bottom = 4.dp, start = 0.dp)
        )


        Text(
            text = answer,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Black100,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier.padding(top = 4.dp, bottom = 4.dp, start = 0.dp)
        )

    }
}

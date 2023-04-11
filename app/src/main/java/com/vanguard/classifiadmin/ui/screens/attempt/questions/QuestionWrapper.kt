package com.vanguard.classifiadmin.ui.screens.attempt.questions

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.ui.theme.Black100

@Composable
fun QuestionWrapper(
    modifier: Modifier = Modifier,
    title: String,
    @StringRes directionsRes: Int? = null,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        QuestionTitle(title = title)
        directionsRes?.let {
            Spacer(modifier = Modifier.height(8.dp))
            QuestionDirections(directions = it)
        }
        Spacer(modifier = Modifier.height(18.dp))
        content()
    }
}


@Composable
fun QuestionTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Black100.copy(0.8f),
        modifier = modifier
            .fillMaxWidth()
            .background(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colors.primary.copy(0.1f)
            )
            .padding(
                vertical = 24.dp,
                horizontal = 16.dp
            )
    )
}

@Composable
fun QuestionDirections(
    modifier: Modifier = Modifier,
    @StringRes directions: Int,
) {
    Text(
        text = stringResource(id = directions),
        color = Black100.copy(0.8f),
        fontSize = 12.sp,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    )
}
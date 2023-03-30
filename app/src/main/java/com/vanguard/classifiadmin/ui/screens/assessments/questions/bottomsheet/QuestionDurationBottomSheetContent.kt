package com.vanguard.classifiadmin.ui.screens.assessments.questions.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.domain.extensions.orZeroString
import com.vanguard.classifiadmin.ui.components.PrimaryTextButtonFillWidth
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel

@Composable
fun QuestionDurationBottomSheetContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onClose: () -> Unit,
) {
    val questionDurationCreateQuestion by viewModel.questionDurationCreateQuestion.collectAsState()

    Surface(modifier = modifier) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(
                        min = 32.dp
                    )
                    .clip(
                        RoundedCornerShape(8.dp)
                    ),
                value = questionDurationCreateQuestion.orZeroString(),
                onValueChange = viewModel::onQuestionDurationCreateQuestionChanged,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.assessment_duration),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Black100.copy(0.5f),
                    )
                },
                shape = RoundedCornerShape(8.dp),
                textStyle = TextStyle(
                    color = Black100,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                isError = false,
            )

            Spacer(modifier = modifier.height(16.dp))

            PrimaryTextButtonFillWidth(
                label = stringResource(id = R.string.done), onClick = { onClose() })
        }
    }
}
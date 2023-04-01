package com.vanguard.classifiadmin.ui.screens.assessments.items

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.QuestionModel
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionDifficulty
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionType
import com.vanguard.classifiadmin.ui.screens.assessments.questions.items.MultiChoiceItem
import com.vanguard.classifiadmin.ui.screens.assessments.questions.items.ShortAnswerItem
import com.vanguard.classifiadmin.ui.screens.assessments.questions.items.TrueFalseItem
import com.vanguard.classifiadmin.ui.theme.Black100

@Composable
fun QuestionItemAssessmentCreation(
    modifier: Modifier = Modifier,
    question: QuestionModel,
    onOptions: (QuestionModel) -> Unit,
) {
    val TAG = "QuestionItemAssessmentCreation"
    val innerModifier = Modifier
    val constraints = QuestionItemAssessmentCreationConstraints(4.dp)
    val answer = if (question.answers.isNotEmpty()) question.answers.first()
    else ""

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        contentAlignment = Alignment.TopStart,
    ) {
        val maxWidth = maxWidth

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            elevation = 2.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxWidth(),
                constraintSet = constraints,
            ) {
                Text(
                    text = question.text.orEmpty(),
                    fontSize = 12.sp,
                    color = Black100,
                    modifier = innerModifier
                        .layoutId("question")
                        .width(maxWidth.times(.825f))
                )

                RoundedIconButton(
                    onClick = { onOptions(question) },
                    tint = Black100,
                    modifier = innerModifier.layoutId("more"),
                    icon = R.drawable.icon_options_horizontal
                )

                //option
                when (question.type) {
                    QuestionType.MultiChoice.title -> {
                        Log.e(TAG, "QuestionItemAssessmentCreation: type is MultiChoice")
                        MultiChoiceItem(
                            answer = answer,
                            contents = listOf(
                                question.optionA.orEmpty(),
                                question.optionB.orEmpty(),
                                question.optionC.orEmpty(),
                                question.optionD.orEmpty()
                            ),
                            modifier = innerModifier
                                .layoutId("options")
                                .width(maxWidth.times(.825f))
                        )
                    }

                    QuestionType.TrueFalse.title -> {
                        Log.e(TAG, "QuestionItemAssessmentCreation: type is TrueFalse")
                        TrueFalseItem(
                            answer = answer,
                            modifier = innerModifier.layoutId("options")
                        )
                    }

                    QuestionType.Short.title -> {
                        Log.e(TAG, "QuestionItemAssessmentCreation: type is Short")
                        ShortAnswerItem(
                            answer = answer,
                            modifier = innerModifier
                                .layoutId("options")
                                .width(maxWidth.times(.825f))
                        )
                    }

                    QuestionType.Essay.title -> {
                        Log.e(TAG, "QuestionItemAssessmentCreation: type is Essay")
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Surface(
                modifier = modifier,
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(
                    topEndPercent = 50,
                    bottomEndPercent = 50,
                )
            ) {
                Box(
                    modifier = modifier
                        .height(28.dp)
                        .widthIn(max = 35.dp)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = question.position.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.onPrimary,
                    )
                }
            }
        }
    }
}

private fun QuestionItemAssessmentCreationConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val question = createRefFor("question")
        val options = createRefFor("options")
        val more = createRefFor("more")

        constrain(question) {
            start.linkTo(parent.start, 26.dp)
            top.linkTo(parent.top, 8.dp)
        }
        constrain(more) {
            top.linkTo(question.top, 0.dp)
            end.linkTo(parent.end, 4.dp)
        }
        constrain(options) {
            start.linkTo(question.start, 0.dp)
            top.linkTo(question.bottom, 8.dp)
            bottom.linkTo(parent.bottom, 4.dp)
        }
    }
}

@Composable
@Preview
private fun QuestionItemAssessmentCreationPreview() {
    QuestionItemAssessmentCreation(
        question = QuestionModel(
            questionId = "429u49fkef",
            type = QuestionType.Short.name,
            difficulty = QuestionDifficulty.Easy.name,
            text = "What is a school used for in the sense of educating the young ones amongs the less -privileged audience less -privileged audience " +
                    "less -privileged audience less -privileged audience less -privileged audience less -privileged audience less -privileged audience less -privileged audience less -privileged audience less -privileged audience less -privileged audience?",
            optionA = "A place of learning yes so learning yes so  learning yes so  learning yes so  learning yes so ",
            optionB = "A place of worship",
            optionC = "A good place to live",
            optionD = "A place to sleep",
            answers = arrayListOf("The meaning is indeterminate is indeterminate is indeterminate is indeterminate is indeterminate is indeterminate is indeterminate is indeterminate is indeterminate is indeterminate is indeterminate is indeterminate"),

            ),
        onOptions = {}
    )
}

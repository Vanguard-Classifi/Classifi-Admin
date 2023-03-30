package com.vanguard.classifiadmin.ui.screens.assessments.items

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.data.local.models.QuestionModel
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.screens.assessments.questions.QuestionType
import com.vanguard.classifiadmin.ui.screens.assessments.questions.items.MultiChoiceItem
import com.vanguard.classifiadmin.ui.screens.assessments.questions.items.ShortAnswerItem
import com.vanguard.classifiadmin.ui.screens.assessments.questions.items.TrueFalseItem
import com.vanguard.classifiadmin.ui.theme.Black100

@Composable
fun QuestionItemAssessmentCreation(
    modifier: Modifier = Modifier,
    question: QuestionModel,
) {
    val innerModifier = Modifier
    val constraints = QuestionItemAssessmentCreationConstraints(4.dp)
    val answer = if (question.answers.isNotEmpty()) question.answers.first()
    else ""

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        val maxWidth = maxWidth

        Card(
            modifier = modifier,
            elevation = 2.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            ConstraintLayout(
                modifier = modifier,
                constraintSet = constraints,
            ) {
                Text(
                    text = question.text.orEmpty(),
                    fontSize = 12.sp,
                    color = Black100,
                    modifier = innerModifier.layoutId("question")
                )

                RoundedIconButton(
                    onClick = {},
                    tint = Black100,
                    modifier = innerModifier.layoutId("more"),
                    icon = R.drawable.icon_options_horizontal
                )

                //option
                when (question.type) {
                    QuestionType.MultiChoice.name -> {
                        MultiChoiceItem(
                            answer = answer,
                            contents = listOf(
                                question.optionA.orEmpty(),
                                question.optionB.orEmpty(),
                                question.optionC.orEmpty(),
                                question.optionD.orEmpty()
                            ),
                        )
                    }

                    QuestionType.TrueFalse.name -> {
                        TrueFalseItem(
                            answer = answer
                        )
                    }

                    QuestionType.Short.name -> {
                        ShortAnswerItem(
                            answer = answer
                        )
                    }

                    QuestionType.Essay.name -> {

                    }
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
            start.linkTo(parent.start, 8.dp)
            top.linkTo(parent.top, 4.dp)
        }
        constrain(more) {
            top.linkTo(question.top, 0.dp)
            end.linkTo(parent.end, 4.dp)
        }
        constrain(options) {
            start.linkTo(parent.start, 0.dp)
            end.linkTo(parent.end, 0.dp)
            top.linkTo(question.bottom, 8.dp)
            bottom.linkTo(parent.bottom, 4.dp)
        }
    }
}

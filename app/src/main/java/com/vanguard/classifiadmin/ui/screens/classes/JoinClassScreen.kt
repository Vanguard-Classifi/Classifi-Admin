package com.vanguard.classifiadmin.ui.screens.classes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.ui.components.RoundedIconButton
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun JoinClassScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onClose: () -> Unit,
) {
    val constraints = joinClassScreenConstraint(16.dp)
    val innerModifier = Modifier
    val selectedJoinClassOption by viewModel.selectedJoinClassOption.collectAsState()

    Card(
        modifier = modifier.padding(horizontal = 8.dp),
        elevation = 8.dp, shape = RoundedCornerShape(16.dp)
    ) {
        BoxWithConstraints(modifier = modifier) {
            ConstraintLayout(
                modifier = modifier.fillMaxWidth(),
                constraintSet = constraints,
            ) {
                Text(
                    text = stringResource(id = R.string.add_class),
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = innerModifier.layoutId("title")
                )

                RoundedIconButton(
                    onClick = onClose,
                    icon = R.drawable.icon_close,
                    modifier = innerModifier.layoutId("close")
                )

                JoinClassSelector(
                    modifier = innerModifier.layoutId("selector"),
                    viewModel = viewModel,
                )

                when (selectedJoinClassOption) {
                    JoinClassOption.UseClassCode -> {
                        EnterClassCodeScreen(
                            viewModel = viewModel,
                            modifier = innerModifier
                                .layoutId("content")
                                .wrapContentHeight()
                        )
                    }

                    JoinClassOption.CreateClass -> {
                        CreateClassScreen(
                            viewModel = viewModel,
                            modifier = innerModifier
                                .layoutId("content")
                                .wrapContentHeight()
                        )
                    }

                    else -> {
                        EnterClassCodeScreen(
                            viewModel = viewModel,
                            modifier = innerModifier
                                .layoutId("content")
                                .wrapContentHeight()
                        )
                    }
                }
            }
        }
    }
}


private fun joinClassScreenConstraint(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val close = createRefFor("close")
        val title = createRefFor("title")
        val content = createRefFor("content")
        val selector = createRefFor("selector")

        constrain(close) {
            top.linkTo(parent.top, margin = 8.dp)
            end.linkTo(parent.end, margin = 8.dp)
        }

        constrain(title) {
            top.linkTo(parent.top, margin = margin)
            start.linkTo(parent.start, margin = 0.dp)
            end.linkTo(parent.end, margin = 0.dp)
        }

        constrain(selector) {
            top.linkTo(title.bottom, margin = 32.dp)
            start.linkTo(title.start, margin = 0.dp)
            end.linkTo(title.end, margin = 0.dp)
        }

        constrain(content) {
            top.linkTo(selector.bottom, margin = 32.dp)
            start.linkTo(parent.start, margin = margin)
            end.linkTo(parent.end, margin = margin)
            width = Dimension.fillToConstraints
        }
    }
}


@Composable
fun EnterClassCodeScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {

    val classCode by viewModel.classCodeAddClass.collectAsState()
    var isVerifying by remember { mutableStateOf(false) }
    var codeVerified by remember { mutableStateOf(false) }

    LaunchedEffect(isVerifying) {
        if (isVerifying) {
            delay(3000)
            isVerifying = false
            codeVerified = true
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            maxLines = 1,
            singleLine = true,
            value = classCode ?: "",
            onValueChange = viewModel::onClassCodeChanged,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            placeholder = {
            },
            label = {
                Text(
                    text = stringResource(id = R.string.placeholder_class_code),
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.primary.copy(0.5f)
                )
            },
            trailingIcon = {
                TextButton(
                    onClick = { isVerifying = true },
                    enabled = classCode != null && classCode?.isBlank() == false,
                    modifier = modifier.padding(horizontal = 4.dp)
                ) {
                    if (isVerifying) {
                        //loading bar
                        CircularProgressIndicator(
                            color = MaterialTheme.colors.primary,
                            strokeWidth = 1.5.dp,
                            modifier = modifier.size(12.dp)
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.verify).uppercase(),
                            fontSize = 10.sp,
                            color = if (classCode?.isBlank() == true || classCode == null) Black100.copy(
                                0.4f
                            ) else MaterialTheme.colors.primary
                        )
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            )
        )

        TextButton(
            onClick = { /*TODO*/ }, enabled = true,
            shape = RoundedCornerShape(22.dp),
            modifier = modifier
                .clip(RoundedCornerShape(22.dp))
                .fillMaxWidth()
                .padding(12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary.copy(0.3f),
                contentColor = MaterialTheme.colors.primary,
                disabledBackgroundColor = Black100.copy(0.1f)
            )
        ) {
            Text(
                text = stringResource(id = R.string.continue_text),
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary,
                modifier = modifier.padding(vertical = 8.dp)
            )
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateClassScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {

    val levels = listOf(
        AcademicLevel("Level 1"),
        AcademicLevel("Level 1"),
        AcademicLevel("Level 1"),
        AcademicLevel("Level 1"),
        AcademicLevel("Level 1"),
        AcademicLevel("Level 1"),
        AcademicLevel("Level 1"),
        AcademicLevel("Level 1"),
        AcademicLevel("Level 1"),
        AcademicLevel("Level 1"),
        AcademicLevel("Level 1"),
        AcademicLevel("Level 1"),
        AcademicLevel("Level 1"),
        AcademicLevel("Level 1"),
        AcademicLevel("Level 1"),
    )

    val classNameAddClass by viewModel.classNameAddClass.collectAsState()
    val selectedAcademicLevel by viewModel.selectedAcademicLevelAddClass.collectAsState()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            maxLines = 1,
            singleLine = true,
            value = classNameAddClass ?: "",
            onValueChange = viewModel::onClassNameAddClassChanged,
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp),
            placeholder = {
            },
            label = {
                Text(
                    text = stringResource(id = R.string.placeholder_class_name),
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.primary.copy(0.5f)
                )
            },
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            )
        )

        var academicLevelMenuState by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = academicLevelMenuState,
            modifier = modifier.zIndex(100f),
            onExpandedChange = {
                academicLevelMenuState = !academicLevelMenuState
            },
        ) {
            OutlinedTextField(
                maxLines = 1,
                singleLine = true,
                value = selectedAcademicLevel?.name ?: "",
                onValueChange = viewModel::onClassNameAddClassChanged,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                trailingIcon = {
                         ExposedDropdownMenuDefaults.TrailingIcon(expanded = academicLevelMenuState)
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.placeholder_academic_level),
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.primary.copy(0.5f)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    autoCorrect = true,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                readOnly = true,
            )

            ExposedDropdownMenu(
                expanded = academicLevelMenuState,
                onDismissRequest = { academicLevelMenuState = false }
            ) {
                levels.forEach { each ->
                    DropdownMenuItem(
                        onClick = {
                            viewModel.onSelectedAcademicLevelAddClassChanged(each)
                            academicLevelMenuState = false
                        }
                    ) {
                        Text(
                            text = each.name,
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.primary.copy(0.5f)
                        )
                    }
                }
            }
        }


        TextButton(
            onClick = { /*TODO* create class */ }, enabled = true,
            shape = RoundedCornerShape(22.dp),
            modifier = modifier
                .clip(RoundedCornerShape(22.dp))
                .fillMaxWidth()
                .padding(12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary.copy(0.3f),
                contentColor = MaterialTheme.colors.primary,
                disabledBackgroundColor = Black100.copy(0.1f)
            )
        ) {
            Text(
                text = stringResource(id = R.string.create_class),
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary,
                modifier = modifier.padding(vertical = 8.dp)
            )
        }
    }
}


@Composable
fun JoinClassSelector(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    options: List<JoinClassOption> = JoinClassOption.values().toList(),
) {
    val selectedJoinClassOption by viewModel.selectedJoinClassOption.collectAsState()

    Surface(
        modifier = modifier.clip(RoundedCornerShape(28.dp)),
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent,
        border = BorderStroke(
            width = 1.dp,
            color = Black100.copy(0.1f)
        )
    ) {
        Row(
            modifier = modifier.padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            options.forEach { each ->
                JoinClassButton(
                    onSelect = viewModel::onSelectedJoinClassOptionChanged,
                    option = each,
                    selected = each == (selectedJoinClassOption ?: JoinClassOption.UseClassCode)
                )
            }
        }
    }
}


@Composable
fun JoinClassButton(
    modifier: Modifier = Modifier,
    onSelect: (JoinClassOption) -> Unit,
    option: JoinClassOption,
    selected: Boolean = false,
) {
    TextButton(
        onClick = { onSelect(option) },
        shape = RoundedCornerShape(28.dp),
        modifier = modifier
            .clip(RoundedCornerShape(28.dp)),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (selected) MaterialTheme.colors.primary.copy(0.3f) else Color.Transparent,
            contentColor = if (selected) MaterialTheme.colors.primary else Black100.copy(0.3f),
            disabledBackgroundColor = Black100.copy(0.1f)
        )
    ) {
        Text(
            text = option.option,
            fontSize = 12.sp,
            color = if (selected) MaterialTheme.colors.primary else Black100.copy(0.3f),
        )
    }
}


enum class JoinClassOption(val option: String) {
    UseClassCode("Use Class Code"),
    CreateClass("Create a Class")
}

data class AcademicLevel(
    val name: String,
)


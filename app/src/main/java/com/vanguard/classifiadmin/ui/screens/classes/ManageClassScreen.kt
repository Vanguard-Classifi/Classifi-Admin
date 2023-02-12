package com.vanguard.classifiadmin.ui.screens.classes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.domain.helpers.generateColorFromClassName
import com.vanguard.classifiadmin.ui.components.ChildTopBar
import com.vanguard.classifiadmin.ui.theme.Black100
import com.vanguard.classifiadmin.viewmodel.MainViewModel

const val MANAGE_CLASS_SCREEN = "manage_class_screen"

@Composable
fun ManageClassScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
) {
    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        Scaffold(modifier = modifier,
            topBar = {
                ChildTopBar(
                    onBack = onBack,
                    heading = stringResource(id = R.string.manage_class)
                )
            },
            content = { padding ->
                ManageClassScreenContent(
                    viewModel = viewModel,
                    modifier = modifier.padding(padding),
                    maxHeight = maxHeight,
                )
            })
    }
}


@Composable
fun ManageClassScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    maxHeight: Dp,
) {
    val constraints = manageClassScreenContentConstraints(16.dp)
    Card(
        modifier = modifier
            .height(maxHeight.times(0.67f))
            .padding(horizontal = 8.dp, vertical = 32.dp),
        elevation = 2.dp, shape = RoundedCornerShape(16.dp)
    ) {
        BoxWithConstraints(modifier = modifier) {
            ConstraintLayout(
                modifier = modifier.fillMaxWidth(),
                constraintSet = constraints,
            ) {

            }
        }
    }
}


private fun manageClassScreenContentConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val classIcon = createRefFor("classIcon")
        val className = createRefFor("className")
        val classCode = createRefFor("classCode")
        val classSubjects = createRefFor("classSubjects")
        val manageSubject = createRefFor("manageSubject")
        val classInfo = createRefFor("classInfo")
        val extras = createRefFor("extras")
    }
}


@Composable
fun ManageClassItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    extra: String,
    icon: Int,
    onExtra: () -> Unit,
) {
    val constraints = manageClassItemConstraints(8.dp)
    val innerModifier = Modifier

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(0.1f)
        )
    ) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            constraintSet = constraints
        ) {
            ManageClassItemIcon(icon = icon, modifier = innerModifier.layoutId("icon"))

            Text(
                modifier = innerModifier.layoutId("className"),
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
            )

            Text(
                modifier = innerModifier.layoutId("code"),
                text = subtitle,
                fontSize = 12.sp,
                color = Black100.copy(0.8f)
            )

            TextButton(onClick = onExtra, modifier = innerModifier.layoutId("manage")) {
                Text(
                    text = extra.uppercase(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.error,
                )
            }
        }
    }

}


private fun manageClassItemConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val icon = createRefFor("icon")
        val className = createRefFor("className")
        val code = createRefFor("code")
        val manage = createRefFor("manage")

        constrain(icon) {
            start.linkTo(parent.start, margin = margin)
            top.linkTo(className.top, margin = 0.dp)
        }

        constrain(className) {
            top.linkTo(parent.top, margin = margin)
            start.linkTo(icon.end, margin = 8.dp)
        }

        constrain(code) {
            top.linkTo(className.bottom, margin = 4.dp)
            start.linkTo(className.start, margin = 0.dp)
        }

        constrain(manage) {
            top.linkTo(className.top, margin = 0.dp)
            bottom.linkTo(code.bottom, margin = 0.dp)
            end.linkTo(parent.end, margin = margin)
        }
    }
}

@Composable
fun ManageClassItemIcon(
    modifier: Modifier = Modifier,
    icon: Int,
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colors.primary.copy(0.3f)
    ) {
        Box(
            modifier = modifier
                .padding(0.dp)
                .height(38.dp)
                .width(38.dp), contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = modifier
                    .size(24.dp)
                    .padding(4.dp),
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = R.string.manage_class_item_icon),
                tint = MaterialTheme.colors.onPrimary,
            )
        }
    }
}

@Composable
fun SubjectChip(
    modifier: Modifier = Modifier,
    subjectName: String,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary,
        ),
        color = MaterialTheme.colors.primary.copy(0.1f)
    ) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(
                text = subjectName,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                modifier = modifier.padding(8.dp),
                maxLines = 1,
            )
        }
    }
}


@Composable
fun ManageSubjectBox(
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {

    }
}


@Preview
@Composable
private fun ManageClassItemIconPreview() {
    ManageClassItemIcon(
        icon = R.drawable.icon_close
    )
}

@Preview
@Composable
private fun ManageClassItemPreview() {
    ManageClassItem(
        title = "YEAR 11 ICT",
        subtitle = "Current class",
        extra = "leave class",
        icon = R.drawable.icon_reports,
        onExtra = {}
    )
}

@Preview
@Composable
private fun SubjectChipPreview() {
    SubjectChip(subjectName = "Mathematics")
}
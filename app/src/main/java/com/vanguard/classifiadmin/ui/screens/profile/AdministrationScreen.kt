package com.vanguard.classifiadmin.ui.screens.profile


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.domain.extensions.splitWithSpace
import com.vanguard.classifiadmin.domain.helpers.generateColorFromClassName
import com.vanguard.classifiadmin.domain.helpers.generateColorFromUserName
import com.vanguard.classifiadmin.ui.components.ClassFilterManageButton
import com.vanguard.classifiadmin.ui.components.ClassIcon
import com.vanguard.classifiadmin.ui.theme.Black100

@Composable
fun MyAccountScreenAdmin(
    modifier: Modifier = Modifier,
    createOrManageFeatures: List<AdminCreateOrManageFeature> = AdminCreateOrManageFeature.values()
        .toList(),
    enrollFeatures: List<AdminEnrollFeature> = AdminEnrollFeature.values().toList(),
) {
    val verticalScroll = rememberScrollState()

    Column(modifier = Modifier.verticalScroll(verticalScroll)) {

        Card(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .padding(top = 16.dp, bottom = 32.dp, start = 8.dp, end = 8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 2.dp,
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.create_or_manage),
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(32.dp))

                createOrManageFeatures.forEach { feature ->
                    CreateOrManageAdminItem(
                        feature = feature,
                        onManageItem = {
                            /*todo: on manage item */
                        },
                        onSelectItem = {
                            /*todo: on Select item */
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }


        Card(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .padding(top = 16.dp, bottom = 32.dp, start = 8.dp, end = 8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 2.dp,
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.enroll),
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(32.dp))

                enrollFeatures.forEach { feature ->
                    EnrollAdminItem(feature = feature, onSelectItem = {})

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun EnrollAdminItem(
    modifier: Modifier = Modifier,
    feature: AdminEnrollFeature,
    onSelectItem: (AdminEnrollFeature) -> Unit,
) {

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(vertical = 8.dp)
            .clickable { onSelectItem(feature) },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart,
        ) {
            Row(
                modifier = modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = feature.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.padding(
                        start = 8.dp,
                        top = 16.dp,
                        bottom = 16.dp,
                    ),
                    color = Color(generateColorFromUserName(feature.title.splitWithSpace()[1])),
                )

                Icon(
                    painter = painterResource(id = R.drawable.icon_enroll),
                    contentDescription = stringResource(id = R.string.enroll),
                    tint = Color(generateColorFromUserName(feature.title.splitWithSpace()[1])),
                    modifier = Modifier.padding(12.dp)
                )
            }

        }
    }
}


@Composable
fun CreateOrManageAdminItem(
    modifier: Modifier = Modifier,
    feature: AdminCreateOrManageFeature,
    onManageItem: (AdminCreateOrManageFeature) -> Unit,
    onSelectItem: (AdminCreateOrManageFeature) -> Unit,
) {
    val constraints = CreateOrManageAdminItemConstraints(8.dp)
    val innerModifier = Modifier
    var rowWidth by remember { mutableStateOf(0) }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(vertical = 8.dp)
            .clickable { onSelectItem(feature) },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary
        )
    ) {
        ConstraintLayout(
            modifier = modifier
                .onGloballyPositioned { rowWidth = it.size.width }
                .fillMaxWidth()
                .padding(8.dp),
            constraintSet = constraints
        ) {
            FeatureIcon(
                icon = feature.icon,
                modifier = innerModifier.layoutId("icon"),
                color = Color(generateColorFromUserName(feature.title.splitWithSpace()[1])),
            )

            Text(
                modifier = innerModifier.layoutId("className"),
                text = feature.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(generateColorFromUserName(feature.title.splitWithSpace()[1])),
            )

            Text(
                modifier = innerModifier
                    .layoutId("code")
                    .widthIn(max = with(LocalDensity.current) {
                        rowWidth
                            .toDp()
                            .times(.5f)
                    }),
                text = feature.description,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color(generateColorFromUserName(feature.title.splitWithSpace()[1])),
            )


            ClassFilterManageButton(
                modifier = innerModifier.layoutId("manage"),
                icon = R.drawable.icon_settings,
                label = stringResource(id = R.string.manage),
                className = feature.title,
                onSelect = { onManageItem(feature) },
            )

        }
    }
}

private fun CreateOrManageAdminItemConstraints(margin: Dp): ConstraintSet {
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
fun FeatureIcon(
    modifier: Modifier = Modifier,
    icon: Int,
    color: Color = MaterialTheme.colors.primary,
    surfaceColor: Color = Color.Transparent,
    iconSize: Dp = 24.dp,
    surfaceSize: Dp = 38.dp,
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = surfaceColor,
        border = BorderStroke(
            1.dp, color,
        )
    ) {
        Box(
            modifier = modifier
                .padding(0.dp)
                .height(surfaceSize)
                .width(surfaceSize), contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = modifier
                    .size(iconSize)
                    .padding(4.dp),
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = R.string.item_icon),
                tint = color,
            )
        }
    }
}


enum class AdminCreateOrManageFeature(
    val title: String,
    val description: String,
    val icon: Int,
) {
    Classroom("Create classes", "More rooms to learn", R.drawable.icon_classroom),
    Subject("Create subjects", "Organized sessions", R.drawable.icon_subject),
}

enum class AdminEnrollFeature(
    val title: String,
) {
    Teacher("Enroll teachers"),
    Student("Enroll students"),
    Parent("Enroll parents"),
}

@Preview
@Composable
private fun CreateOrManageAdminItemPreview() {
    CreateOrManageAdminItem(
        feature = AdminCreateOrManageFeature.Classroom,
        onManageItem = {},
        onSelectItem = {}
    )
}
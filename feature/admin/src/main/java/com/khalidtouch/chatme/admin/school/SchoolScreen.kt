package com.khalidtouch.chatme.admin.school

import android.net.Uri
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import com.google.accompanist.navigation.animation.composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.khalidtouch.chatme.admin.R
import com.khalidtouch.chatme.admin.common.ItemNotAvailable
import com.khalidtouch.chatme.admin.school.addschool.AddSchoolScreen
import com.khalidtouch.chatme.admin.school.addschool.AddSchoolViewModel
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import com.khalidtouch.core.designsystem.ClassifiLoadingWheel
import com.khalidtouch.core.designsystem.components.ClassifiFab
import com.khalidtouch.core.designsystem.components.ClassifiIconButton
import com.khalidtouch.core.designsystem.components.ClassifiSimpleTopAppBar
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SchoolRoute(
    windowSizeClass: WindowSizeClass,
    schoolViewModel: SchoolViewModel,
    addSchoolViewModel: AddSchoolViewModel = hiltViewModel<AddSchoolViewModel>(),
    onBackPressed: () -> Unit,
    onModifySchool: () -> Unit,
    onClickItem: (SchoolSegment) -> Unit,
) {
    val TAG = "Schoolroute"
    val uiState by schoolViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        schoolViewModel.loadSchoolId()
        if(uiState is SchoolScreenUiState.Success) {
            schoolViewModel.updateCurrentSchool((uiState as SchoolScreenUiState.Success).data.currentSchoolId)
        }
        delay(1_000)
        schoolViewModel.onFinishLoadingState()
    }


    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            val headerStyle = MaterialTheme.typography.titleMedium

            ClassifiSimpleTopAppBar(
                title = {
                    Box(Modifier.padding(start = 16.dp)) {
                        ProvideTextStyle(headerStyle) {
                            Text(
                                text = stringResource(id = R.string.manage_school)
                            )
                        }
                    }
                },
                navIcon = {
                    Box(modifier = Modifier) {
                        IconButton(
                            onClick = onBackPressed,
                            enabled = true,
                        ) {
                            Icon(
                                painter = painterResource(id = ClassifiIcons.Back),
                                contentDescription = stringResource(id = R.string.go_back)
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            when (uiState) {
                is SchoolScreenUiState.Loading -> Unit
                is SchoolScreenUiState.Success -> {
                    if ((uiState as SchoolScreenUiState.Success).data.hasFinishedLoading) {
                        val school = (uiState as SchoolScreenUiState.Success).data.currentSchool
                        if (school == null || school.schoolId == -1L) {
                            ClassifiFab(
                                onClick = schoolViewModel::onShowAddSchoolDialog,
                                icon = {
                                    Icon(
                                        painter = painterResource(ClassifiIcons.Add),
                                        contentDescription = stringResource(R.string.add_school)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        },
        content = {
            SchoolScreen(
                modifier = Modifier.padding(it),
                schoolViewModel = schoolViewModel,
                windowSizeClass = windowSizeClass,
                addSchoolViewModel = addSchoolViewModel,
                onModifySchool = onModifySchool,
                onClickItem = onClickItem,
            )
        }
    )
}


@Composable
private fun SchoolScreen(
    modifier: Modifier = Modifier,
    schoolViewModel: SchoolViewModel,
    addSchoolViewModel: AddSchoolViewModel,
    windowSizeClass: WindowSizeClass,
    onModifySchool: () -> Unit,
    onClickItem: (SchoolSegment) -> Unit,
) {
    val uiState by schoolViewModel.uiState.collectAsStateWithLifecycle()
    val state = rememberLazyListState()
    val configuration = LocalConfiguration.current
    val showLoadingWheel = when (uiState) {
        is SchoolScreenUiState.Loading -> true
        is SchoolScreenUiState.Success -> {
            !(uiState as SchoolScreenUiState.Success).data.hasFinishedLoading
        }
    }

    when (uiState) {
        is SchoolScreenUiState.Loading -> Unit
        is SchoolScreenUiState.Success -> {
            val school = (uiState as SchoolScreenUiState.Success).data.currentSchool
            if (school != null && -1L != school.schoolId) {
                LazyColumn(
                    state = state,
                    modifier = modifier
                        .fillMaxSize()
                        .testTag("admin:school"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(16.dp),
                ) {
                    schoolItem(
                        fullWidth = configuration.screenWidthDp.dp,
                        school = school,
                        onModifySchool = onModifySchool,
                        onClickItem = onClickItem,
                    )
                }
            }

            if (((uiState as SchoolScreenUiState.Success).data.hasFinishedLoading)) {
                if (school == null || school.schoolId == -1L) {
                    ItemNotAvailable(
                        headerText = stringResource(id = R.string.no_school_added),
                        labelText = stringResource(id = R.string.click_plus_to_add)
                    )
                }
            }


            if ((uiState as SchoolScreenUiState.Success).data.shouldShowAddSchoolDialog) {
                AddSchoolScreen(
                    windowSizeClass = windowSizeClass,
                    schoolViewModel = schoolViewModel,
                    addSchoolViewModel = addSchoolViewModel,
                )
            }
        }
    }


    AnimatedVisibility(
        visible = showLoadingWheel,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
        ) + fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center,
        ) {
            ClassifiLoadingWheel()
        }
    }
}


const val schoolNavigationRoute = "school_navigation_route"

fun NavController.navigateToSchoolScreen() {
    this.navigate(schoolNavigationRoute) {
        launchSingleTop = true
        popUpTo(schoolNavigationRoute)
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.schoolScreen(
    windowSizeClass: WindowSizeClass,
    onModifySchool: () -> Unit,
    onClickItem: (SchoolSegment) -> Unit,
    onBackPressed: () -> Unit,
    schoolViewModel: SchoolViewModel,
) {
    composable(
        route = schoolNavigationRoute,
    ) {
        SchoolRoute(
            onBackPressed = onBackPressed,
            windowSizeClass = windowSizeClass,
            onClickItem = onClickItem,
            onModifySchool = onModifySchool,
            schoolViewModel = schoolViewModel,
        )
    }
}

private fun LazyListScope.schoolItem(
    fullWidth: Dp,
    school: ClassifiSchool,
    onModifySchool: () -> Unit,
    onClickItem: (SchoolSegment) -> Unit,
) {
    item {
        SchoolCard(
            fullWidth = fullWidth,
            school = school,
            onModifySchool = onModifySchool,
            onClickItem = onClickItem,
            modifier = Modifier,
        )
    }
}

@Composable
private fun SchoolCard(
    modifier: Modifier = Modifier,
    fullWidth: Dp,
    school: ClassifiSchool,
    onModifySchool: () -> Unit,
    onClickItem: (SchoolSegment) -> Unit,
) {
    Card(
        modifier = modifier
            .widthIn(max = fullWidth - 16.dp)
            .padding(top = 16.dp, bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        content = {
            SchoolContent(
                school = school,
                onModifySchool = onModifySchool,
                onClickItem = onClickItem,
            )
        }
    )
}


@Composable
fun SchoolContent(
    school: ClassifiSchool,
    onModifySchool: () -> Unit,
    onClickItem: (SchoolSegment) -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {
        SchoolHeader(
            school = school,
            onModifySchool = onModifySchool,
        )
        SchoolBase(
            school = school,
            onClick = onClickItem,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SchoolBase(
    school: ClassifiSchool,
    onClick: (SchoolSegment) -> Unit,
) {
    Column {
        FlowRow {
            SchoolSegment.values().map {
                val stats = when (it) {
                    SchoolSegment.Admin -> school.admins.size
                    SchoolSegment.Parents -> school.parents.size
                    SchoolSegment.Students -> school.students.size
                    SchoolSegment.Teachers -> school.teachers.size
                    SchoolSegment.Classes -> school.classes.size
                }
                SchoolBaseItem(
                    segment = it,
                    stats = stats,
                    text = it.text,
                    icon = it.icon,
                    onClick = onClick,
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Box(Modifier.padding(16.dp)) {
            val schoolAddress = when (school.address) {
                null, "" -> stringResource(id = R.string.address_not_added)
                else -> school.address!!
            }
            Text(
                text = schoolAddress,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.outline,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}


@Composable
private fun SchoolBaseItem(
    segment: SchoolSegment,
    stats: Int,
    text: String,
    @DrawableRes icon: Int,
    onClick: (SchoolSegment) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.padding(16.dp)) {
            ClassifiIconButton(
                onClick = { onClick(segment) },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.outline.copy(
                        SchoolScreenDefaults.iconTintAlpha
                    )
                ),
                icon = {
                    Icon(
                        painterResource(id = segment.icon),
                        contentDescription = segment.text,
                        tint = MaterialTheme.colorScheme.outline.copy(
                            SchoolScreenDefaults.iconTintAlpha
                        )
                    )
                }
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = stats.toString(),
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.outline,
            )
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = segment.text,
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.outline,
            )
        )
    }
}

@Composable
private fun SchoolHeader(
    school: ClassifiSchool,
    onModifySchool: () -> Unit,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .clickable(
                enabled = true,
                onClick = onModifySchool,
                role = Role.Button,
            )
    ) {
        val bannerUri = Uri.parse(school.bannerImage)
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(bannerUri).build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
        )

        Box(
            Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color.White.copy(0.3f), Color.Black.copy(0.3f)),
                        0.0f,
                        500.0f,
                    )
                )
        )


        Box(
            Modifier
                .matchParentSize()
                .padding(
                    bottom = 16.dp, start = 16.dp,
                    end = 32.dp,
                ),
            contentAlignment = Alignment.BottomStart
        ) {
            val schoolName: String = when (school.schoolName) {
                null, "" -> stringResource(id = R.string.school_name_not_set)
                else -> school.schoolName!!
            }
            Text(
                text = schoolName,
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Box(
            Modifier
                .matchParentSize()
                .padding(top = 8.dp, end = 8.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            ClassifiIconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.outline.copy(
                        SchoolScreenDefaults.iconTintAlpha
                    )
                ),
                onClick = onModifySchool,
                icon = {
                    Icon(
                        painter = painterResource(id = ClassifiIcons.EditSolid),
                        contentDescription = null,
                    )
                }
            )
        }
    }
}

enum class SchoolSegment(val text: String, val icon: Int) {
    Teachers("Teachers", ClassifiIcons.Personal),
    Students("Students", ClassifiIcons.Students),
    Parents("Parents", ClassifiIcons.Parent),
    Admin("Admins", ClassifiIcons.Admin),
    Classes("Classes", ClassifiIcons.Classroom)
}

object SchoolScreenDefaults {
    const val iconTintAlpha = 0.7f
}
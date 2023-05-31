package com.khalidtouch.chatme.admin.school

import android.net.Uri
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SchoolRoute(
    windowSizeClass: WindowSizeClass,
    schoolViewModel: SchoolViewModel = hiltViewModel<SchoolViewModel>(),
    addSchoolViewModel: AddSchoolViewModel = hiltViewModel<AddSchoolViewModel>(),
    onBackPressed: () -> Unit,
) {
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
            ClassifiFab(
                onClick = schoolViewModel::onShowAddSchoolDialog,
                icon = {
                    Icon(
                        painter = painterResource(ClassifiIcons.Add),
                        contentDescription = stringResource(R.string.add_school)
                    )
                }
            )
        },
        content = {
            SchoolScreen(
                modifier = Modifier.padding(it),
                schoolViewModel = schoolViewModel,
                windowSizeClass = windowSizeClass,
                addSchoolViewModel = addSchoolViewModel,
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
) {
    val uiState by schoolViewModel.uiState.collectAsStateWithLifecycle()
    val state = rememberLazyListState()
    val configuration = LocalConfiguration.current
    val mySchool by schoolViewModel.observeMySchool.collectAsStateWithLifecycle()

    when (uiState) {
        is SchoolScreenUiState.Loading -> Unit
        is SchoolScreenUiState.Success -> {
            if (mySchool != null) {
                LazyColumn(
                    state = state,
                    modifier = modifier
                        .fillMaxSize()
                        .testTag("admin:school")
                ) {
                    schoolItem(
                        fullWidth = configuration.screenWidthDp.dp,
                        school = ,
                        onModifySchool = ,
                        onClickItem = ,
                    )
                }
            }

            if (mySchool == null) {
                ItemNotAvailable(
                    headerText = stringResource(id = R.string.no_school_added),
                    labelText = stringResource(id = R.string.click_plus_to_add)
                )
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
        visible = uiState is SchoolScreenUiState.Loading,
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
    onBackPressed: () -> Unit,
) {
    composable(
        route = schoolNavigationRoute,
    ) {
        SchoolRoute(
            onBackPressed = onBackPressed,
            windowSizeClass = windowSizeClass,
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
            school = school ,
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
              val stats = when(it) {
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
          Text(
              text = school.address.orEmpty(),
              style = MaterialTheme.typography.labelMedium.copy(
                  color = MaterialTheme.colorScheme.outline,
              )
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
    Row {
        Box(Modifier.padding(16.dp)) {
            ClassifiIconButton(
                onClick = { onClick(segment) },
                icon = {
                    Icon(
                        painterResource(id = segment.icon),
                        contentDescription = segment.text
                    )
                }
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = stats.toString(),
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.outline,
            )
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = segment.text,
            style = MaterialTheme.typography.labelLarge.copy(
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
                .height(400.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(bannerUri).build(),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )

        Box(
            Modifier
                .matchParentSize()
                .padding(bottom = 16.dp, start = 16.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = school.schoolName ?: stringResource(id = R.string.school_name_not_set),
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.outline,
                )
            )
        }

        Box(
            Modifier
                .matchParentSize()
                .padding(top = 16.dp, end = 16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            ClassifiIconButton(
                onClick = onModifySchool,
                icon = {
                    Icon(
                        painter = painterResource(id = ClassifiIcons.EditSolid),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline
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
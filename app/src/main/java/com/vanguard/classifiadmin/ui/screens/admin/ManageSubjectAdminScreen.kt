package com.vanguard.classifiadmin.ui.screens.admin

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.ui.components.ChildTopBarWithOptions

const val MANAGE_SUBJECT_ADMIN_SCREEN = "manage_subject_admin_screen"

@Composable
fun ManageSubjectAdminScreen(
    modifier: Modifier = Modifier
) {

    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        val maxHeight = maxHeight

        Scaffold(
            modifier = Modifier,
            topBar = {
                ChildTopBarWithOptions(
                    onBack = onBack,
                    heading = stringResource(id = R.string.manage_classes),
                    elevation = 0.dp,
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                    iconTint = MaterialTheme.colors.onPrimary,
                    onOptions = {
                        optionState = !optionState
                    },
                )
            },
            content = {
                ManageClassAdminScreenContent(
                    modifier = modifier.padding(it),
                    viewModel = viewModel,
                    onBack = onBack,
                    onManageClassDetail = onManageClassDetail,
                    maxHeight = maxHeight,
                    onAddClass = onAddClass,
                )
            }
        )
    }
}



@Composable
fun ManageSubjectAdminScreenContent(
    modifier: Modifier = Modifier
) {

}
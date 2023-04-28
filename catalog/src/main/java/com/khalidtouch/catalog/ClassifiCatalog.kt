package com.khalidtouch.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.khalidtouch.core.designsystem.components.ClassifiAvatar
import com.khalidtouch.core.designsystem.components.ClassifiButton
import com.khalidtouch.core.designsystem.components.ClassifiNavigationBar
import com.khalidtouch.core.designsystem.components.ClassifiNavigationBarItem
import com.khalidtouch.core.designsystem.components.ClassifiOutlinedButton
import com.khalidtouch.core.designsystem.components.ClassifiScrollableTabRow
import com.khalidtouch.core.designsystem.components.ClassifiTab
import com.khalidtouch.core.designsystem.components.ClassifiTabRow
import com.khalidtouch.core.designsystem.components.ClassifiTextButton
import com.khalidtouch.core.designsystem.components.ClassifiToggleButton
import com.khalidtouch.core.designsystem.extensions.getInitials
import com.khalidtouch.core.designsystem.icons.ClassifiIcons
import com.khalidtouch.core.designsystem.theme.ClassifiTheme

@Composable
fun ClassifiCatalog() {
    ClassifiTheme {
        var toggleButtonState by remember { mutableStateOf(false) }

        Surface {
            val contentPadding = WindowInsets
                .systemBars
                .add(WindowInsets(left = 16.dp, top = 16.dp, right = 16.dp, bottom = 16.dp))
                .asPaddingValues()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    Text(
                        text = "Classifi Typographies",
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(
                        text = "DisplayLarge",
                        style = MaterialTheme.typography.displayLarge,
                    )

                    Text(
                        text = "DisplayMedium",
                        style = MaterialTheme.typography.displayMedium,
                    )

                    Text(
                        text = "DisplaySmall",
                        style = MaterialTheme.typography.displaySmall,
                    )

                    Text(
                        text = "HeadlineLarge",
                        style = MaterialTheme.typography.headlineLarge,
                    )

                    Text(
                        text = "HeadlineMedium",
                        style = MaterialTheme.typography.headlineMedium,
                    )

                    Text(
                        text = "HeadlineSmall",
                        style = MaterialTheme.typography.headlineSmall,
                    )

                    Text(
                        text = "TitleLarge",
                        style = MaterialTheme.typography.titleLarge,
                    )

                    Text(
                        text = "TitleMedium",
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Text(
                        text = "TitleSmall",
                        style = MaterialTheme.typography.titleSmall,
                    )

                    Text(
                        text = "BodyLarge",
                        style = MaterialTheme.typography.bodyLarge,
                    )

                    Text(
                        text = "BodyMedium",
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Text(
                        text = "BodySmall",
                        style = MaterialTheme.typography.bodySmall,
                    )

                    Text(
                        text = "LabelLarge",
                        style = MaterialTheme.typography.labelLarge,
                    )

                    Text(
                        text = "LabelMedium",
                        style = MaterialTheme.typography.labelMedium,
                    )

                    Text(
                        text = "LabelSmall",
                        style = MaterialTheme.typography.labelSmall,
                    )

                }

                item {
                    Text(text = "Buttons", style = MaterialTheme.typography.headlineMedium)
                }
                item {
                    ClassifiButton(onClick = { /*TODO*/ }) {
                        Text(text = "Click Here", style = MaterialTheme.typography.labelMedium)
                    }

                    ClassifiButton(
                        onClick = {},
                        text = {
                            Text(
                                text = "ClassifiButton",
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = ClassifiIcons.Tick),
                                contentDescription = null,
                            )
                        }
                    )


                    ClassifiTextButton(onClick = { /*TODO*/ }) {
                        Text("TextButton", style = MaterialTheme.typography.labelLarge)
                    }

                    ClassifiOutlinedButton(onClick = { /*TODO*/ }) {
                        Text(
                            "Outline Button", style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
                        )
                    }


                    ClassifiToggleButton(checked = toggleButtonState, onCheckedChange = {
                        toggleButtonState = !toggleButtonState
                    },
                        icon = {
                            Icon(
                                painter = painterResource(id = ClassifiIcons.Close),
                                contentDescription = null,
                            )
                        },
                        checkedIcon = {
                            Icon(
                                painter = painterResource(id = ClassifiIcons.Tick),
                                contentDescription = null,
                            )
                        }
                    )

                    ClassifiToggleButton(
                        enabled = false,
                        checked = toggleButtonState, onCheckedChange = {
                            toggleButtonState = !toggleButtonState
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = ClassifiIcons.Close),
                                contentDescription = null,
                            )
                        },
                        checkedIcon = {
                            Icon(
                                painter = painterResource(id = ClassifiIcons.Tick),
                                contentDescription = null,
                            )
                        }
                    )
                }

                item {
                    var selectedItem by remember { mutableStateOf(0) }
                    val items = listOf("Feeds", "Students", "Assessments", "Reports")
                    val icons = listOf(
                        ClassifiIcons.Feeds,
                        ClassifiIcons.StudentsOutline,
                        ClassifiIcons.AssessmentOutline,
                        ClassifiIcons.ReportsOutline
                    )

                    val selectedIcons = listOf(
                        ClassifiIcons.FeedsSolid,
                        ClassifiIcons.Students,
                        ClassifiIcons.Assessment,
                        ClassifiIcons.Reports
                    )

                    Text("Bottom Navigation", style = MaterialTheme.typography.headlineSmall)

                    ClassifiNavigationBar {
                        items.forEachIndexed { index, item ->
                            ClassifiNavigationBarItem(
                                selected = selectedItem == index,
                                onClick = { selectedItem = index },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = icons[index]),
                                        contentDescription = item,
                                    )
                                },
                                selectedIcon = {
                                    Icon(
                                        painter = painterResource(id = selectedIcons[index]),
                                        contentDescription = item,
                                    )
                                },
                                label = { Text(item, style = MaterialTheme.typography.labelSmall) },
                            )
                        }
                    }
                }

                item {
                    Text("Tabs", style = MaterialTheme.typography.headlineSmall)

                    var selectedTabIndex by remember { mutableStateOf(0) }
                    val titles =
                        listOf("Profile", "Account Settings", "Preferences", "Administration")

                    ClassifiTabRow(selectedTabIndex = selectedTabIndex) {
                        titles.forEachIndexed { index, title ->
                            ClassifiTab(
                                selected = index == selectedTabIndex,
                                onClick = { selectedTabIndex = index },
                                text = {
                                    Text(title, style = MaterialTheme.typography.labelSmall)
                                }
                            )
                        }
                    }


                    ClassifiScrollableTabRow(
                        selectedTabIndex = selectedTabIndex,
                        pageSize = titles.size,
                    ) {
                        titles.forEachIndexed { index, title ->
                            ClassifiTab(
                                selected = index == selectedTabIndex,
                                onClick = { selectedTabIndex = index },
                                text = {
                                    Text(title, style = MaterialTheme.typography.titleSmall)
                                }
                            )
                        }
                    }

                    ClassifiScrollableTabRow(
                        selectedTabIndex = selectedTabIndex,
                        pageSize = titles.size,
                    ) {
                        titles.forEachIndexed { index, title ->
                            ClassifiTab(
                                selected = index == selectedTabIndex,
                                onClick = { selectedTabIndex = index },
                                text = {
                                    Text(title, style = MaterialTheme.typography.titleSmall)
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = ClassifiIcons.Profile),
                                        contentDescription = null,
                                    )
                                }
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Avatar",
                        style = MaterialTheme.typography.titleSmall
                    )

                    ClassifiAvatar(
                        isLarge = true,
                        onClick = { /*TODO*/ },
                        text = {
                            Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                                Text(
                                    text = "Mubarak Isah".getInitials(),
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    )

                    ClassifiAvatar(
                        isLarge = false,
                        onClick = { /*TODO*/ },
                        text = {
                            Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                                Text(
                                    text = "Mubarak Isah".getInitials(),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    )

                }
            }
        }
    }
}
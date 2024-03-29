package com.vanguard.classifiadmin.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import com.vanguard.classifiadmin.navigation.ClassifiNavHost

@Composable
fun ClassifiApp(
    windowSizeClass: WindowSizeClass,
) {
    Column {
        ClassifiNavHost(windowSizeClass = windowSizeClass)
    }
}

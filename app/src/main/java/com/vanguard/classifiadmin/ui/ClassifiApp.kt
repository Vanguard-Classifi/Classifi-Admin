package com.vanguard.classifiadmin.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vanguard.classifiadmin.navigation.ClassifiNavHost
import com.vanguard.classifiadmin.onboarding.OnboardingNavHost
import com.vanguard.classifiadmin.onboarding.OnboardingScreen
import com.vanguard.classifiadmin.viewmodel.MainActivityUiState
import com.vanguard.classifiadmin.viewmodel.MainActivityViewModel

@Composable
fun ClassifiApp(
    windowSizeClass: WindowSizeClass,
    mainActivityViewModel: MainActivityViewModel = hiltViewModel<MainActivityViewModel>()
) {
    val uiState by mainActivityViewModel.uiState.collectAsStateWithLifecycle()

    Box {
        ClassifiNavHost(windowSizeClass = windowSizeClass)
        when(uiState) {
            is MainActivityUiState.Loading -> Unit
            is MainActivityUiState.Success -> {
                if(!(uiState as MainActivityUiState.Success).userData.shouldHideOnboarding) {
                    //onboarding dialog
                    Box(Modifier.fillMaxSize()) {
                        OnboardingScreen(windowSizeClass = windowSizeClass)
                    }
                }
            }
        }
    }
}

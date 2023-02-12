package com.vanguard.classifiadmin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanguard.classifiadmin.ui.screens.classes.JoinClassOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private var _classCode = MutableStateFlow(null as String?)
    val classCode: StateFlow<String?> = _classCode

    private var _selectedJoinClassOption = MutableStateFlow(null as JoinClassOption?)
    val selectedJoinClassOption: StateFlow<JoinClassOption?> = _selectedJoinClassOption

    fun onSelectedJoinClassOptionChanged(option: JoinClassOption?) = effect {
        _selectedJoinClassOption.value = option
    }

    fun onClassCodeChanged(code: String?) = effect {
        _classCode.value = code
    }


    private fun effect(block: suspend () -> Unit) = viewModelScope.launch { block() }
}
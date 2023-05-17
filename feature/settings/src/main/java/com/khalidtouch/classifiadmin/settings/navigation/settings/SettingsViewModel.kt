package com.khalidtouch.classifiadmin.settings.navigation.settings

import androidx.compose.foundation.gestures.DraggableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _selectedTabIndex: MutableLiveData<Int> = MutableLiveData(0)
    val selectedTabIndex: LiveData<Int> = _selectedTabIndex
    val tabs = Settings.values().toList()
    var isSwipeLeft = false
    private val draggableState = DraggableState {
        isSwipeLeft = it > 0
    }

    private var _dragState = MutableLiveData<DraggableState>(draggableState)
    val dragState: LiveData<DraggableState> = _dragState

    fun updateTabIndexBasedOnSwipe() {
        _selectedTabIndex.value = when (isSwipeLeft) {
            false -> Math.floorMod(_selectedTabIndex.value!!.plus(1), tabs.size)
            true -> Math.floorMod(_selectedTabIndex.value!!.minus(1), tabs.size)
        }
    }

    fun updateTabIndex(i: Int) {
        _selectedTabIndex.value = i
    }
}
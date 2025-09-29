package com.minesweeper.features.menu.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


data class MenuUiSate(val foo:Boolean)
class MenuViewModel: ViewModel() {

    private val _uState = MutableStateFlow(MenuUiSate(false))

    val uiState = _uState.asStateFlow()
}
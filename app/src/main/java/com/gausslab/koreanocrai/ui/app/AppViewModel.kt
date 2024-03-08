package com.gausslab.koreanocrai.ui.app

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    val navController: NavHostController
) : ViewModel() {
    fun onEvent(event: AppUiEvent) {
        when (event) {
            else -> {}
        }
    }
}

sealed class AppUiEvent {
}
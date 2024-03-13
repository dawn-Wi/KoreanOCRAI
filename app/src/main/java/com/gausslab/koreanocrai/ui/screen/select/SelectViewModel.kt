package com.gausslab.koreanocrai.ui.screen.select

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.gausslab.koreanocrai.ui.screen.home.navigateToHomeScreen
import com.gausslab.koreanocrai.ui.screen.labelling.navigateToLabellingScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectViewModel @Inject constructor(
    private val navController: NavHostController
) : ViewModel() {

    fun onEvent(event: SelectUiEvent) {
        when (event) {
            SelectUiEvent.MakeDataButtonPressed -> {
                navController.navigateToLabellingScreen()
            }

            SelectUiEvent.ShowDataButtonPRessed -> {
                navController.navigateToHomeScreen()
            }
        }
    }
}

sealed class SelectUiEvent {
    object MakeDataButtonPressed : SelectUiEvent()
    object ShowDataButtonPRessed : SelectUiEvent()
}
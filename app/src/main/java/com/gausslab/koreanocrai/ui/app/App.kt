package com.gausslab.koreanocrai.ui.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import com.gausslab.koreanocrai.ui.screen.home.homeScreen
import com.gausslab.koreanocrai.ui.screen.home.homeScreenRoute
import com.gausslab.koreanocrai.ui.screen.labelling.labellingScreen
import com.gausslab.koreanocrai.ui.screen.select.selectScreen
import com.gausslab.koreanocrai.ui.screen.select.selectScreenRoute

@Composable
fun App(
    appViewModel: AppViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {},
        bottomBar = {}
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            NavHost(
                navController = appViewModel.navController,
                startDestination = selectScreenRoute
            ){
                selectScreen()
                homeScreen()
                labellingScreen()
            }
        }
    }
}
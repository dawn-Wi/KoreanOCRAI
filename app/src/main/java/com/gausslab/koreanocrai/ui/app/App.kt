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
                startDestination = homeScreenRoute
            ){
                homeScreen()
            }
        }
    }
}
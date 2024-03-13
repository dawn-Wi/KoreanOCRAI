package com.gausslab.koreanocrai.ui.screen.select

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlin.system.exitProcess

const val selectScreenRoute = "select_screen_route"

fun NavGraphBuilder.selectScreen() {
    composable(route = selectScreenRoute) {
        SelectScreen()
    }
}

fun NavController.navigateToSelectScreen(navOptions: NavOptions? = null) {
    navigate(route = selectScreenRoute, navOptions = navOptions)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectScreen(
    viewModel: SelectViewModel = hiltViewModel()
) {
    var backPressedTime: Long = 0;
    val context = LocalContext.current

    BackHandler(
        enabled = true, onBack = {
            if (System.currentTimeMillis() > backPressedTime + 2000) {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(context, "뒤로 가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            } else if (System.currentTimeMillis() <= backPressedTime + 2000) {
                exitProcess(0)
            }
        })

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(0.3f),
                onClick = { viewModel.onEvent(SelectUiEvent.MakeDataButtonPressed) },
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(
                    modifier = Modifier,
                    fontSize = 25.sp,
                    text = "데이터\n\n만들기"
                )
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(0.3f),
                onClick = { viewModel.onEvent(SelectUiEvent.ShowDataButtonPRessed) },
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(
                    modifier = Modifier,
                    fontSize = 25.sp,
                    text = "데이터\n\n예측"
                )
            }
        }
    }
}
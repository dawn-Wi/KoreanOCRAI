package com.gausslab.koreanocrai.ui.screen.home

import android.graphics.Bitmap
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val homeScreenRoute = "home_screen_route"

fun NavGraphBuilder.homeScreen() {
    composable(route = homeScreenRoute) {
        HomeScreen()
    }
}

fun NavController.navigateToHomeScreen(navOptions: NavOptions? = null) {
    navigate(route = homeScreenRoute, navOptions = navOptions)
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val answer by viewModel.answer.collectAsState()
    val lines by viewModel.lines.collectAsState()
    var canvasSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // BoxWithConstraints를 사용하여 Canvas의 최대 크기를 얻습니다.
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(0.5f)
        ) {
            Canvas(
                modifier = Modifier
                    .matchParentSize() // Box의 크기에 맞게 Canvas 크기를 설정합니다.
                    .background(Color.Gray)
                    .onGloballyPositioned { coordinates ->
                        // Canvas의 크기를 가져와 저장합니다.
                        canvasSize = coordinates.size.toSize()
                    }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val line = Line(
                                start = change.position - dragAmount,
                                end = change.position
                            )
                            viewModel.onEvent(HomeUiEvent.DrawLines(line))
                        }
                    },
                onDraw = {
                    lines.forEach { line ->
                        drawLine(
                            color = line.color,
                            start = line.start,
                            end = line.end,
                            strokeWidth = line.strokeWidth.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onClick = { viewModel.onEvent(HomeUiEvent.ClearButtonPressed) }
            ) {
                Text(text = "다시")
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onClick = {
                    if (canvasSize.width > 0 && canvasSize.height > 0) {
                        val bitmap = addLineToBitmap(
                            width = canvasSize.width.toInt(),
                            height = canvasSize.height.toInt(),
                            lineList = lines
                        )
                        // 여기에서 bitmap을 처리합니다 (예: ImageBitmap으로 변환, 저장 등).
                        viewModel.onEvent(HomeUiEvent.SendButtonPressed(bitmap.asImageBitmap()))
                    }
                }
            ) {
                Text(text = "전송")
            }
        }
        Text(text = answer)
    }
}

private fun addLineToBitmap(
    width: Int,
    height: Int,
    lineList: List<Line>
): Bitmap{
    val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = lineList[0].strokeWidth.value
        color = lineList[0].color.toArgb()
    }
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)

    lineList.forEach { line->
        val startX = line.start.x
        val startY = line.start.y
        val endX = line.end.x
        val endY = line.end.y

        paint.color = android.graphics.Color.parseColor("#FFFF0000")

        canvas.drawLine(startX, startY, endX, endY, paint)
    }

    return bitmap
}
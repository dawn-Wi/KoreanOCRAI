package com.gausslab.koreanocrai.ui.screen.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.gausslab.koreanocrai.component.addLineToBitmap
import com.gausslab.koreanocrai.ui.screen.DetailsScreen
import java.lang.Float.max
import java.lang.Float.min

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

    val selectedColor by viewModel.selectColor.collectAsState()
    val colors = listOf(Color.Black, Color.Red, Color.Yellow, Color.Green, Color.Blue)
    val strokeWidth by viewModel.strokeWidth.collectAsState()

    DetailsScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(0.5f)
            ) {
                Canvas(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Gray)
                        .onGloballyPositioned { coordinates ->
                            canvasSize = coordinates.size.toSize()
                        }
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                val startPosition = Offset(
                                    x = max(0f, min(change.previousPosition.x, canvasSize.width)),
                                    y = max(0f, min(change.previousPosition.y, canvasSize.height))
                                )
                                val endPosition = Offset(
                                    x = max(0f, min(change.position.x, canvasSize.width)),
                                    y = max(0f, min(change.position.y, canvasSize.height))
                                )
                                val line = Line(
                                    start = startPosition,
                                    end = endPosition,
                                    color = selectedColor,
                                    strokeWidth = strokeWidth.toDp()
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
                    .padding(8.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(color, shape = CircleShape)
                            .clickable {
                                viewModel.onEvent(HomeUiEvent.SelectedColor(color))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedColor == color) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Selected",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
            Text("선 두께: ${strokeWidth.toInt()}dp")
            Slider(
                modifier = Modifier,
                value = strokeWidth,
                onValueChange = { viewModel.onEvent(HomeUiEvent.ChangeStrokeWidth(it)) },
                valueRange = 1f..20f
            )
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
                            viewModel.onEvent(HomeUiEvent.SendButtonPressed(bitmap.asImageBitmap()))
                        }
                    }
                ) {
                    Text(text = "전송")
                }
            }
            Text(text = answer.answer)
            Text(text = if (answer.probability != 0f) answer.probability.toString() else "")
        }
    }
}

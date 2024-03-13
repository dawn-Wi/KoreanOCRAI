package com.gausslab.koreanocrai.ui.screen.labelling

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.gausslab.koreanocrai.component.addLineToBitmap
import com.gausslab.koreanocrai.ui.screen.DetailsScreen
import com.gausslab.koreanocrai.ui.screen.home.Line
import java.lang.Float
import kotlin.Unit

const val labellingScreenRoute = "labelling_screen_route"
fun NavGraphBuilder.labellingScreen() {
    composable(route = labellingScreenRoute) {
        LabellingScreen()
    }
}

fun NavController.navigateToLabellingScreen(navOptions: NavOptions? = null) {
    navigate(route = labellingScreenRoute, navOptions = navOptions)
}

@Composable
fun LabellingScreen(
    viewModel: LabellingViewModel = hiltViewModel()
) {
    val lines by viewModel.lines.collectAsState()
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    val label by viewModel.label.collectAsState()

    DetailsScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                                    x = Float.max(
                                        0f,
                                        Float.min(change.previousPosition.x, canvasSize.width)
                                    ),
                                    y = Float.max(
                                        0f,
                                        Float.min(change.previousPosition.y, canvasSize.height)
                                    )
                                )
                                val endPosition = Offset(
                                    x = Float.max(
                                        0f,
                                        Float.min(change.position.x, canvasSize.width)
                                    ),
                                    y = Float.max(
                                        0f,
                                        Float.min(change.position.y, canvasSize.height)
                                    )
                                )
                                val line = Line(
                                    start = startPosition,
                                    end = endPosition,
                                )
                                viewModel.onEvent(LabellingUiEvent.DrawLines(line))
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

            TextField(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color(0xFFD9D9D9),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .fillMaxWidth()
                    .background(Color.Transparent),
                value = label,
                onValueChange = {
                    viewModel.onEvent(LabellingUiEvent.ChangeLabelText(it))
                },
                shape = RoundedCornerShape(4.dp),
                placeholder = {
                    Text(
                        modifier = Modifier,
                        text = "라벨을 입력해주세요"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary
                )
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
                    onClick = { viewModel.onEvent(LabellingUiEvent.ClearButtonPressed) }
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
                            viewModel.onEvent(
                                LabellingUiEvent.LabellingDataSubmit(
                                    label = label,
                                    bitmap.asImageBitmap()
                                )
                            )
                        }
                    },
                    enabled = label.isNotEmpty()
                ) {
                    Text(text = "전송")
                }
            }
        }
    }
}
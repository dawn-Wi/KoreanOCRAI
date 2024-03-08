package com.gausslab.koreanocrai.ui.screen.home

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Line(
    val start: Offset,
    val end: Offset,
    val color: Color = Color.Red,
    val strokeWidth: Dp = 10.dp
)

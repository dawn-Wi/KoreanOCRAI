package com.gausslab.koreanocrai.remote

import androidx.compose.ui.graphics.ImageBitmap

data class ImageRequest(
    val image: ImageBitmap,
    val label: String
)

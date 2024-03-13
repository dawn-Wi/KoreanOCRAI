package com.gausslab.koreanocrai.component

import android.graphics.Bitmap
import android.graphics.Paint
import androidx.compose.ui.graphics.toArgb
import com.gausslab.koreanocrai.ui.screen.home.Line

fun addLineToBitmap(
    width: Int,
    height: Int,
    lineList: List<Line>
): Bitmap {
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
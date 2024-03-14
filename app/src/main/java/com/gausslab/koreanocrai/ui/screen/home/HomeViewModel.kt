package com.gausslab.koreanocrai.ui.screen.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.gausslab.koreanocrai.remote.PredictionResponse
import com.gausslab.koreanocrai.repository.KoreanOCRAIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val koreanOCRAIRepository: KoreanOCRAIRepository,
    private val navController: NavHostController
) : ViewModel() {
    private val _answer = MutableStateFlow(PredictionResponse("", 0f))
    val answer = _answer.asStateFlow()

    private val _lines = MutableStateFlow<List<Line>>(emptyList())
    val lines = _lines.asStateFlow()

    private val _selectColor = MutableStateFlow<Color>(Color.Black)
    val selectColor = _selectColor.asStateFlow()

    private val _strokeWidth = MutableStateFlow<Float>(10f)
    val strokeWidth = _strokeWidth.asStateFlow()

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.DrawLines -> {
                val lineList = mutableListOf<Line>()
                lineList.add(event.line)
                _lines.value = _lines.value + lineList
            }

            is HomeUiEvent.SendButtonPressed -> {
                viewModelScope.launch {
                    _answer.value = koreanOCRAIRepository.sendImage(event.imageBitmap)
                }
            }

            is HomeUiEvent.SelectedColor -> {
                _selectColor.value = event.color
            }

            is HomeUiEvent.ChangeStrokeWidth -> {
                _strokeWidth.value = event.width
            }

            HomeUiEvent.ClearButtonPressed -> {
                _lines.value = emptyList()
                _answer.value = PredictionResponse("", 0f)
            }

            else -> {}
        }
    }
}

sealed class HomeUiEvent {
    data class DrawLines(val line: Line) : HomeUiEvent()
    data class SendButtonPressed(val imageBitmap: ImageBitmap) : HomeUiEvent()
    data class SelectedColor(val color: Color): HomeUiEvent()
    data class ChangeStrokeWidth(val width: Float): HomeUiEvent()
    object ClearButtonPressed : HomeUiEvent()
}
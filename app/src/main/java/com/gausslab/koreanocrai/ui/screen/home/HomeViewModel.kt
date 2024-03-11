package com.gausslab.koreanocrai.ui.screen.home

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
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
    private val _answer = MutableStateFlow("")
    val answer = _answer.asStateFlow()

    private val _lines = MutableStateFlow<List<Line>>(emptyList())
    val lines = _lines.asStateFlow()

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.DrawLines -> {
                val lineList = mutableListOf<Line>()
                lineList.add(event.line)
                _lines.value = _lines.value + lineList
            }

            HomeUiEvent.ClearButtonPressed -> {
                _lines.value = emptyList()
            }

            is HomeUiEvent.SendButtonPressed -> {
                viewModelScope.launch {
                    _answer.value = koreanOCRAIRepository.sendImage(event.imageBitmap)
                }
            }
        }
    }
}

sealed class HomeUiEvent {
    data class DrawLines(val line: Line) : HomeUiEvent()
    data class SendButtonPressed(val imageBitmap: ImageBitmap): HomeUiEvent()
    object ClearButtonPressed : HomeUiEvent()
}
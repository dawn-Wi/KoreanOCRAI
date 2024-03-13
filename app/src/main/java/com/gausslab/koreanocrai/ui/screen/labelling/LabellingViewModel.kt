package com.gausslab.koreanocrai.ui.screen.labelling

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.gausslab.koreanocrai.repository.KoreanOCRAIRepository
import com.gausslab.koreanocrai.ui.screen.home.Line
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabellingViewModel @Inject constructor(
    private val koreanOCRAIRepository: KoreanOCRAIRepository,
    private val navController: NavHostController
) : ViewModel() {

    private val _lines = MutableStateFlow<List<Line>>(emptyList())
    val lines = _lines.asStateFlow()

    private val _label = MutableStateFlow("")
    val label = _label.asStateFlow()

    fun onEvent(event: LabellingUiEvent) {
        when (event) {
            is LabellingUiEvent.DrawLines -> {
                val lineList = mutableListOf<Line>()
                lineList.add(event.line)
                _lines.value = _lines.value + lineList
            }

            is LabellingUiEvent.LabellingDataSubmit -> {
                viewModelScope.launch {
                    koreanOCRAIRepository.saveLabellingImage(
                        userLabellingImage = event.image,
                        label = event.label
                    )
                    _lines.value = emptyList()
                }
            }

            is LabellingUiEvent.ChangeLabelText -> {
                _label.value = event.label
            }

            LabellingUiEvent.ClearButtonPressed -> {
                _lines.value = emptyList()
            }

        }
    }
}

sealed class LabellingUiEvent {
    data class DrawLines(val line: Line) : LabellingUiEvent()
    data class LabellingDataSubmit(val label: String, val image: ImageBitmap) : LabellingUiEvent()
    data class ChangeLabelText(val label: String) : LabellingUiEvent()
    object ClearButtonPressed : LabellingUiEvent()
}
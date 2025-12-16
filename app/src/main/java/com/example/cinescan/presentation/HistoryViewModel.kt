package com.example.cinescan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinescan.data.repository.AnalysisRepository
import com.example.cinescan.domain.model.AnalysisHistoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val analysisRepository: AnalysisRepository
) : ViewModel() {

    val historyItems: StateFlow<List<AnalysisHistoryItem>> = analysisRepository
        .getHistoryItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

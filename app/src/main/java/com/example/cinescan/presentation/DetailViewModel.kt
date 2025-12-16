package com.example.cinescan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinescan.data.local.entity.AnalysisRecordEntity
import com.example.cinescan.data.repository.AnalysisRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val analysisRepository: AnalysisRepository
) : ViewModel() {

    private val _analysisDetail = MutableStateFlow<AnalysisRecordEntity?>(null)
    val analysisDetail: StateFlow<AnalysisRecordEntity?> = _analysisDetail.asStateFlow()

    fun loadAnalysisDetail(id: Long) {
        viewModelScope.launch {
            _analysisDetail.value = analysisRepository.getAnalysisById(id)
        }
    }
}

package com.example.cinescan.data.repository

import com.example.cinescan.data.local.entity.AnalysisRecordEntity
import com.example.cinescan.domain.model.AnalysisHistoryItem
import kotlinx.coroutines.flow.Flow

interface AnalysisRepository {
    
    suspend fun saveAnalysis(analysis: AnalysisRecordEntity): Long
    
    fun getAllAnalysisOrderedByDate(): Flow<List<AnalysisRecordEntity>>
    
    suspend fun getAnalysisById(id: Long): AnalysisRecordEntity?
    
    fun getHistoryItems(): Flow<List<AnalysisHistoryItem>>
}

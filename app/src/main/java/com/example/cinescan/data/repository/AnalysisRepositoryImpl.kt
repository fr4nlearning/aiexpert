package com.example.cinescan.data.repository

import com.example.cinescan.data.local.dao.AnalysisRecordDao
import com.example.cinescan.data.local.entity.AnalysisRecordEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalysisRepositoryImpl @Inject constructor(
    private val analysisRecordDao: AnalysisRecordDao
) : AnalysisRepository {
    
    override suspend fun saveAnalysis(analysis: AnalysisRecordEntity): Long {
        return analysisRecordDao.insert(analysis)
    }
    
    override fun getAllAnalysisOrderedByDate(): Flow<List<AnalysisRecordEntity>> {
        return analysisRecordDao.getAllRecords()
    }
    
    override suspend fun getAnalysisById(id: Long): AnalysisRecordEntity? {
        return analysisRecordDao.getById(id)
    }
}

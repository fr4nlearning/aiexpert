package com.example.cinescan.data.local.dao

import androidx.room.*
import com.example.cinescan.data.local.entity.AnalysisRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnalysisRecordDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: AnalysisRecordEntity): Long
    
    @Update
    suspend fun update(record: AnalysisRecordEntity)
    
    @Delete
    suspend fun delete(record: AnalysisRecordEntity)
    
    @Query("SELECT * FROM analysis_records WHERE id = :id")
    suspend fun getById(id: Long): AnalysisRecordEntity?
    
    @Query("SELECT * FROM analysis_records ORDER BY momentoAnalisis DESC")
    fun getAllRecords(): Flow<List<AnalysisRecordEntity>>
    
    @Query("SELECT * FROM analysis_records WHERE titulo LIKE '%' || :query || '%' ORDER BY momentoAnalisis DESC")
    fun searchByTitle(query: String): Flow<List<AnalysisRecordEntity>>
    
    @Query("SELECT * FROM analysis_records WHERE tipo = :tipo ORDER BY momentoAnalisis DESC")
    fun getByType(tipo: String): Flow<List<AnalysisRecordEntity>>
    
    @Query("SELECT * FROM analysis_records WHERE plataforma = :plataforma ORDER BY momentoAnalisis DESC")
    fun getByPlatform(plataforma: String): Flow<List<AnalysisRecordEntity>>
    
    @Query("DELETE FROM analysis_records")
    suspend fun deleteAll()
}

package com.example.cinescan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "analysis_records")
data class AnalysisRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val titulo: String?,
    val tipo: String,
    val plataforma: String,
    val fechaEstrenoTexto: String?,
    val fechaEstrenoTimestamp: Long?,
    val momentoAnalisis: Long,
    val imagePath: String?
)

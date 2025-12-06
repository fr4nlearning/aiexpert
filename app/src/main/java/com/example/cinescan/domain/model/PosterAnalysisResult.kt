package com.example.cinescan.domain.model

data class PosterAnalysisResult(
    val titulo: String?,
    val tipo: PosterType,
    val plataforma: Platform,
    val fechaEstreno: String?
)


package com.example.cinescan.data.remote

import com.example.cinescan.data.remote.dto.PosterAnalysisDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Interfaz de Retrofit para comunicarse con Abacus.AI.
 */
interface AbacusApiService {
    
    /**
     * Analiza un póster de película o serie.
     * 
     * @param image Imagen del póster en formato multipart
     * @param prompt Prompt para la IA con instrucciones de análisis
     * @return DTO con el resultado del análisis
     */
    @Multipart
    @POST("analyze")
    suspend fun analyzePoster(
        @Part image: MultipartBody.Part,
        @Part("prompt") prompt: RequestBody
    ): PosterAnalysisDto
}


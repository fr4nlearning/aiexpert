package com.example.cinescan.data.remote

import com.example.cinescan.data.remote.dto.OpenAIRequestDto
import com.example.cinescan.data.remote.dto.OpenAIResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interfaz de Retrofit para comunicarse con Abacus.AI usando formato OpenAI.
 */
interface AbacusApiService {
    
    /**
     * Analiza un póster de película o serie usando visión AI.
     * 
     * @param request Petición en formato OpenAI con imagen y prompt
     * @return Respuesta de la IA con el análisis
     */
    @POST("chat/completions")
    suspend fun analyzePoster(
        @Body request: OpenAIRequestDto
    ): OpenAIResponseDto
}


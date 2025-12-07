package com.example.cinescan.data.remote

import com.example.cinescan.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor para añadir la API Key de Abacus.AI a todas las peticiones.
 */
class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Si no hay API Key configurada, continuar sin modificar la petición
        if (BuildConfig.ABACUS_API_KEY.isEmpty()) {
            return chain.proceed(originalRequest)
        }
        
        // Añadir la API Key como header
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.ABACUS_API_KEY}")
            .build()
        
        return chain.proceed(newRequest)
    }
}


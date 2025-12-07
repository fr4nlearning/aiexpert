package com.example.cinescan.data.remote.di

import com.example.cinescan.BuildConfig
import com.example.cinescan.data.remote.AbacusApiService
import com.example.cinescan.data.remote.ApiKeyInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Módulo de Hilt para proporcionar dependencias de red (Retrofit, OkHttp).
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    /**
     * Proporciona una instancia de Json configurada para kotlinx.serialization.
     */
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }
    
    /**
     * Proporciona una instancia de OkHttpClient con logging interceptor.
     * El logging solo está habilitado en modo debug.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        
        // Añadir interceptor de API Key
        builder.addInterceptor(ApiKeyInterceptor())
        
        // Añadir logging interceptor solo en modo debug
        if (BuildConfig.DEBUG_MODE) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(loggingInterceptor)
        }
        
        return builder.build()
    }
    
    /**
     * Proporciona una instancia de Retrofit configurada para Abacus.AI.
     * La baseUrl será configurada más adelante con la URL real de Abacus.AI.
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        
        return Retrofit.Builder()
            .baseUrl("https://routellm.abacus.ai/v1/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
    
    /**
     * Proporciona una instancia de AbacusApiService.
     */
    @Provides
    @Singleton
    fun provideAbacusApiService(retrofit: Retrofit): AbacusApiService {
        return retrofit.create(AbacusApiService::class.java)
    }
}


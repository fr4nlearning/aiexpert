package com.example.cinescan.data.repository.di

import com.example.cinescan.data.remote.mapper.PosterAnalysisMapper
import com.example.cinescan.data.repository.AnalysisRepository
import com.example.cinescan.data.repository.AnalysisRepositoryImpl
import com.example.cinescan.data.repository.PosterRepository
import com.example.cinescan.data.repository.PosterRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para proporcionar dependencias del repositorio.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Proporciona la implementación del PosterRepository.
     */
    @Binds
    @Singleton
    abstract fun bindPosterRepository(
        impl: PosterRepositoryImpl
    ): PosterRepository

    /**
     * Proporciona la implementación del AnalysisRepository.
     */
    @Binds
    @Singleton
    abstract fun bindAnalysisRepository(
        impl: AnalysisRepositoryImpl
    ): AnalysisRepository
    
    companion object {
        /**
         * Proporciona la instancia del mapper.
         */
        @Provides
        @Singleton
        fun providePosterAnalysisMapper(): PosterAnalysisMapper = PosterAnalysisMapper
    }
}


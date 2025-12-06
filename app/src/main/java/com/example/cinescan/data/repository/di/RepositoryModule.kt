package com.example.cinescan.data.repository.di

import com.example.cinescan.data.repository.PosterRepository
import com.example.cinescan.data.repository.PosterRepositoryImpl
import dagger.Binds
import dagger.Module
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
}


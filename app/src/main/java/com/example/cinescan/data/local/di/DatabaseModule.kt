package com.example.cinescan.data.local.di

import android.content.Context
import com.example.cinescan.data.local.AppDatabase
import com.example.cinescan.data.local.dao.AnalysisRecordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return AppDatabase.getInstance(context)
    }
    
    @Provides
    @Singleton
    fun provideAnalysisRecordDao(
        database: AppDatabase
    ): AnalysisRecordDao {
        return database.analysisRecordDao()
    }
}

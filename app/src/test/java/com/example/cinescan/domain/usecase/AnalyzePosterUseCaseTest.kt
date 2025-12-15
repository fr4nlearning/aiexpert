package com.example.cinescan.domain.usecase

import com.example.cinescan.data.local.entity.AnalysisRecordEntity
import com.example.cinescan.data.repository.AnalysisRepository
import com.example.cinescan.data.repository.PosterRepository
import com.example.cinescan.domain.model.Platform
import com.example.cinescan.domain.model.PosterAnalysisResult
import com.example.cinescan.domain.model.PosterType
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AnalyzePosterUseCaseTest {

    @Test
    fun `invoke returns success when repository provides result`() = runTest {
        val expectedResult = PosterAnalysisResult(
            titulo = "Duna",
            tipo = PosterType.PELÍCULA,
            plataforma = Platform.NETFLIX,
            fechaEstreno = "2025-10-01"
        )
        val posterRepository = FakePosterRepository { expectedResult }
        val analysisRepository = FakeAnalysisRepository()
        val useCase = AnalyzePosterUseCase(posterRepository, analysisRepository)

        val result = useCase.invoke(byteArrayOf(1, 2, 3))

        assertTrue(result.isSuccess)
        assertEquals(expectedResult, result.getOrNull())
        assertTrue(analysisRepository.savedAnalysis.isNotEmpty())
    }

    @Test
    fun `invoke returns failure when repository throws network error`() = runTest {
        val posterRepository = FakePosterRepository {
            throw IOException("timeout")
        }
        val analysisRepository = FakeAnalysisRepository()
        val useCase = AnalyzePosterUseCase(posterRepository, analysisRepository)

        val result = useCase.invoke(byteArrayOf(4, 5, 6))

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)
        assertTrue(analysisRepository.savedAnalysis.isEmpty())
    }

    @Test
    fun `invoke returns failure when repository throws invalid response`() = runTest {
        val posterRepository = FakePosterRepository {
            throw IllegalStateException("Invalid payload")
        }
        val analysisRepository = FakeAnalysisRepository()
        val useCase = AnalyzePosterUseCase(posterRepository, analysisRepository)

        val result = useCase.invoke(byteArrayOf(7, 8, 9))

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalStateException)
        assertTrue(analysisRepository.savedAnalysis.isEmpty())
    }

    @Test
    fun `invoke returns success even when local save fails`() = runTest {
        val expectedResult = PosterAnalysisResult(
            titulo = "Duna",
            tipo = PosterType.PELÍCULA,
            plataforma = Platform.NETFLIX,
            fechaEstreno = "2025-10-01"
        )
        val posterRepository = FakePosterRepository { expectedResult }
        val analysisRepository = FakeAnalysisRepository(shouldFailOnSave = true)
        val useCase = AnalyzePosterUseCase(posterRepository, analysisRepository)

        val result = useCase.invoke(byteArrayOf(1, 2, 3))

        assertTrue(result.isSuccess)
        assertEquals(expectedResult, result.getOrNull())
    }

    private class FakePosterRepository(
        private val block: suspend (ByteArray) -> PosterAnalysisResult
    ) : PosterRepository {
        override suspend fun analyzePoster(imageBytes: ByteArray): PosterAnalysisResult = block(imageBytes)
    }

    private class FakeAnalysisRepository(
        private val shouldFailOnSave: Boolean = false
    ) : AnalysisRepository {
        val savedAnalysis = mutableListOf<AnalysisRecordEntity>()

        override suspend fun saveAnalysis(analysis: AnalysisRecordEntity): Long {
            if (shouldFailOnSave) {
                throw IOException("Database error")
            }
            savedAnalysis.add(analysis)
            return savedAnalysis.size.toLong()
        }

        override fun getAllAnalysisOrderedByDate(): Flow<List<AnalysisRecordEntity>> = emptyFlow()

        override suspend fun getAnalysisById(id: Long): AnalysisRecordEntity? = null

        override fun getHistoryItems() = emptyFlow<List<com.example.cinescan.domain.model.AnalysisHistoryItem>>()
    }
}

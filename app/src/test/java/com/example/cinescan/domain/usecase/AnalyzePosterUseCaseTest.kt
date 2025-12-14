package com.example.cinescan.domain.usecase

import com.example.cinescan.data.repository.PosterRepository
import com.example.cinescan.domain.model.Platform
import com.example.cinescan.domain.model.PosterAnalysisResult
import com.example.cinescan.domain.model.PosterType
import java.io.IOException
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
        val repository = FakePosterRepository { expectedResult }
        val useCase = AnalyzePosterUseCase(repository)

        val result = useCase.invoke(byteArrayOf(1, 2, 3))

        assertTrue(result.isSuccess)
        assertEquals(expectedResult, result.getOrNull())
    }

    @Test
    fun `invoke returns failure when repository throws network error`() = runTest {
        val repository = FakePosterRepository {
            throw IOException("timeout")
        }
        val useCase = AnalyzePosterUseCase(repository)

        val result = useCase.invoke(byteArrayOf(4, 5, 6))

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)
    }

    @Test
    fun `invoke returns failure when repository throws invalid response`() = runTest {
        val repository = FakePosterRepository {
            throw IllegalStateException("Invalid payload")
        }
        val useCase = AnalyzePosterUseCase(repository)

        val result = useCase.invoke(byteArrayOf(7, 8, 9))

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalStateException)
    }

    private class FakePosterRepository(
        private val block: suspend (ByteArray) -> PosterAnalysisResult
    ) : PosterRepository {
        override suspend fun analyzePoster(imageBytes: ByteArray): PosterAnalysisResult = block(imageBytes)
    }
}

package com.example.cinescan

import app.cash.turbine.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertEquals
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Test dummy para validar la configuración del entorno de tests unitarios.
 * Verifica que las librerías de testing están correctamente configuradas:
 * - JUnit
 * - Mockito-Kotlin
 * - kotlinx-coroutines-test
 * - Turbine
 */
class TestConfigurationTest {

    @Test
    fun `test dummy - verifica configuración básica`() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `test dummy - verifica Mockito Kotlin`() = runTest {
        // Crear un mock simple
        val mock: TestInterface = mock()
        whenever(mock.getValue()).thenReturn("test")

        assertEquals("test", mock.getValue())
    }

    @Test
    fun `test dummy - verifica kotlinx-coroutines-test`() = runTest {
        var executed = false
        delay(100) // Este delay será saltado automáticamente
        executed = true
        assertEquals(true, executed)
    }

    @Test
    fun `test dummy - verifica Turbine para Flows`() = runTest {
        flowOf("uno", "dos", "tres").test {
            assertEquals("uno", awaitItem())
            assertEquals("dos", awaitItem())
            assertEquals("tres", awaitItem())
            awaitComplete()
        }
    }
}

interface TestInterface {
    fun getValue(): String
}


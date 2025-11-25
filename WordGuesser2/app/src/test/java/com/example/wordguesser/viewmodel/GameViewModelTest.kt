package com.example.wordguesser.viewmodel

import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import com.example.wordguesser.model.listaDeDesafios

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: GameViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = GameViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state loads a challenge`() {
        // Then
        assertNotNull(viewModel.uiState.desafioActual)
        assertEquals("Adivina la palabra:", viewModel.uiState.mensajeUsuario)
        assertFalse(viewModel.uiState.estaResuelto)
    }

    @Test
    fun `desafio actual is from listaDeDesafios`() {
        // Then
        val desafioActual = viewModel.uiState.desafioActual
        assertNotNull(desafioActual)
        assertTrue(listaDeDesafios.contains(desafioActual))
    }

    @Test
    fun `nuevaRonda loads a new challenge`() {
        // Given
        val primerDesafio = viewModel.uiState.desafioActual

        // When
        viewModel.nuevaRonda()

        // Then
        assertNotNull(viewModel.uiState.desafioActual)
        assertEquals("Adivina la palabra:", viewModel.uiState.mensajeUsuario)
        assertFalse(viewModel.uiState.estaResuelto)
    }

    @Test
    fun `nuevaRonda resets estaResuelto to false`() {
        // Given - mark current challenge as resolved
        val palabraCorrecta = viewModel.uiState.desafioActual?.palabraSecreta ?: ""
        viewModel.comprobarRespuesta(palabraCorrecta)
        assertTrue(viewModel.uiState.estaResuelto)

        // When
        viewModel.nuevaRonda()

        // Then
        assertFalse(viewModel.uiState.estaResuelto)
    }

    @Test
    fun `comprobarRespuesta with correct answer marks as resolved`() {
        // Given
        val palabraCorrecta = viewModel.uiState.desafioActual?.palabraSecreta ?: ""

        // When
        viewModel.comprobarRespuesta(palabraCorrecta)

        // Then
        assertTrue(viewModel.uiState.estaResuelto)
        assertTrue(viewModel.uiState.mensajeUsuario.contains("¡Excelente!"))
        assertTrue(viewModel.uiState.mensajeUsuario.contains(palabraCorrecta))
    }

    @Test
    fun `comprobarRespuesta with incorrect answer shows try again message`() {
        // Given
        val respuestaIncorrecta = "palabraincorrecta123"

        // When
        viewModel.comprobarRespuesta(respuestaIncorrecta)

        // Then
        assertFalse(viewModel.uiState.estaResuelto)
        assertTrue(viewModel.uiState.mensajeUsuario.contains("Dijiste '$respuestaIncorrecta'"))
        assertTrue(viewModel.uiState.mensajeUsuario.contains("¡Intenta de nuevo!"))
    }

    @Test
    fun `comprobarRespuesta with empty string is incorrect`() {
        // When
        viewModel.comprobarRespuesta("")

        // Then
        assertFalse(viewModel.uiState.estaResuelto)
        assertTrue(viewModel.uiState.mensajeUsuario.contains("¡Intenta de nuevo!"))
    }

    @Test
    fun `comprobarRespuesta is case sensitive`() {
        // Given
        val palabraCorrecta = viewModel.uiState.desafioActual?.palabraSecreta ?: ""
        val palabraMayusculas = palabraCorrecta.uppercase()

        // When - assuming the correct answer is lowercase
        if (palabraCorrecta != palabraMayusculas) {
            viewModel.comprobarRespuesta(palabraMayusculas)

            // Then
            assertFalse(viewModel.uiState.estaResuelto)
        }
    }

    @Test
    fun `comprobarRespuesta with partial match is incorrect`() {
        // Given
        val palabraCorrecta = viewModel.uiState.desafioActual?.palabraSecreta ?: ""
        val palabraParcial = palabraCorrecta.take(palabraCorrecta.length - 1)

        // When
        viewModel.comprobarRespuesta(palabraParcial)

        // Then
        assertFalse(viewModel.uiState.estaResuelto)
    }

    @Test
    fun `multiple incorrect attempts dont change estaResuelto`() {
        // When
        viewModel.comprobarRespuesta("intento1")
        assertFalse(viewModel.uiState.estaResuelto)

        viewModel.comprobarRespuesta("intento2")
        assertFalse(viewModel.uiState.estaResuelto)

        viewModel.comprobarRespuesta("intento3")

        // Then
        assertFalse(viewModel.uiState.estaResuelto)
    }

    @Test
    fun `correct answer after incorrect attempts works`() {
        // Given
        val palabraCorrecta = viewModel.uiState.desafioActual?.palabraSecreta ?: ""

        // When
        viewModel.comprobarRespuesta("incorrecto1")
        assertFalse(viewModel.uiState.estaResuelto)

        viewModel.comprobarRespuesta(palabraCorrecta)

        // Then
        assertTrue(viewModel.uiState.estaResuelto)
    }

    @Test
    fun `uiState maintains desafioActual after wrong answer`() {
        // Given
        val desafioInicial = viewModel.uiState.desafioActual

        // When
        viewModel.comprobarRespuesta("respuestaincorrecta")

        // Then
        assertEquals(desafioInicial, viewModel.uiState.desafioActual)
    }

    @Test
    fun `uiState maintains desafioActual after correct answer`() {
        // Given
        val desafioInicial = viewModel.uiState.desafioActual
        val palabraCorrecta = desafioInicial?.palabraSecreta ?: ""

        // When
        viewModel.comprobarRespuesta(palabraCorrecta)

        // Then
        assertEquals(desafioInicial, viewModel.uiState.desafioActual)
    }
}
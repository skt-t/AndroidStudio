package com.example.wordguesser.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    // This rule makes LiveData work synchronously in tests
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Test dispatcher for coroutines
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        // Set up the test dispatcher
        Dispatchers.setMain(testDispatcher)

        // Create ViewModel
        viewModel = LoginViewModel()
    }

    @After
    fun tearDown() {
        // Clean up
        Dispatchers.resetMain()
    }

    @Test
    fun `login with empty username shows error`() {
        // Given
        viewModel.username = ""
        viewModel.password = "123"

        // When
        viewModel.login { }

        // Then
        assertEquals("Por favor, llena todos los campos.", viewModel.errorMessage)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `login with empty password shows error`() {
        // Given
        viewModel.username = "test"
        viewModel.password = ""

        // When
        viewModel.login { }

        // Then
        assertEquals("Por favor, llena todos los campos.", viewModel.errorMessage)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `login with blank username shows error`() {
        // Given
        viewModel.username = "   "
        viewModel.password = "123"

        // When
        viewModel.login { }

        // Then
        assertEquals("Por favor, llena todos los campos.", viewModel.errorMessage)
    }

    @Test
    fun `login with blank password shows error`() {
        // Given
        viewModel.username = "test"
        viewModel.password = "   "

        // When
        viewModel.login { }

        // Then
        assertEquals("Por favor, llena todos los campos.", viewModel.errorMessage)
    }

    @Test
    fun `username is properly stored`() {
        // When
        viewModel.username = "testuser"

        // Then
        assertEquals("testuser", viewModel.username)
    }

    @Test
    fun `password is properly stored`() {
        // When
        viewModel.password = "testpass123"

        // Then
        assertEquals("testpass123", viewModel.password)
    }

    @Test
    fun `error message can be set and retrieved`() {
        // When
        viewModel.errorMessage = "Test error"

        // Then
        assertEquals("Test error", viewModel.errorMessage)
    }

    @Test
    fun `error message can be cleared`() {
        // Given
        viewModel.errorMessage = "Test error"

        // When
        viewModel.errorMessage = null

        // Then
        assertNull(viewModel.errorMessage)
    }

    @Test
    fun `isLoading starts as false`() {
        // Then
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `username starts empty`() {
        // Then
        assertEquals("", viewModel.username)
    }

    @Test
    fun `password starts empty`() {
        // Then
        assertEquals("", viewModel.password)
    }

    @Test
    fun `errorMessage starts as null`() {
        // Then
        assertNull(viewModel.errorMessage)
    }
}
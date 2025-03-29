package com.sachin_singh_dighan.newsapp.ui.languageselection

import app.cash.turbine.test
import com.sachin_singh_dighan.newsapp.utils.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.languageselection.LanguageData
import com.sachin_singh_dighan.newsapp.data.repository.languageselection.LanguageSelectionRepository
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.TestDispatcherProvider
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LanguageSelectionViewModelTest {

    @Mock
    private lateinit var languageSelectionRepository: LanguageSelectionRepository

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var logger: Logger

    private lateinit var dispatcherProvider: TestDispatcherProvider
    private lateinit var viewModel: LanguageSelectionViewModel

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun init_shouldFetchLanguageData_WHEN_networkIsAvailable() = runTest {
        // Arrange
        val mockLanguageData = listOf(
            LanguageData(languageCode = "en", languageName = "English"),
            LanguageData(languageCode = "hi", languageName = "Hindi"),
            LanguageData(languageCode = "fr", languageName = "French")
        )

        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(languageSelectionRepository.getLanguageData()).thenReturn(flowOf(mockLanguageData))

        // Act
        viewModel = LanguageSelectionViewModel(
            languageSelectionRepository,
            networkHelper,
            logger,
            dispatcherProvider
        )

        // Assert
        viewModel.uiState.test {
            val emittedItem = awaitItem()
            assertTrue(emittedItem is UiState.Success)
            assertEquals(mockLanguageData, (emittedItem as UiState.Success).data)
            cancelAndIgnoreRemainingEvents()
        }

        verify(networkHelper).isNetworkAvailable()
        verify(languageSelectionRepository).getLanguageData()
    }

    @Test
    fun init_shouldShowError_WHEN_networkIsUnavailable() = runTest {
        // Arrange
        `when`(networkHelper.isNetworkAvailable()).thenReturn(false)

        // Act
        viewModel = LanguageSelectionViewModel(
            languageSelectionRepository,
            networkHelper,
            logger,
            dispatcherProvider
        )

        // Assert
        viewModel.uiState.test {
            val emittedItem = awaitItem()
            assertTrue(emittedItem is UiState.Error)
            assertEquals(AppConstant.NETWORK_ERROR, (emittedItem as UiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }

        verify(networkHelper).isNetworkAvailable()
    }

    @Test
    fun init_shouldShowError_WHEN_repositoryThrowsException() = runTest {
        // Arrange
        val exception = IOException("Repository error")

        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(languageSelectionRepository.getLanguageData()).thenReturn(
            flow { throw exception }
        )

        // Act
        viewModel = LanguageSelectionViewModel(
            languageSelectionRepository,
            networkHelper,
            logger,
            dispatcherProvider
        )

        // Assert
        viewModel.uiState.test {
            val emittedItem = awaitItem()
            assertTrue(emittedItem is UiState.Error)
            assertEquals(exception.toString(), (emittedItem as UiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }

        verify(networkHelper).isNetworkAvailable()
        verify(languageSelectionRepository).getLanguageData()
        verify(logger).d(LanguageSelectionViewModel.TAG, exception.toString())
    }

    @Test
    fun toggleSelection_shouldAddItem_WHEN_notAlreadySelected() = runTest {
        // Arrange
        setupDefaultViewModel()

        // Act
        val result = viewModel.toggleSelection("en")

        // Assert
        assertFalse(result) // Should return false (not triggering navigation)
        assertEquals(1, viewModel.selectedItems.size)
        assertTrue(viewModel.selectedItems.contains("en"))
    }

    @Test
    fun toggleSelection_shouldRemoveItem_WHEN_alreadySelected() = runTest {
        // Arrange
        setupDefaultViewModel()
        viewModel.toggleSelection("en") // Add item first

        // Act
        val result = viewModel.toggleSelection("en")

        // Assert
        assertFalse(result) // Should return false (not triggering navigation)
        assertEquals(0, viewModel.selectedItems.size)
    }

    @Test
    fun toggleSelection_shouldReturnTrue_WHEN_secondItemIsSelected() = runTest {
        // Arrange
        setupDefaultViewModel()
        viewModel.toggleSelection("en") // Add first item

        // Act
        val result = viewModel.toggleSelection("fr")

        // Assert
        assertTrue(result) // Should return true (triggering navigation)
        assertEquals(2, viewModel.selectedItems.size)
        assertTrue(viewModel.selectedItems.contains("en"))
        assertTrue(viewModel.selectedItems.contains("fr"))
    }

    @Test
    fun toggleSelection_shouldNotAddMoreThan_2_items() = runTest {
        // Arrange
        setupDefaultViewModel()
        viewModel.toggleSelection("en") // Add first item
        viewModel.toggleSelection("fr") // Add second item

        // Act
        val result = viewModel.toggleSelection("hi")

        // Assert
        assertFalse(result) // Should return false (not adding third item)
        assertEquals(2, viewModel.selectedItems.size)
        assertFalse(viewModel.selectedItems.contains("hi"))
    }

    @Test
    fun clearSelection_shouldRemoveAllSelectedItems() = runTest {
        // Arrange
        setupDefaultViewModel()
        viewModel.toggleSelection("en")
        viewModel.toggleSelection("fr")
        assertEquals(2, viewModel.selectedItems.size)

        // Act
        viewModel.clearSelection()

        // Assert
        assertEquals(0, viewModel.selectedItems.size)
    }

    @Test
    fun isItemSelected_shouldReturnTrueForSelectedItems() = runTest {
        // Arrange
        setupDefaultViewModel()
        viewModel.toggleSelection("en")

        // Act & Assert
        assertTrue(viewModel.isItemSelected("en"))
        assertFalse(viewModel.isItemSelected("fr"))
    }

    // Helper method to create default ViewModel instance for selection tests
    private fun setupDefaultViewModel() {
        val mockLanguageData = listOf(
            LanguageData(languageCode = "en", languageName = "English"),
            LanguageData(languageCode = "hi", languageName = "Hindi"),
            LanguageData(languageCode = "fr", languageName = "French")
        )

        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(languageSelectionRepository.getLanguageData()).thenReturn(flowOf(mockLanguageData))

        viewModel = LanguageSelectionViewModel(
            languageSelectionRepository,
            networkHelper,
            logger,
            dispatcherProvider
        )
    }
}
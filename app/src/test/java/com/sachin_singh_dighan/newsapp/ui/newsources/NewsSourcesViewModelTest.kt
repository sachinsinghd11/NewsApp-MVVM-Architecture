package com.sachin_singh_dighan.newsapp.ui.newsources

import app.cash.turbine.test
import com.sachin_singh_dighan.newsapp.utils.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.newsources.Sources
import com.sachin_singh_dighan.newsapp.data.repository.newsources.NewsSourcesRepository
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.TestDispatcherProvider
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NewsSourcesViewModelTest {

    @Mock
    private lateinit var newsSourcesRepository: NewsSourcesRepository

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var logger: Logger

    private lateinit var dispatcherProvider: TestDispatcherProvider

    private lateinit var viewModel: NewsSourcesViewModel

    // Given
    // Two mock Sources objects
    private val mockSources = listOf(
        Sources(
            id = "bbc-news",
            name = "BBC News",
            description = "BBC News Channel",
            url = "https://www.bbc.com/news",
            category = "general",
            language = "en",
            country = "gb"
        ),
        Sources(
            id = "cnn",
            name = "CNN",
            description = "Cable News Network",
            url = "https://www.cnn.com",
            category = "general",
            language = "en",
            country = "us"
        )
    )

    @Before
    fun setup() {
        dispatcherProvider = TestDispatcherProvider()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchNewSource_WHEN_networkAvailableShouldUpdateUiStateWithSuccess() = runTest {

        //Given
        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(newsSourcesRepository.getNewResources()).thenReturn(flowOf(mockSources))

        // Act
        viewModel =
            NewsSourcesViewModel(newsSourcesRepository, networkHelper, logger, dispatcherProvider)

        // Assert
        viewModel.uiState.test {
            val emittedItem = awaitItem()
            assertTrue(emittedItem is UiState.Success)
            assertEquals(mockSources, (emittedItem as UiState.Success).data)
            cancelAndIgnoreRemainingEvents()
        }

        verify(networkHelper).isNetworkAvailable()
        verify(newsSourcesRepository, times(1)).getNewResources()
    }

    @Test
    fun fetchNewSource_WHEN_networkUnavailableShouldUpdateUiStateWithError() = runTest {
        // Given
        `when`(networkHelper.isNetworkAvailable()).thenReturn(false)

        // Act
        viewModel =
            NewsSourcesViewModel(newsSourcesRepository, networkHelper, logger, dispatcherProvider)

        // Assert
        viewModel.uiState.test {
            val emittedItem = awaitItem()
            assertTrue(emittedItem is UiState.Error)
            assertEquals(AppConstant.NETWORK_ERROR, (emittedItem as UiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.showErrorDialog.test {
            assertTrue(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        verify(networkHelper).isNetworkAvailable()
    }

    @Test
    fun fetchNewSource_WHEN_repositoryThrowsExceptionShouldUpdateUiStateWithError() = runTest {
        // Given
        val errorMessage = AppConstant.NETWORK_ERROR
        val exception = IOException(errorMessage)

        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(newsSourcesRepository.getNewResources()).thenReturn(flow {
            throw exception
        })

        // Act
        viewModel =
            NewsSourcesViewModel(newsSourcesRepository, networkHelper, logger, dispatcherProvider)

        // Assert
        viewModel.uiState.test {
            val emittedItem = awaitItem()
            assertTrue(emittedItem is UiState.Error)
            assertTrue((emittedItem as UiState.Error).message.contains(exception.toString()))
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.showErrorDialog.test {
            assertTrue(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        verify(networkHelper).isNetworkAvailable()
        verify(newsSourcesRepository, times(1)).getNewResources()
        verify(logger).d(NewsSourcesViewModel.TAG, exception.toString())
    }

    @Test
    fun retryOperation_shouldResetErrorDialog_AND_fetch_Sources_Again() = runTest {
        // Given - First setup for error
        `when`(networkHelper.isNetworkAvailable()).thenReturn(false)
        viewModel =
            NewsSourcesViewModel(newsSourcesRepository, networkHelper, logger, dispatcherProvider)

        // Verify initial error state
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertTrue(initialState is UiState.Error)
            cancelAndIgnoreRemainingEvents()
        }

        // Now prepare for success on retry
        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(newsSourcesRepository.getNewResources()).thenReturn(flowOf(mockSources))

        // Act
        viewModel.retryOperation()

        // Assert
        viewModel.uiState.test {
            val newState = awaitItem()
            assertTrue(newState is UiState.Success)
            assertEquals(mockSources, (newState as UiState.Success).data)
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.showErrorDialog.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun initial_state_ShouldBeLoading() = runTest {
        // Given - force delay in repository response
        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(newsSourcesRepository.getNewResources()).thenReturn(flow {
            delay(100) // Small delay
            emit(mockSources)
        })

        // Create the ViewModel but observe the state before init completes
        viewModel =
            NewsSourcesViewModel(newsSourcesRepository, networkHelper, logger, dispatcherProvider)

        // Assert
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is UiState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
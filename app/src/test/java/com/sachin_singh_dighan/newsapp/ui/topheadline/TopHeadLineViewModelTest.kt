package com.sachin_singh_dighan.newsapp.ui.topheadline

import app.cash.turbine.test
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.topheadline.ApiArticle
import com.sachin_singh_dighan.newsapp.data.model.topheadline.ApiSource
import com.sachin_singh_dighan.newsapp.data.repository.topheadline.TopHeadLineRepository
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.utils.DispatcherProvider
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.TestDispatcherProvider
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.never
import org.mockito.Mockito.reset
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class TopHeadLineViewModelTest {


    @Mock
    private lateinit var topHeadLineRepository: TopHeadLineRepository

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var logger: Logger

    private lateinit var viewModel: TopHeadLineViewModel
    private lateinit var defaultDispatcher: DispatcherProvider

    // Sample test data
    private val mockApiArticles = listOf(
        ApiArticle(
            title = "Test ApiArticle 1",
            description = "This is a test article",
            url = "https://example.com/article1",
            imageUrl = "https://example.com/image1.jpg",
            apiSource = ApiSource("cnn", "CNN"),
        ),
        ApiArticle(
            title = "Test ApiArticle 2",
            description = "This is another test article",
            url = "https://example.com/article2",
            imageUrl = "https://example.com/image2.jpg",
            apiSource = ApiSource("bbc", "BBC"),
        )
    )

    @Before
    fun setup() {
        defaultDispatcher = TestDispatcherProvider()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun init_shouldSetUiStateToLoading_WHEN_initialized() = runTest {
        // Given
        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(topHeadLineRepository.getTopHeadLinesByDefault()).thenReturn(flow {
            delay(100) // Small delay
            emit(mockApiArticles)
        })

        // When - ViewModel is initialized
        viewModel =
            TopHeadLineViewModel(topHeadLineRepository, networkHelper, logger, defaultDispatcher)

        // Then - First state should be Loading
        assert(viewModel.uiState.value is UiState.Loading)
    }

    @Test
    fun fetchTopHeadlines_shouldUpdateUiStateToSuccess_WHEN_networkIsAvailable_AND_isFetchedSuccessfully() =
        runTest {
            // Given
            `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
            `when`(topHeadLineRepository.getTopHeadLinesByDefault()).thenReturn(flowOf(mockApiArticles))

            // When
            viewModel = TopHeadLineViewModel(
                topHeadLineRepository,
                networkHelper,
                logger,
                defaultDispatcher
            )

            // Then
            viewModel.uiState.test {
                val emission = expectMostRecentItem()
                Assert.assertTrue(emission is UiState.Success)
                Assert.assertEquals(mockApiArticles, (emission as UiState.Success).data)
                cancelAndIgnoreRemainingEvents()
            }

            //verify method was called 1 time
            verify(topHeadLineRepository, times(1)).getTopHeadLinesByDefault()

            // Verify logger was called
            verify(logger).d(TopHeadLineViewModel.TAG, mockApiArticles.toString())
        }

    @Test
    fun fetchTopHeadlines_shouldUpdateUiStateToError_WHEN_anExceptionOccurs() = runTest {
        // Given
        val exception = IOException("Network error")
        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(topHeadLineRepository.getTopHeadLinesByDefault()).thenReturn(flow { throw exception })

        // When
        viewModel =
            TopHeadLineViewModel(topHeadLineRepository, networkHelper, logger, defaultDispatcher)

        // Then
        viewModel.uiState.test {
            val emission = expectMostRecentItem()
            Assert.assertTrue(emission is UiState.Error)
            Assert.assertEquals(exception.toString(), (emission as UiState.Error).message)
        }

        // Verify logger was called
        verify(topHeadLineRepository, times(1)).getTopHeadLinesByDefault()

        // Verify logger and error handling
        verify(logger).d(TopHeadLineViewModel.TAG, exception.toString())
    }

    @Test
    fun fetchTopHeadlines_shouldUpdateUiStateToErrorWithNetworkErrorMessage_WHEN_networkIsNotAvailable() =
        runTest {
            // Given
            `when`(networkHelper.isNetworkAvailable()).thenReturn(false)

            // When
            viewModel = TopHeadLineViewModel(
                topHeadLineRepository,
                networkHelper,
                logger,
                defaultDispatcher
            )

            // Then
            viewModel.uiState.test {
                val emission = expectMostRecentItem()
                Assert.assertTrue(emission is UiState.Error)
                Assert.assertEquals(AppConstant.NETWORK_ERROR, (emission as UiState.Error).message)
            }
            // Verify repository was not called
            verify(topHeadLineRepository, never()).getTopHeadLinesByDefault()
        }

    @Test
    fun retryOperation_shouldResetErrorDialog_AND_fetchHeadlinesAgain() = runTest {
        // Given
        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(topHeadLineRepository.getTopHeadLinesByDefault()).thenReturn(flowOf(mockApiArticles))

        // Initialize viewModel with network error
        `when`(networkHelper.isNetworkAvailable()).thenReturn(false)
        viewModel =
            TopHeadLineViewModel(topHeadLineRepository, networkHelper, logger, defaultDispatcher)

        // Reset mocks and prepare for retry
        reset(topHeadLineRepository, networkHelper)
        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(topHeadLineRepository.getTopHeadLinesByDefault()).thenReturn(flowOf(mockApiArticles))

        // When
        viewModel.retryOperation()
        //advanceTimeBy(1000) // Advance time to allow coroutines to complete

        // Then
        viewModel.uiState.test {
            val emission = expectMostRecentItem()
            Assert.assertTrue(emission is UiState.Success)
            Assert.assertEquals(mockApiArticles, (emission as UiState.Success).data)
        }

        // Verify showErrorDialog is reset
        Assert.assertEquals(false, viewModel.showErrorDialog.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun retryOperation_shouldShowErrorAgain_WHEN_NetworkIsStillNotAvailable() = runTest {
        // Given
        `when`(networkHelper.isNetworkAvailable()).thenReturn(false)
        viewModel =
            TopHeadLineViewModel(topHeadLineRepository, networkHelper, logger, defaultDispatcher)

        // Reset error dialog state for clean test
        clearInvocations(networkHelper)

        // When
        viewModel.retryOperation()
        ///advanceTimeBy(1000) // Advance time to allow coroutines to complete
//runCurrent()
        // Then
        viewModel.uiState.test {
            val emission = expectMostRecentItem()
            Assert.assertTrue(emission is UiState.Error)
            Assert.assertEquals(AppConstant.NETWORK_ERROR, (emission as UiState.Error).message)
        }

        // Verify network was checked again
        verify(networkHelper, times(1)).isNetworkAvailable()
    }

    @Test
    fun fetchTopHeadlines_shouldHandleEmptyListFromRepository() = runTest {
        // Given
        val emptyList = emptyList<ApiArticle>()
        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(topHeadLineRepository.getTopHeadLinesByDefault()).thenReturn(flowOf(emptyList))

        // When
        viewModel =
            TopHeadLineViewModel(topHeadLineRepository, networkHelper, logger, defaultDispatcher)

        // Then
        viewModel.uiState.test {
            val emission = expectMostRecentItem()
            Assert.assertTrue(emission is UiState.Success)
            Assert.assertTrue((emission as UiState.Success).data.isEmpty())
        }
    }

}
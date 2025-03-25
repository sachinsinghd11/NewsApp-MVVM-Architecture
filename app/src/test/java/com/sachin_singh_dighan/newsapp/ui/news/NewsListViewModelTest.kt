package com.sachin_singh_dighan.newsapp.ui.news

import app.cash.turbine.test
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.topheadline.ApiArticle
import com.sachin_singh_dighan.newsapp.data.model.topheadline.ApiSource
import com.sachin_singh_dighan.newsapp.data.repository.topheadline.TopHeadLineRepository
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
class NewsListViewModelTest {

    @Mock
    private lateinit var topHeadLineRepository: TopHeadLineRepository

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var logger: Logger

    private lateinit var viewModel: NewsListViewModel

    private lateinit var dispatcherProvider: TestDispatcherProvider

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
        viewModel =
            NewsListViewModel(topHeadLineRepository, networkHelper, logger, dispatcherProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchNewsByCategory_WHEN_networkAvailableReturnsSuccess() = runTest {
        // Arrange
        val sourceId = "bbc-news"
        val mockApiArticles = listOf(
            ApiArticle(
                title = "Test ApiArticle 1",
                description = "Test Description 1",
                url = "https://example.com/article1",
                imageUrl = "https://example.com/image1.jpg",
                apiSource = ApiSource("cnn", "CNN"),
            ),
            ApiArticle(
                title = "Test ApiArticle 2",
                description = "Test Description 2",
                url = "https://example.com/article2",
                imageUrl = "https://example.com/image2.jpg",
                apiSource = ApiSource("bbc", "BBC"),
            )
        )

        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(topHeadLineRepository.getTopHeadLinesByCategory(sourceId)).thenReturn(
            flowOf(
                mockApiArticles
            )
        )

        // Act
        viewModel.fetchNewsByCategory(sourceId)

        // Assert
        viewModel.uiState.test {
            val emittedItem = awaitItem()
            assertTrue(emittedItem is UiState.Success)
            assertEquals(mockApiArticles, (emittedItem as UiState.Success).data)
            cancelAndIgnoreRemainingEvents()
        }

        verify(topHeadLineRepository).getTopHeadLinesByCategory(sourceId)
        verify(networkHelper).isNetworkAvailable()
    }

    @Test
    fun fetchNewsByCategory_WHEN_networkUnavailableReturnsError() = runTest {
        // Arrange
        val sourceId = "bbc-news"

        `when`(networkHelper.isNetworkAvailable()).thenReturn(false)

        // Act
        viewModel.fetchNewsByCategory(sourceId)

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
    fun fetchNewsByCategory_WHEN_RepositoryThrowsExceptionReturnsError() = runTest {
        // Arrange
        val sourceId = "bbc-news"
        val exception = IOException("API Error")

        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(topHeadLineRepository.getTopHeadLinesByCategory(sourceId)).thenReturn(
            flow { throw exception }
        )

        // Act
        viewModel.fetchNewsByCategory(sourceId)

        // Assert
        viewModel.uiState.test {
            val emittedItem = awaitItem()
            assertTrue(emittedItem is UiState.Error)
            assertEquals(exception.toString(), (emittedItem as UiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }

        verify(topHeadLineRepository).getTopHeadLinesByCategory(sourceId)
        verify(networkHelper).isNetworkAvailable()
        verify(logger).d(NewsListViewModel.TAG, exception.toString())
    }

    @Test
    fun fetchNewsByCountry_WHEN_networkAvailableReturnsSuccess() = runTest {
        // Arrange
        val country = "us"
        val mockApiArticles = listOf(
            ApiArticle(
                title = "US News ApiArticle",
                description = "US News Description",
                url = "https://example.com/us-news",
                imageUrl = "https://example.com/us-image.jpg",
                apiSource = ApiSource("cnn", "CNN"),
            )
        )

        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(topHeadLineRepository.getTopHeadLinesByCountry(country)).thenReturn(
            flowOf(
                mockApiArticles
            )
        )

        // Act
        viewModel.fetchNewsByCountry(country)

        // Assert
        viewModel.uiState.test {
            val emittedItem = awaitItem()
            assertTrue(emittedItem is UiState.Success)
            assertEquals(mockApiArticles, (emittedItem as UiState.Success).data)
            cancelAndIgnoreRemainingEvents()
        }

        verify(topHeadLineRepository).getTopHeadLinesByCountry(country)
        verify(networkHelper).isNetworkAvailable()
    }

    @Test
    fun fetchNewsByCountry_WHEN_networkUnavailableReturnsError() = runTest {
        // Arrange
        val country = "us"

        `when`(networkHelper.isNetworkAvailable()).thenReturn(false)

        // Act
        viewModel.fetchNewsByCountry(country)

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
    fun fetchNewsByLanguage_WHEN_networkAvailableReturnsCombinedResults() = runTest {
        // Arrange
        val languages = listOf("en", "fr")

        val englishApiArticles = listOf(
            ApiArticle(
                title = "English ApiArticle 1",
                description = "English Description 1",
                url = "https://example.com/english1",
                imageUrl = "https://example.com/image1.jpg",
                apiSource = ApiSource("cnn", "CNN"),
            ),
            ApiArticle(
                title = "English ApiArticle 2",
                description = "English Description 2",
                url = "https://example.com/english2",
                imageUrl = "https://example.com/image1.jpg",
                apiSource = ApiSource("cnn", "CNN"),
            )
        )

        val frenchApiArticles = listOf(
            ApiArticle(
                title = "French ApiArticle 1",
                description = "French Description 1",
                url = "https://example.com/french1",
                imageUrl = "https://example.com/image1.jpg",
                apiSource = ApiSource("cnn", "CNN"),
            )
        )

        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(topHeadLineRepository.getTopHeadLinesByLanguage(languages[0])).thenReturn(
            flowOf(
                englishApiArticles
            )
        )
        `when`(topHeadLineRepository.getTopHeadLinesByLanguage(languages[1])).thenReturn(
            flowOf(
                frenchApiArticles
            )
        )

        // Act
        viewModel.fetchNewsByLanguage(languages)

        // Assert
        viewModel.uiState.test {
            val emittedItem = awaitItem()
            assertTrue(emittedItem is UiState.Success)

            // Combined list should contain all apiArticles
            val combinedArticles = (emittedItem as UiState.Success).data
            assertEquals(3, combinedArticles.size)

            // Check that all apiArticles from both languages are present
            val titles = combinedArticles.map { it.title }.toSet()
            assertTrue(
                titles.containsAll(
                    setOf(
                        "English ApiArticle 1",
                        "English ApiArticle 2",
                        "French ApiArticle 1"
                    )
                )
            )

            cancelAndIgnoreRemainingEvents()
        }

        verify(topHeadLineRepository).getTopHeadLinesByLanguage(languages[0])
        verify(topHeadLineRepository).getTopHeadLinesByLanguage(languages[1])
        verify(networkHelper).isNetworkAvailable()
    }

    @Test
    fun fetchNewsByLanguage_WHEN_networkUnavailableReturnsError() = runTest {
        // Arrange
        val languages = listOf("en", "fr")

        `when`(networkHelper.isNetworkAvailable()).thenReturn(false)

        // Act
        viewModel.fetchNewsByLanguage(languages)

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
    fun fetchNewsByLanguage_WHEN_repositoryThrowsExceptionReturnsError() = runTest {
        // Arrange
        val languages = listOf("en", "fr")
        val exception = IOException("API Error")

        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(topHeadLineRepository.getTopHeadLinesByLanguage(languages[0])).thenReturn(
            flow { throw exception }
        )

        // Act
        viewModel.fetchNewsByLanguage(languages)

        // Assert
        viewModel.uiState.test {
            val emittedItem = awaitItem()
            assertTrue(emittedItem is UiState.Error)
            assertEquals(exception.toString(), (emittedItem as UiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }

        verify(topHeadLineRepository).getTopHeadLinesByLanguage(languages[0])
        verify(networkHelper).isNetworkAvailable()
        verify(logger).d(NewsListViewModel.TAG, exception.toString())
    }
}
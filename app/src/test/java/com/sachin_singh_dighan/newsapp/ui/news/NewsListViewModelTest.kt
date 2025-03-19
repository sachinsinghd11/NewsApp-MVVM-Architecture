package com.sachin_singh_dighan.newsapp.ui.news

import app.cash.turbine.test
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.topheadline.Article
import com.sachin_singh_dighan.newsapp.data.model.topheadline.Source
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
        val mockArticles = listOf(
            Article(
                title = "Test Article 1",
                description = "Test Description 1",
                url = "https://example.com/article1",
                imageUrl = "https://example.com/image1.jpg",
                source = Source("cnn", "CNN"),
            ),
            Article(
                title = "Test Article 2",
                description = "Test Description 2",
                url = "https://example.com/article2",
                imageUrl = "https://example.com/image2.jpg",
                source = Source("bbc", "BBC"),
            )
        )

        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(topHeadLineRepository.getTopHeadLinesByCategory(sourceId)).thenReturn(
            flowOf(
                mockArticles
            )
        )

        // Act
        viewModel.fetchNewsByCategory(sourceId)

        // Assert
        viewModel.uiState.test {
            val emittedItem = awaitItem()
            assertTrue(emittedItem is UiState.Success)
            assertEquals(mockArticles, (emittedItem as UiState.Success).data)
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
        val mockArticles = listOf(
            Article(
                title = "US News Article",
                description = "US News Description",
                url = "https://example.com/us-news",
                imageUrl = "https://example.com/us-image.jpg",
                source = Source("cnn", "CNN"),
            )
        )

        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(topHeadLineRepository.getTopHeadLinesByCountry(country)).thenReturn(
            flowOf(
                mockArticles
            )
        )

        // Act
        viewModel.fetchNewsByCountry(country)

        // Assert
        viewModel.uiState.test {
            val emittedItem = awaitItem()
            assertTrue(emittedItem is UiState.Success)
            assertEquals(mockArticles, (emittedItem as UiState.Success).data)
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

        val englishArticles = listOf(
            Article(
                title = "English Article 1",
                description = "English Description 1",
                url = "https://example.com/english1",
                imageUrl = "https://example.com/image1.jpg",
                source = Source("cnn", "CNN"),
            ),
            Article(
                title = "English Article 2",
                description = "English Description 2",
                url = "https://example.com/english2",
                imageUrl = "https://example.com/image1.jpg",
                source = Source("cnn", "CNN"),
            )
        )

        val frenchArticles = listOf(
            Article(
                title = "French Article 1",
                description = "French Description 1",
                url = "https://example.com/french1",
                imageUrl = "https://example.com/image1.jpg",
                source = Source("cnn", "CNN"),
            )
        )

        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(topHeadLineRepository.getTopHeadLinesByLanguage(languages[0])).thenReturn(
            flowOf(
                englishArticles
            )
        )
        `when`(topHeadLineRepository.getTopHeadLinesByLanguage(languages[1])).thenReturn(
            flowOf(
                frenchArticles
            )
        )

        // Act
        viewModel.fetchNewsByLanguage(languages)

        // Assert
        viewModel.uiState.test {
            val emittedItem = awaitItem()
            assertTrue(emittedItem is UiState.Success)

            // Combined list should contain all articles
            val combinedArticles = (emittedItem as UiState.Success).data
            assertEquals(3, combinedArticles.size)

            // Check that all articles from both languages are present
            val titles = combinedArticles.map { it.title }.toSet()
            assertTrue(
                titles.containsAll(
                    setOf(
                        "English Article 1",
                        "English Article 2",
                        "French Article 1"
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
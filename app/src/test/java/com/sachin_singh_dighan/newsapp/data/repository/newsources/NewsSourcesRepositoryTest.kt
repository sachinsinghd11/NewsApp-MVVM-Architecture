package com.sachin_singh_dighan.newsapp.data.repository.newsources

import app.cash.turbine.test
import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.data.model.newsources.NewSourceResponse
import com.sachin_singh_dighan.newsapp.data.model.newsources.Sources
import com.sachin_singh_dighan.newsapp.utils.DispatcherProvider
import com.sachin_singh_dighan.newsapp.utils.TestDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NewsSourcesRepositoryTest {

    @Mock
    private lateinit var networkService: NetworkService

    private lateinit var newsSourcesRepository: NewsSourcesRepository

    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
        newsSourcesRepository = NewsSourcesRepository(networkService)
    }

    @Test
    fun getNewResources_shouldReturnDistinctSourcesByCategory() = runTest {
        // Arrange
        val mockSources = listOf(
            Sources(
                id = "bbc-news",
                name = "BBC News",
                description = "BBC News is an operational business division of the British Broadcasting Corporation responsible for the gathering and broadcasting of news and current affairs.",
                url = "https://www.bbc.com/news",
                category = "general",
                language = "en",
                country = "gb"
            ),
            Sources(
                id = "cnn",
                name = "CNN",
                description = "CNN (Cable News Network) is an American news-based pay television channel owned by CNN Worldwide, a unit of the WarnerMedia News & Sports division of AT&T's WarnerMedia.",
                url = "https://www.cnn.com",
                category = "general", // Same category as BBC News
                language = "en",
                country = "us"
            ),
            Sources(
                id = "espn",
                name = "ESPN",
                description = "ESPN is a U.S.-based sports television channel owned by ESPN Inc., a joint venture owned by The Walt Disney Company and Hearst Communications.",
                url = "https://www.espn.com",
                category = "sports", // Different category
                language = "en",
                country = "us"
            )
        )

        val mockResponse = NewSourceResponse(
            status = "ok",
            sources = mockSources
        )

        `when`(networkService.getNewResources()).thenReturn(mockResponse)

        // Act & Assert
        newsSourcesRepository.getNewResources().test {
            val result = awaitItem()

            // Expecting 2 sources because we should get distinct by category (general and sports)
            assertEquals(2, result.size)

            // Verify first source is in the result (should be BBC News since it comes first in the list)
            assertEquals("bbc-news", result[0].id)
            assertEquals("general", result[0].category)

            // Verify sports category is in the result
            assertEquals("espn", result[1].id)
            assertEquals("sports", result[1].category)

            awaitComplete()
        }
    }

    @Test
    fun getNewResources_shouldReturnEmptyList_WHEN_API_ReturnsEmptySources() = runTest {
        // Arrange
        val mockResponse = NewSourceResponse(
            status = "ok",
            sources = emptyList()
        )

        `when`(networkService.getNewResources()).thenReturn(mockResponse)

        // Act & Assert
        newsSourcesRepository.getNewResources().test {
            val result = awaitItem()
            assertEquals(0, result.size)
            awaitComplete()
        }
    }

    @Test
    fun getNewResources_shouldPropagateException_WHEN_API_callFails() = runTest {
        // Arrange
        val exception = RuntimeException("API Error")
        `when`(networkService.getNewResources()).thenThrow(exception)

        // Act & Assert
        newsSourcesRepository.getNewResources().test {
            val error = awaitError()
            assertEquals("API Error", error.message)
            assertEquals(RuntimeException::class.java, error.javaClass)
        }
    }

    @Test
    fun getNewResources_shouldHandleAllSourcesWithDifferentCategories() = runTest {
        // Arrange
        val mockSources = listOf(
            Sources(
                id = "bbc-news",
                name = "BBC News",
                description = "BBC News description",
                url = "https://www.bbc.com/news",
                category = "general",
                language = "en",
                country = "gb"
            ),
            Sources(
                id = "financial-times",
                name = "Financial Times",
                description = "Financial Times description",
                url = "https://www.ft.com",
                category = "business",
                language = "en",
                country = "gb"
            ),
            Sources(
                id = "espn",
                name = "ESPN",
                description = "ESPN description",
                url = "https://www.espn.com",
                category = "sports",
                language = "en",
                country = "us"
            ),
            Sources(
                id = "techcrunch",
                name = "TechCrunch",
                description = "TechCrunch description",
                url = "https://techcrunch.com",
                category = "technology",
                language = "en",
                country = "us"
            )
        )

        val mockResponse = NewSourceResponse(
            status = "ok",
            sources = mockSources
        )

        `when`(networkService.getNewResources()).thenReturn(mockResponse)

        // Act & Assert
        newsSourcesRepository.getNewResources().test {
            val result = awaitItem()

            // Should return all 4 sources as they all have different categories
            assertEquals(4, result.size)

            // Verify all categories are present
            val categories = result.map { it.category }.toSet()
            assertEquals(setOf("general", "business", "sports", "technology"), categories)

            awaitComplete()
        }
    }
}
package com.sachin_singh_dighan.newsapp.data.repository.topheadline

import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.data.model.topheadline.ApiArticle
import com.sachin_singh_dighan.newsapp.data.model.topheadline.ApiSource
import com.sachin_singh_dighan.newsapp.data.model.topheadline.TopHeadLinesResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TopHeadLineRepositoryTest {

    @Mock
    private lateinit var networkService: NetworkService

    private lateinit var topHeadLineRepository: TopHeadLineRepository

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

    private val mockResponse = TopHeadLinesResponse(
        status = "ok",
        totalResults = mockApiArticles.size,
        apiArticles = mockApiArticles
    )

    @Before
    fun setUp() {
        topHeadLineRepository = TopHeadLineRepository(networkService)
    }

    @Test
    fun getTopHeadLinesByDefault_shouldReturnArticlesFromNetworkService() = runTest {
        // Given
        `when`(networkService.getTopHeadLinesByCountry(AppConstant.NEWS_BY_DEFAULT))
            .thenReturn(mockResponse)

        // When
        val result = topHeadLineRepository.getTopHeadLinesByDefault().first()

        // Then
        verify(networkService).getTopHeadLinesByCountry(AppConstant.NEWS_BY_DEFAULT)
        assertEquals(mockApiArticles, result)
    }

    @Test
    fun getTopHeadLinesByCategory_shouldReturnArticlesForSpecifiedCategory() = runTest {
        // Given
        val category = "technology"
        `when`(networkService.getTopHeadLinesByCategory(category))
            .thenReturn(mockResponse)

        // When
        val result = topHeadLineRepository.getTopHeadLinesByCategory(category).first()

        // Then
        verify(networkService).getTopHeadLinesByCategory(category)
        assertEquals(mockApiArticles, result)
    }

    @Test
    fun getTopHeadLinesByCountry_shouldReturnArticlesForSpecifiedCountry() = runTest {
        // Given
        val country = "us"
        `when`(networkService.getTopHeadLinesByCountry(country))
            .thenReturn(mockResponse)

        // When
        val result = topHeadLineRepository.getTopHeadLinesByCountry(country).first()

        // Then
        verify(networkService).getTopHeadLinesByCountry(country)
        assertEquals(mockApiArticles, result)
    }

    @Test
    fun getTopHeadLinesByLanguage_shouldReturnArticlesForSpecifiedLanguage() = runTest {
        // Given
        val language = "en"
        `when`(networkService.getTopHeadLinesByLanguage(language))
            .thenReturn(mockResponse)

        // When
        val result = topHeadLineRepository.getTopHeadLinesByLanguage(language).first()

        // Then
        verify(networkService).getTopHeadLinesByLanguage(language)
        assertEquals(mockApiArticles, result)
    }

    @Test
    fun getTopHeadLinesByDefault_shouldPropagateExceptionFromNetworkService() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        `when`(networkService.getTopHeadLinesByCountry(AppConstant.NEWS_BY_DEFAULT))
            .thenThrow(exception)

        // When & Then
        try {
            topHeadLineRepository.getTopHeadLinesByDefault().first()
            assert(false) { "Expected an exception but none was thrown" }
        } catch (e: RuntimeException) {
            assertEquals("Network error", e.message)
        }
    }

    @Test
    fun getTopHeadLinesByCategory_shouldPropagateExceptionFromNetworkService() = runTest {
        // Given
        val category = "business"
        val exception = RuntimeException("API error")
        `when`(networkService.getTopHeadLinesByCategory(category))
            .thenThrow(exception)

        // When & Then
        try {
            topHeadLineRepository.getTopHeadLinesByCategory(category).first()
            assert(false) { "Expected an exception but none was thrown" }
        } catch (e: RuntimeException) {
            assertEquals("API error", e.message)
        }
    }
}
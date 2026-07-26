package com.sachin_singh_dighan.newsapp.data.repository.topheadline

import com.sachin_singh_dighan.newsapp.BuildConfig
import com.sachin_singh_dighan.newsapp.data.api.NetworkSecurityService
import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.data.model.topheadline.ApiArticle
import com.sachin_singh_dighan.newsapp.utils.AppConstant
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopHeadLineRepository @Inject constructor(
    private val networkService: NetworkService,
    private val networkSecurityService: NetworkSecurityService,
    private val logger: Logger
) {
    companion object {
        private const val TAG = "TopHeadLineRepository"
    }

    fun getTopHeadLinesByDefault(): Flow<List<ApiArticle>> {
        return flow {
            // Validate API request
            if (!networkSecurityService.validateApiRequest(AppConstant.TOP_HEADLINES_ENDPOINT )) {
                if (BuildConfig.DEBUG) {
                    logger.d(TAG, "API request validation failed for getTopHeadlines")
                }
                throw Exception("Request validation failed. Please check your network connection and try again.")
            }
            val response = networkService.getTopHeadLinesByCountry(AppConstant.NEWS_BY_DEFAULT)

            if (BuildConfig.DEBUG) {
                logger.d(TAG, "API call successful for getTopHeadlines")
                logger.d(TAG, "Articles count: ${response.apiArticles.size}")
            }
            emit(response)
        }.map {
            it.apiArticles
        }.catch { e ->
            if (BuildConfig.DEBUG) {
                logger.d(TAG, "Error in getTopHeadlines: ${e.message}")
            }
            emit(emptyList())
        }.onStart {
            if (BuildConfig.DEBUG) {
                logger.d(TAG, "Starting getTopHeadlines flow")
            }
        }
    }

    fun getTopHeadLinesByCategory(category: String): Flow<List<ApiArticle>> {
        return flow {
            if (!networkSecurityService.validateApiRequest(category)) {
                if (BuildConfig.DEBUG) {
                    logger.d(TAG, "API request validation failed for getTopHeadlinesByCategory")
                }
                throw Exception("Request validation failed for category: $category")
            }
            emit(networkService.getTopHeadLinesByCategory(category))
        }.map {
            it.apiArticles
        }.catch { exception ->
            if (BuildConfig.DEBUG) {
                logger.d(TAG, "Exception in getTopHeadlinesByCategory: ${exception.message}")
            }
            emit(emptyList())
        }
    }

    fun getTopHeadLinesByCountry(country: String): Flow<List<ApiArticle>> {
        return flow {
            if (!networkSecurityService.validateApiRequest(country)) {
                if (BuildConfig.DEBUG) {
                    logger.d(TAG, "API request validation failed for getTopHeadLinesByCountry")
                }
                throw Exception("Request validation failed for country: $country")
            }
            emit(networkService.getTopHeadLinesByCountry(country))
        }.map {
            it.apiArticles
        }.catch { exception ->
            if (BuildConfig.DEBUG) {
                logger.d(TAG, "Exception in getTopHeadlinesByCountry: ${exception.message}")
            }
            emit(emptyList())
        }
    }

    fun getTopHeadLinesByLanguage(language: String): Flow<List<ApiArticle>> {
        return flow {
            if (!networkSecurityService.validateApiRequest(language)) {
                if (BuildConfig.DEBUG) {
                    logger.d(TAG, "API request validation failed for getTopHeadLinesByLanguage")
                }
                throw Exception("Request validation failed for language: $language")
            }
            emit(networkService.getTopHeadLinesByLanguage(language))
        }.map {
            it.apiArticles
        }.catch { exception ->
            if (BuildConfig.DEBUG) {
                logger.d(TAG, "Exception in getTopHeadLinesByLanguage: ${exception.message}")
            }
            emit(emptyList())
        }
    }
}
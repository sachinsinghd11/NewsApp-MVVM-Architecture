package com.sachin_singh_dighan.newsapp.data.repository.newsources

import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.data.model.newsources.Sources
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class NewSourcesRepository @Inject constructor(private val networkService: NetworkService) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getNewResources(): Flow<List<Sources>> {
        return flow {
            emit(networkService.getNewResources().sources)
        }.mapLatest { list ->
            list.distinctBy {
                it.category
            }
        }
    }
}
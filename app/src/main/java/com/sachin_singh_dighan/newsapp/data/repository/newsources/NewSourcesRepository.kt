package com.sachin_singh_dighan.newsapp.data.repository.newsources

import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.data.model.newsources.Sources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewSourcesRepository @Inject constructor(private  val networkService: NetworkService){
    fun getNewResources(): Flow<List<Sources>> {
        return flow {
            emit(networkService.getNewResources())
        }.map {
            it.sources
        }
    }
}
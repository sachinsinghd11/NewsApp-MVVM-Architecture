package com.sachin_singh_dighan.newsapp.data.repository.topheadlinepaging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.data.model.topheadline.ApiArticle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TopHeadlinePagingSourceRepository @Inject constructor(
    private val networkService: NetworkService
) {
    fun getTopHeadlineByPagination(): Flow<PagingData<ApiArticle>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TopHeadlinePagingSource(networkService) }
        ).flow
    }
}
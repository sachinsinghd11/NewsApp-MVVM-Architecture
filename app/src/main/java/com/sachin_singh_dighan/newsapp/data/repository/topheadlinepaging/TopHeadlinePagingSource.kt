package com.sachin_singh_dighan.newsapp.data.repository.topheadlinepaging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.api.NetworkService
import com.sachin_singh_dighan.newsapp.data.model.topheadline.ApiArticle

class TopHeadlinePagingSource(
    private val networkService: NetworkService
) : PagingSource<Int, ApiArticle>() {
    override fun getRefreshKey(state: PagingState<Int, ApiArticle>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ApiArticle> {
        return try {
            val page = params.key ?: AppConstant.INITIAL_PAGE

            val response = networkService.getTopHeadLinesPaginationByCountry(
                country = AppConstant.NEWS_BY_DEFAULT,
                page = page,
                pageSize = AppConstant.PAGE_SIZE
            )

            LoadResult.Page(
                data = response.apiArticles,
                prevKey = if (page == AppConstant.INITIAL_PAGE) null else page.minus(1),
                nextKey = if (response.apiArticles.isEmpty()) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
package com.example.imagevista.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.imagevista.data.mapper.toDomainModelList
import com.example.imagevista.data.remote.UnsplashApiService
import com.example.imagevista.data.util.Constant
import com.example.imagevista.domain.model.UnsplashImage

class SearchPagingSource(
    private val query: String,
    private val unsplashApiService: UnsplashApiService
) : PagingSource<Int, UnsplashImage>() {

    companion object{
        private const val STARTING_PAGE_INDEX = 1
    }


    override fun getRefreshKey(state: PagingState<Int, UnsplashImage>): Int? {
        Log.d(Constant.IV_LOG_TAG, "getRefresh: ${state.anchorPosition}" )
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashImage> {
        val currentPage = params.key ?: STARTING_PAGE_INDEX
        Log.d(Constant.IV_LOG_TAG, "currentPage: $currentPage" )
        return try {
            val response = unsplashApiService.searchImage(
                query = query,
                page = currentPage,
                perPage = params.loadSize
            )
            val endOfPaginationReached = response.image.isEmpty()
            Log.d(Constant.IV_LOG_TAG, "Load Result Response: ${response.image.toDomainModelList()}" )
            Log.d(Constant.IV_LOG_TAG, "endOfPaginationReached: $endOfPaginationReached" )
            LoadResult.Page(
                data = response.image.toDomainModelList(),
                prevKey = if (currentPage == STARTING_PAGE_INDEX) null else currentPage - 1,
                nextKey =if (endOfPaginationReached) null else currentPage + 1
            )
        } catch (e: Exception) {
            Log.d(Constant.IV_LOG_TAG, "LoadResultError: ${e.message}" )
            LoadResult.Error(e)
        }
    }
}
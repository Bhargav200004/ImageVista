package com.example.imagevista.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.imagevista.data.mapper.toDomainModel
import com.example.imagevista.data.mapper.toDomainModelList
import com.example.imagevista.data.paging.SearchPagingSource
import com.example.imagevista.data.remote.UnsplashApiService
import com.example.imagevista.data.util.Constant.ITEMS_PER_PAGE
import com.example.imagevista.domain.model.UnsplashImage
import com.example.imagevista.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow

class ImageRepositoryImpl(
    private val unsplashApiService: UnsplashApiService
) : ImageRepository  {
    override suspend fun getEditorialFeedImage(): List<UnsplashImage> {
        return unsplashApiService.getEditorialFeedImage().toDomainModelList()
    }

    override suspend fun getImage(imageId: String): UnsplashImage {
        return unsplashApiService.getImage(imageId = imageId).toDomainModel()
    }

    override fun searchImages(query: String): Flow<PagingData<UnsplashImage>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {
                SearchPagingSource(query, unsplashApiService)
            }
        ).flow
    }

}
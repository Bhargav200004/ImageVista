package com.example.imagevista.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.imagevista.data.local.ImageVistaDatabase
import com.example.imagevista.data.mapper.toDomainModel
import com.example.imagevista.data.mapper.toFavoriteImageEntity
import com.example.imagevista.data.paging.EditorialFeedRemoteMediator
import com.example.imagevista.data.paging.SearchPagingSource
import com.example.imagevista.data.remote.UnsplashApiService
import com.example.imagevista.data.util.Constant.ITEMS_PER_PAGE
import com.example.imagevista.domain.model.UnsplashImage
import com.example.imagevista.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class ImageRepositoryImpl(
    private val unsplashApiService: UnsplashApiService,
    private val database : ImageVistaDatabase
) : ImageRepository  {

    private val favoriteImageDao = database.favoriteImagesDao()
    private val editorialImageDao = database.editorialFeedDao()


    override fun getEditorialFeedImage(): Flow<PagingData<UnsplashImage>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            remoteMediator = EditorialFeedRemoteMediator(unsplashApiService , database),
            pagingSourceFactory = {
                editorialImageDao.getAllEditorialFeedImages()
            }
        ).flow
            .map { pagingData->
                pagingData.map {
                    it.toDomainModel()
                }
            }
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

    override fun getAllFavoriteImages(): Flow<PagingData<UnsplashImage>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {
                favoriteImageDao.getAllFavoriteImage()
            }
        ).flow
            .map { pagingData ->
                pagingData.map { it.toDomainModel()}
            }
    }

    override suspend fun toggleFavoriteStatus(image: UnsplashImage) {
        val isFavorite = favoriteImageDao.isImageFavorite(image.id)
        val favoriteImage = image.toFavoriteImageEntity()

        if (isFavorite){
            favoriteImageDao.deleteFavoriteImage(favoriteImage)
        }
        else{
            favoriteImageDao.insertFavoriteImage(favoriteImage)
        }
    }

    override fun getFavoriteImageIds(): Flow<List<String>> {
        return favoriteImageDao.getFavoriteImageIds()
    }

}
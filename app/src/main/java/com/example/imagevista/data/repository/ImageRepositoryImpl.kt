package com.example.imagevista.data.repository

import com.example.imagevista.data.mapper.toDomainModelList
import com.example.imagevista.data.remote.UnsplashApiService
import com.example.imagevista.domain.model.UnsplashImage
import com.example.imagevista.domain.repository.ImageRepository

class ImageRepositoryImpl(
    private val unsplashApiService: UnsplashApiService
) : ImageRepository  {
    override suspend fun getEditorialFeedImage(): List<UnsplashImage> {
        return unsplashApiService.getEditorialFeedImage().toDomainModelList()
    }

}
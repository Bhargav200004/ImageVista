package com.example.imagevista.domain.repository

import com.example.imagevista.domain.model.UnsplashImage

interface ImageRepository {

    suspend fun getEditorialFeedImage () : List<UnsplashImage>
}
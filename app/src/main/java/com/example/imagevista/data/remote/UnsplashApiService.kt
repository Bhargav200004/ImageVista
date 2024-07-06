package com.example.imagevista.data.remote

import com.example.imagevista.data.remote.dto.UnsplashImageDto
import com.example.imagevista.data.util.Constant.API_KEY
import com.example.imagevista.domain.model.UnsplashImage
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface UnsplashApiService {

    @Headers("Authorization: Client-ID $API_KEY")
    @GET("/photos")
    suspend fun getEditorialFeedImage() : List<UnsplashImageDto>

    @Headers("Authorization: Client-ID $API_KEY")
    @GET("/photos/{id}")
    suspend fun getImage(
        @Path("id") imageId : String
    ) : UnsplashImageDto



}
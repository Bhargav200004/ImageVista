package com.example.imagevista.di

import com.example.imagevista.data.remote.UnsplashApiService
import com.example.imagevista.util.Constant.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object AppModule {

    private val contentType = "application/json".toMediaType()
    private val json = Json{ ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory(contentType))
        .baseUrl(BASE_URL)
        .build()

    val retrofitApiService : UnsplashApiService by lazy {
        retrofit.create(UnsplashApiService::class.java)
    }

}
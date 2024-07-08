package com.example.imagevista.data.remote.dto


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnsplashImageSearchResult(
    @SerialName("results")
    val image: List<UnsplashImageDto>,
    @SerializedName("total")
    val total: Int,
    @SerialName("total_pages")
    val totalPages: Int
)
package com.example.imagevista.data.remote.dto


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerializedName("links")
    val links: UserLinksDto,
    @SerializedName("name")
    val name: String,
    @SerialName("profile_image")
    val profileImage: ProfileImageDto ,
    @SerializedName("username")
    val username: String
)


@Serializable
data class ProfileImageDto(
    @SerializedName("small")
    val small: String
)

@Serializable
data class UserLinksDto(
    @SerializedName("html")
    val html: String,
    @SerializedName("likes")
    val likes: String,
    @SerializedName("photos")
    val photos: String,
    @SerializedName("portfolio")
    val portfolio: String,
    @SerializedName("self")
    val self: String
)
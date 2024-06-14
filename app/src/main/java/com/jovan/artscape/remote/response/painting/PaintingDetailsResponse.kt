package com.jovan.artscape.remote.response.painting

import com.google.gson.annotations.SerializedName

data class PaintingDetailsResponse(
    @SerializedName("photo") val photo: String,
    @SerializedName("price") val price: Int,
    @SerializedName("yearCreated") val yearCreated: Int,
    @SerializedName("genre") val genre: String,
    @SerializedName("description") val description: String,
    @SerializedName("artistId") val artistId: String,
    @SerializedName("media") val media: String,
    @SerializedName("title") val title: String,
    @SerializedName("availability") val availability: Boolean,
)

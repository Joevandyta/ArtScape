package com.jovan.artscape.remote.response.painting

import com.google.gson.annotations.SerializedName

data class AllPaintingResponse(
    @SerializedName("id")val id: String = "",
    @SerializedName("title")val title: String? = null,
    @SerializedName("photo") val photo: String? = null,
    @SerializedName("description")val description: String? = null,
    @SerializedName("genre")val genre: String? = null,
    @SerializedName("price")val price: String? = null,
    @SerializedName("artistId")val artistId: String? = null,
)

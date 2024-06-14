package com.jovan.artscape.remote.response

import com.google.gson.annotations.SerializedName
import com.jovan.artscape.remote.response.painting.AllPaintingResponse
import com.jovan.artscape.remote.response.user.AllUserResponse

data class SearchResponse(
    @SerializedName("artworks") val listPainting: List<AllPaintingResponse>,
    @SerializedName("users") val listUser: List<AllUserResponse>,
)

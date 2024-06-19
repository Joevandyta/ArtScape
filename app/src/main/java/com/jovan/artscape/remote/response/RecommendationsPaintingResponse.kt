package com.jovan.artscape.remote.response

import com.google.gson.annotations.SerializedName

data class RecommendationsPaintingResponse(
    @SerializedName("recommendations") val recommendations: List<String>,
)

package com.jovan.artscape.remote.request

import com.google.gson.annotations.SerializedName

data class RecommendationsPaintingRequest(
    @SerializedName("ratings") val ratings: List<List<Any>>,
    @SerializedName("num_recommendations") val numRecommendations: Int,
)

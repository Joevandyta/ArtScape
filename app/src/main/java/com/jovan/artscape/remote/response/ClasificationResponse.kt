package com.jovan.artscape.remote.response

import com.google.gson.annotations.SerializedName

class ClasificationListResponse {
    @SerializedName("predictions")
    val predictions: List<ClasificationResponse>? = null
}

class ClasificationResponse {
    @SerializedName("class")
    val className: String? = null

    @SerializedName("probability")
    val probability: Double = 0.0
}

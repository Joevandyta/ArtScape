package com.jovan.artscape.remote.response.address

import com.google.gson.annotations.SerializedName

data class VillageResponse(
    val id: String,
    @SerializedName("district_id")
    val districtId: String,
    val name: String
)

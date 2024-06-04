package com.jovan.artscape.remote.response

import com.google.gson.annotations.SerializedName

data class VillageResponse(
    val id: Int,
    @SerializedName("district_id")
    val districtId: Int,
    val name: String
)

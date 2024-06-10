package com.jovan.artscape.remote.response.address

import com.google.gson.annotations.SerializedName

data class DistrictResponse (
    val id: String,
    @SerializedName("regency_id")
    val regencyId: String,
    val name: String
)
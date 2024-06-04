package com.jovan.artscape.remote.response

import com.google.gson.annotations.SerializedName

data class DistrictResponse (
    val id: Int,
    @SerializedName("regency_id")
    val regencyId: Int,
    val name: String
)
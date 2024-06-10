package com.jovan.artscape.remote.response.address

import com.google.gson.annotations.SerializedName

data class RegenciesResponse (
    val id: String,
    @SerializedName("province_id")
    val provinceId: String,
    val name: String
    )
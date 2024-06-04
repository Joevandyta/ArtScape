package com.jovan.artscape.remote.response.address

import com.google.gson.annotations.SerializedName

data class RegenciesResponse (
    val id: Int,
    @SerializedName("province_id")
    val provinceId: Int,
    val name: String
    )
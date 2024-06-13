package com.jovan.artscape.remote.request

import com.google.gson.annotations.SerializedName

data class UpdateUserRequest(
    @SerializedName("name") val name: String,
    @SerializedName("description") val bio: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
)

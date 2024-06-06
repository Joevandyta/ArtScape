package com.jovan.artscape.remote.request

import com.google.gson.annotations.SerializedName

data class AddUserRequest(
    @SerializedName("nama") val nama: String,
    @SerializedName("email") val email: String,
    @SerializedName("deskripsi") val deskripsi: String,
    @SerializedName("minat") val minat: List<String>
)
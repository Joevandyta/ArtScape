package com.jovan.artscape.remote.response.user

import com.google.gson.annotations.SerializedName

data class AllUserResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,
    @SerializedName("interest")
    val interest: List<String>? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("picture")
    val picture: String? = null,
)

package com.jovan.artscape.remote.response.user

import com.google.gson.annotations.SerializedName

data class AllUserResponse(

    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("phoneNumber")
    val phoneNumber: String,

    @SerializedName("interest")
    val interest: List<String>,



    @SerializedName("description")
    val description: String,



    @SerializedName("email")
    val email: String,

    @SerializedName("picture")
    val picture: String
)

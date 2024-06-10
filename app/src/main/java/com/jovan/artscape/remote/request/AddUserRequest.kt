package com.jovan.artscape.remote.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class AddUserRequest(
    @SerializedName("idToken") val idToken: String,
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String,
    @SerializedName("description") val bio: String,
    @SerializedName("interest") var interest: List<String>
):Parcelable
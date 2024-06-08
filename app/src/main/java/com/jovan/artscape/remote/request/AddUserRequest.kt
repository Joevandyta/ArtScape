package com.jovan.artscape.remote.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class AddUserRequest(
    @SerializedName("nama") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("deskripsi") val bio: String,
    @SerializedName("minat") val interest: List<String>
):Parcelable
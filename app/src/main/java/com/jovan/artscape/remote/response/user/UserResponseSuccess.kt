package com.jovan.artscape.remote.response.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponseSuccess(
    val message: String,
    val uid: String,
    val name: String?,
    val phoneNumber: String
) : Parcelable
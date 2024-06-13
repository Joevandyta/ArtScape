package com.jovan.artscape.remote.response.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponseSuccess(
    val message: String,
    val uid: String,
) : Parcelable

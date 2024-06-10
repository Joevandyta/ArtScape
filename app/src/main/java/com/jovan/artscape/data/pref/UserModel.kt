package com.jovan.artscape.data.pref

data class UserModel(
    val uid: String,
    val token: String,
    val isLogin: Boolean = false
)
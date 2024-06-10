package com.jovan.artscape.remote.response

sealed class UserResponse<out T> {
    data class Success<out T>(val data: T) : UserResponse<T>()
    data class Error(val error: String, val details: String) : UserResponse<Nothing>()
}

data class SuccessResponse(
    val message: String,
    val uid: String
)

data class ErrorResponse(
    val error: String,
    val details: String? = ""
    // Other fields for error response
)
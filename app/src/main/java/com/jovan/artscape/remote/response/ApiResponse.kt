package com.jovan.artscape.remote.response

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val error: String, val details: String) : ApiResponse<Nothing>()
}


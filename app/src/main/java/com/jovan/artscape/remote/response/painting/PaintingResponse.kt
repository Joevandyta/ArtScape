package com.jovan.artscape.remote.response.painting

sealed class PaintingResponse<out T> {
    data class Success<out T>(val data: T) : PaintingResponse<T>()
    data class Error(val error: String) : PaintingResponse<Nothing>()
}

data class ErrorResponse(
    val error: String,
    val details: String? = ""
    // Other fields for error response
)
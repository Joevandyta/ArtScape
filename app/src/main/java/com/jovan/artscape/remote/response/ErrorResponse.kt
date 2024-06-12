package com.jovan.artscape.remote.response

data class ErrorResponse(
    val error: String,
    val details: String? = ""
// Other fields for error response
)

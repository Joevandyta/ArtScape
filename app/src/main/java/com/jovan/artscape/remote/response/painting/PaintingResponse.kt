package com.jovan.artscape.remote.response.painting

data class PaintingResponse(
    val id: Int,
    val title: String? = null,
    val description: String? = null,
    val media: String? = null,
    val genre: String? = null,
    val price: String? = null,
    val createdYear: String? = null,
    val artistId: String? = null,
    val keterangan: String? = null
)
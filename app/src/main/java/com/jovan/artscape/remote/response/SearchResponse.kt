package com.jovan.artscape.remote.response

import com.jovan.artscape.remote.response.painting.AllPaintingResponse
import com.jovan.artscape.remote.response.user.AllUserResponse

data class SearchResponse(
    val listPainting: List<AllPaintingResponse>,
    val listUser: List<AllUserResponse>,
)

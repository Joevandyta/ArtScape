package com.jovan.artscape.remote.api

import com.jovan.artscape.remote.request.AddUserRequest
import com.jovan.artscape.remote.request.LoginRequest
import com.jovan.artscape.remote.request.UpdateUserRequest
import com.jovan.artscape.remote.response.address.DistrictResponse
import com.jovan.artscape.remote.response.address.ProvinceResponse
import com.jovan.artscape.remote.response.address.RegenciesResponse
import com.jovan.artscape.remote.response.address.VillageResponse
import com.jovan.artscape.remote.response.painting.AllPaintingResponse
import com.jovan.artscape.remote.response.painting.PaintingDetailsResponse
import com.jovan.artscape.remote.response.painting.UploadResponseSuccess
import com.jovan.artscape.remote.response.user.AllUserResponse
import com.jovan.artscape.remote.response.user.UserResponseSuccess
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("provinces.json")
    suspend fun getProvince(): List<ProvinceResponse>

    @GET("regencies/{province_id}.json")
    suspend fun getRegencies(
        @Path("province_id") provinceId: String,
    ): List<RegenciesResponse>

    @GET("districts/{regencies_id}.json")
    suspend fun getDistrict(
        @Path("regencies_id") regenciesId: String,
    ): List<DistrictResponse>

    @GET("villages/{district_id}.json")
    suspend fun getVillage(
        @Path("district_id") districtId: String,
    ): List<VillageResponse>

    // USER
    @POST("api/auth/google")
    suspend fun addUserData(
        @Body addUserRequest: AddUserRequest,
    ): Response<UserResponseSuccess>

    @POST("api/auth/google")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): Response<UserResponseSuccess>

    @GET("api/user/{id}")
    suspend fun getUserData(
        @Path("id") id: String,
    ): Response<AllUserResponse>

    @PUT("api/user/{id}")
    suspend fun editUser(
        @Path("id") id: String,
        @Body updateUserRequest: UpdateUserRequest,
    ): Response<UserResponseSuccess>

    /*    @DELETE("api/pengguna/{id}")
        suspend fun deleteUser(
            @Path("id") id: String
        ): retrofit2.Response<SuccessResponse>
     */

    // PAINTING VBN M,./
    @Multipart
    @POST("api/artwork/add")
    suspend fun uploadPainting(
        @Part file: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("media") media: RequestBody,
        @Part("genre") genre: RequestBody,
        @Part("price") price: RequestBody,
        @Part("yearCreated") yearCreated: RequestBody,
        @Part("artistId") artistId: RequestBody,
    ): Response<UploadResponseSuccess>

    @GET("api/artwork/allartwork")
    suspend fun getAllPainting(): Response<List<AllPaintingResponse>>

    @GET("api/artwork/{id}")
    suspend fun getPaintingDetail(
        @Path("id") id: String,
    ): Response<PaintingDetailsResponse>
}

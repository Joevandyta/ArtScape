package com.jovan.artscape.remote.api

import com.jovan.artscape.remote.request.AddUserRequest
import com.jovan.artscape.remote.response.address.DistrictResponse
import com.jovan.artscape.remote.response.address.ProvinceResponse
import com.jovan.artscape.remote.response.address.RegenciesResponse
import com.jovan.artscape.remote.response.address.VillageResponse
import com.jovan.artscape.remote.response.user.SuccessResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("provinces.json")
    suspend fun getProvince(
    ):List<ProvinceResponse>

    @GET("regencies/{province_id}.json")
    suspend fun getRegencies(
        @Path("province_id") provinceId: String
    ):List<RegenciesResponse>

    @GET("districts/{regencies_id}.json")
    suspend fun getDistrict(
        @Path("regencies_id") regenciesId: String
    ):List<DistrictResponse>

    @GET("villages/{district_id}.json")
    suspend fun getVillage(
        @Path("district_id") districtId: String
    ):List<VillageResponse>

    //USER
    @POST("api/pengguna/add")
    suspend fun addUser(
        @Body addUserRequest: AddUserRequest
    ): retrofit2.Response<SuccessResponse>

    @GET("api/pengguna/{id}")
    suspend fun getUserDetail(
        @Path("id") id: String
    ): retrofit2.Response<SuccessResponse>

    @PUT("api/pengguna/{id}")
    suspend fun editUser(
        @Path("id") id: String,
        @Body addUserRequest: AddUserRequest
    ): retrofit2.Response<SuccessResponse>

    @DELETE("api/pengguna/{id}")
    suspend fun deleteUser(
        @Path("id") id: String
    ): retrofit2.Response<SuccessResponse>

    //PAINTING
    @Multipart
    @POST("api/karyaSeni/add")
    suspend fun uploadPainting(
        @Part("judul") title: RequestBody,
        @Part("deskripsi") description: RequestBody,
        @Part media: MultipartBody.Part,
        @Part("genre") genre: RequestBody,
        @Part("harga") price: RequestBody,
        @Part("tahunBuat") createdYear: RequestBody,
        @Part("idSeniman") artistId: RequestBody,
        @Part("keterangan") keterangan: RequestBody
    ): retrofit2.Response<SuccessResponse>

    @GET("api/karyaSeni/{id}")
    suspend fun getPaintingDetail(
        @Path("id") id: String
    ): retrofit2.Response<SuccessResponse>

    @PUT("api/karyaSeni/{id}")
    suspend fun editPainting(
        @Path("id") id: String,
        @Part("judul") title: RequestBody,
        @Part("deskripsi") description: RequestBody,
        @Part media: MultipartBody.Part,
        @Part("genre") genre: RequestBody,
        @Part("harga") price: RequestBody,
        @Part("tahunBuat") createdYear: RequestBody,
        @Part("idSeniman") artistId: RequestBody,
        @Part("keterangan") keterangan: RequestBody
    ): retrofit2.Response<SuccessResponse>

    @DELETE("api/karyaSeni/{id}")
    suspend fun deletePainting(
        @Path("id") id: String
    ): retrofit2.Response<SuccessResponse>

}
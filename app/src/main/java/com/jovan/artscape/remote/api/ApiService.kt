package com.jovan.artscape.remote.api

import com.jovan.artscape.remote.request.AddUserRequest
import com.jovan.artscape.remote.response.address.DistrictResponse
import com.jovan.artscape.remote.response.address.ProvinceResponse
import com.jovan.artscape.remote.response.address.RegenciesResponse
import com.jovan.artscape.remote.response.address.VillageResponse
import com.jovan.artscape.remote.response.user.SuccessResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    @POST("api/pengguna/add")
    suspend fun addUser(
        @Body addUserRequest: AddUserRequest
    ): SuccessResponse

}
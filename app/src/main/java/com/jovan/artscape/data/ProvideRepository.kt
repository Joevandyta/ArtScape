package com.jovan.artscape.data

import com.jovan.artscape.data.pref.ProvidePreference
import com.jovan.artscape.data.pref.UserModel
import com.jovan.artscape.remote.api.RetrofiClient
import com.jovan.artscape.remote.request.AddUserRequest
import com.jovan.artscape.remote.request.LoginRequest
import com.jovan.artscape.remote.response.SuccessResponse
import com.jovan.artscape.remote.response.address.DistrictResponse
import com.jovan.artscape.remote.response.address.ProvinceResponse
import com.jovan.artscape.remote.response.address.RegenciesResponse
import com.jovan.artscape.remote.response.address.VillageResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class ProvideRepository private constructor(
    private var providePreference: ProvidePreference
) {

    //Session DataStore
    suspend fun saveSession(user: UserModel) {
        providePreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return providePreference.getSession()
    }

    suspend fun logout() {
        providePreference.logout()
    }

    //API Region
    suspend fun getProvinces(): List<ProvinceResponse> {
        return RetrofiClient.getApiRegion().getProvince()
    }

    suspend fun getRegencies(id: String): List<RegenciesResponse> {
        return RetrofiClient.getApiRegion().getRegencies(id)
    }

    suspend fun getDistricts(id: String): List<DistrictResponse> {
        return RetrofiClient.getApiRegion().getDistrict(id)
    }

    suspend fun getVillages(id: String): List<VillageResponse> {
        return RetrofiClient.getApiRegion().getVillage(id)
    }

    suspend fun addUserData(addUserRequest: AddUserRequest): Response<SuccessResponse> {
        return RetrofiClient.getApiArtSpace().addUserData(addUserRequest)
    }

    suspend fun setlogin(loginRequest: LoginRequest): Response<SuccessResponse> {
        return RetrofiClient.getApiArtSpace().login(loginRequest)
    }
    suspend fun getUserData(userId: String): AddUserRequest {
        return RetrofiClient.getApiArtSpace().getUserData(userId)
    }

    suspend fun uploadPainting(
        title: RequestBody,
        description: RequestBody,
        media: MultipartBody.Part,
        genre: RequestBody,
        price: RequestBody,
        createdYear: RequestBody,
        artistId: RequestBody,
        keterangan: RequestBody
    ): Response<SuccessResponse> {
        return RetrofiClient.getApiArtSpace().uploadPainting(
            title,
            description,
            media,
            genre,
            price,
            createdYear,
            artistId,
            keterangan
        )
    }

    companion object {
        @Volatile
        private var instance: ProvideRepository? = null
        fun getInstance(
            providePreference: ProvidePreference
        ): ProvideRepository =
            instance ?: synchronized(this) {
                instance ?: ProvideRepository(providePreference)
            }.also { instance = it }
    }
}
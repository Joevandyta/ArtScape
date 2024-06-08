package com.jovan.artscape.data

import com.jovan.artscape.data.pref.ProvidePreference
import com.jovan.artscape.data.pref.UserModel
import com.jovan.artscape.remote.api.RetrofiClient
import com.jovan.artscape.remote.request.AddUserRequest
import com.jovan.artscape.remote.response.address.DistrictResponse
import com.jovan.artscape.remote.response.address.ProvinceResponse
import com.jovan.artscape.remote.response.address.RegenciesResponse
import com.jovan.artscape.remote.response.address.VillageResponse
import com.jovan.artscape.remote.response.user.SuccessResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class ProvideRepository private constructor(
    private var providePreference: ProvidePreference
){

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

    suspend fun addUser(addUserRequest: AddUserRequest): Response<SuccessResponse>{
        return RetrofiClient.getApiArtSpace().addUser(addUserRequest)
    }


    suspend fun uploadPainting(addUserRequest: AddUserRequest): Response<SuccessResponse>{
        return RetrofiClient.getApiArtSpace().addUser(addUserRequest)
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